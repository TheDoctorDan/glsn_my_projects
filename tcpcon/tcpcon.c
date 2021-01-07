/*
	Written by Ross Cartlidge (rossc@extro.ucc.su.oz)
	University Computer Service
	March 1989

	tcpcon - Program to connect a tty to a tcp socket
	Developed on a MIPS M/2000 running SysVr3
	Ported to BSD/SUN-OS
*/
#include	<fcntl.h>
#include	<sys/ioctl.h>
#include	<sys/signal.h>
#include	<sys/types.h>
#include	<syslog.h>
#include	<errno.h>
#include	<stdio.h>
#include	<string.h>
#if defined(SYSTYPE_SYSV)
#include	<malloc.h>
#else
extern char	*malloc();
extern char	*calloc();
#endif

#include <sys/time.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <setjmp.h>

#define max(a,b) 	((a)<(b) ? (b) : (a))
#define min(a,b) 	((a)>(b) ? (b) : (a))

#if defined(SYSTYPE_BSD43)
#define sigset		signal
#define sighold(s)	sigblock(sigmask(s))
#define sigrelse(s)	sigsetmask(sigsetmask(-1) & ~sigmask(s))
extern int		errno;
#endif

#if !defined(FD_SET)
#define	fd_set	int
#define	FD_SET(n,p)	(*(p) |= 1 << (n))
#define	FD_CLR(n,p)	(*(p) &= ~(1 << (n)))
#define	FD_ISSET(n,p)	(*(p) & 1 << (n))
#define	FD_ZERO(p)	(*(p) = 0)
#endif

struct buf
{
	char	*buf;
	int	cnt;
};

jmp_buf	env;
int	pid;
int	bufsize		= 256;
int	server;
int	linger_time	= 5;
long	ofrom_socket;
long	oto_socket;
long	orfrom_socket;
long	orto_socket;
long	from_socket;
long	to_socket;
long	rfrom_socket;
long	rto_socket;

main(argc, argv)
int	argc;
char	*argv[];
{
	struct hostent		*host;
	struct servent		*serv;
	struct sockaddr_in	sin;
	int			s;
	int			i;
	int			r;
	char			perror_fmt[128];
	char			usage[128];
	int			p;
	fd_set			rfds;
	fd_set			efds;
	fd_set			wfds;
	int			mintime		= 2;
	int			minreads	= 2;
	char			*dev		= (char *)0;
	char			*kdev		= (char *)0;
	char			*statfile	= (char *)0;
	FILE			*sf		= (FILE *)0;
	char			*basename;
	int			update		= 30;
	long			slw;
	long			olw;
	long			lw;
	int			et;
	int			errflg		= 0;
	void			terminate();
	int			c;
	int			status;
	struct buf		*bufs;
	int			timeout();
	int			con_type;
	extern char		*optarg;
	extern int		optind;

	sprintf(perror_fmt, "PERROR_FMT=%s: %%t %%s%%m - (%%e)", argv[0]);
	sprintf
	(
		usage,
		"USAGE: %s [-a] [-t mintime ] [-r minreads] [-l pty] [-k tty] <IP Address> <TCP Port>\n", argv[0]
	);
#if defined(SYSTYPE_SYSV)
	putenv(perror_fmt);
#endif
	while ((c = getopt(argc, argv, "a:t:l:r:k:s:b:u:")) != -1)
		switch (c)
		{
		case 'a':
			linger_time = atoi(optarg);
			break;
		case 'k':
			kdev = optarg;
			break;
		case 't':
			mintime = atoi(optarg);
			break;
		case 'r':
			minreads = atoi(optarg);
			break;
		case 'l':
			dev = optarg;
			break;
		case 's':
			statfile = optarg;
			break;
		case 'b':
			bufsize = atoi(optarg);
			break;
		case 'u':
			update = atoi(optarg);
			break;
		case '?':
			errflg++;
		}
	if (basename = strrchr(argv[0], '/'))
		basename++;
	else
		basename = argv[0];
	if (*basename == 't')
		con_type =  SOCK_STREAM;
	else
		con_type =  SOCK_DGRAM;
	server = (basename[3] == 's');
	if (server)
	{
		if (errflg || optind >= argc)
		{
			fputs(usage, stderr);
			exit(2);
		}
		optind--;	/* make port code same for connect */
	}
	else
	{
		if (errflg || optind + 1 >= argc)
		{
			fputs(usage, stderr);
			exit(2);
		}
	}
	if (server)
		sin.sin_addr.s_addr = INADDR_ANY;
	else
	{
		if ((sin.sin_addr.s_addr = inet_addr(argv[optind])) != -1)
			sin.sin_family = AF_INET;
		else
		{
			if (host = gethostbyname(argv[optind]))
			{
				sin.sin_family = host->h_addrtype;
				memcpy(&sin.sin_addr.s_addr, host->h_addr, host->h_length);
			}
			else
			{
				fprintf
				(
					stderr,
					"%s: %s: unknown host\n",
					argv[0],
					argv[optind]
				);
				exit(2);
			}
		}
	}
	if (serv = getservbyname(argv[optind + 1], "tcp"))
		sin.sin_port = serv->s_port;
	else
		if
		(
			(
				sin.sin_port
				=
				htons
				(
					(short)strtol(argv[optind + 1],
					(char **)0, 0)
				)
			)
			<=
			0
		)
		{
			fprintf
			(
				stderr,
				"%s: %s: unknown service\n",
				argv[0],
				argv[optind + 1]
			);
			exit(2);
		}
	if ((s = connectsocket(&sin, con_type)) < 0)
		exit(1);
	if ((bufs = (struct buf *)calloc(max(minreads, 1) ,sizeof (struct buf))) == (char *)0)
	{
		perror("calloc bufs");
		exit(1);
	}
	for (i = 0; i < max(minreads, 1); i++)
	{
		if ((bufs[i].buf = malloc(bufsize)) == (char *)0)
		{
			perror("calloc bufs");
			exit(1);
		}
	}
	if (!server)
	{
		sigset(SIGALRM, timeout);
		sighold(SIGALRM);
		alarm(mintime);
		if (setjmp(env) == 0)
			for (i = 0; i < minreads; i++)
			{
				sigrelse(SIGALRM);
				r = read(s, bufs[i].buf, bufsize);
				sighold(SIGALRM);
				if (r <= 0)
					exit(1);
				else
					bufs[i].cnt = r;
			}
		alarm(0);
		sigset(SIGALRM, SIG_IGN);
		sigrelse(SIGALRM);
	}
	if (dev)
	{
		close(0);
		close(1);
		sighold(SIGTERM);
		if (pid = fork())
		{
			sigset(SIGTERM, terminate);
			sigrelse(SIGTERM);
			if (kdev != (char *)0 && open(kdev, O_RDWR) == -1)
				kill(pid, SIGTERM);
#if defined(SYSTYPE_BSD43)
			ioctl(0, TIOCNOTTY, 0);
			setpgrp(0, getpid());
#endif
#if defined(SYSTYPE_SYSV)
			setpgrp();
#endif
			while (wait(&status) != -1 || errno == EINTR)
				;
			if ((status & 0xff) == 0 && (status >> 8 & 0xff))
				exit(status >> 8 & 0xff);
			else
				exit(0);
		}
#if defined(SYSTYPE_BSD43)
		ioctl(0, TIOCNOTTY, 0);
		setpgrp(0, getpid());
#endif
#if defined(SYSTYPE_SYSV)
		setpgrp();
#endif
		sigrelse(SIGTERM);
		if (open(dev, O_RDWR) == -1)
		{
			perror(dev);
			exit(1);
		}
		dup(0);
	}
	
	time(&olw);
	time(&slw);
	if (!server)
		for (i = 0; i < minreads && bufs[i].cnt > 0; i++)
			if (Write(1, bufs[i].buf, bufs[i].cnt, &from_socket, &rfrom_socket) != bufs[i].cnt)
				exit(0);
	if (statfile && (sf = fopen(statfile, "w")) == NULL)
	{
		perror(statfile);
		exit(1);
	}
	time(&lw);
	if (sf)
	{
		et = max(1, lw -slw);
		fprintf(sf,"%10s%8s%10s%8s%10s%8s%10s%8s%8s\n",
			"chars <-",
			"c/s <-",
			"reads <-",
			"r/s <-",
			"chars ->",
			"c/s ->",
			"reads ->",
			"r/s ->",
			"time"
		);
		fprintf(sf,"%10d%8d%10d%8d%10d%8d%10d%8d%8d\n",
			from_socket,
			(from_socket - ofrom_socket)/et,
			rfrom_socket,
			(rfrom_socket - orfrom_socket)/et,
			to_socket,
			(to_socket - oto_socket)/et,
			rto_socket,
			(rto_socket - orto_socket)/et,
			et
		);
		fflush(sf);
		rewind(sf);
		olw = lw;
		ofrom_socket = from_socket;
		oto_socket = to_socket;
		orfrom_socket = rfrom_socket;
		orto_socket = rto_socket;
	}
	for (;;)
	{
		char	*buf	= bufs[0].buf;

		FD_ZERO(&rfds);
		FD_ZERO(&efds);
		FD_SET(0, &rfds);
		FD_SET(s, &rfds);
		FD_SET(0, &efds);
		FD_SET(s, &efds);
		select(s + 1, &rfds, (fd_set *)0, &efds, (struct timeval *)0);
		if (sf && time(&lw) > olw + update)
		{
			et = max(1, lw -olw);
			fprintf(sf,"%10s%8s%10s%8s%10s%8s%10s%8s%8s\n",
				"chars <-",
				"c/s <-",
				"reads <-",
				"r/s <-",
				"chars ->",
				"c/s ->",
				"reads ->",
				"r/s ->",
				"time"
			);
			fprintf(sf,"%10d%8d%10d%8d%10d%8d%10d%8d%8d\n",
				from_socket,
				(from_socket - ofrom_socket)/et,
				rfrom_socket,
				(rfrom_socket - orfrom_socket)/et,
				to_socket,
				(to_socket - oto_socket)/et,
				rto_socket,
				(rto_socket - orto_socket)/et,
				lw - slw
			);
			fflush(sf);
			rewind(sf);
			olw = lw;
			ofrom_socket = from_socket;
			oto_socket = to_socket;
			orfrom_socket = rfrom_socket;
			orto_socket = rto_socket;
		}
		if (FD_ISSET(s, &rfds) || FD_ISSET(s, &efds))
		{
			if ((r = read(s, buf, bufsize)) <= 0)
			{
				perror("read");
				exit(0);
			}
			if (Write(1, buf, r, &from_socket, &rfrom_socket) != r)
			{
				perror("write");
				exit(0);
			}
		}
		if ((FD_ISSET(0, &rfds) || FD_ISSET(0, &efds)))
		{
			if ((r = read(0, buf, bufsize)) <= 0)
			{
				perror("read");
				exit(0);
			}
			if (Write(s, buf, r, &to_socket, &rto_socket) != r)
			{
				perror("write");
				exit(0);
			}
		}
	}
}

connectsocket(sinp, t)
struct sockaddr_in	*sinp;
int			t;
{
	int		s;
	int		l;
	int		sockopt;
	struct linger	lg;

	if ((s = socket(AF_INET, t, 0)) <  0)
	{
		perror("socket");
		return -1;
	}
	l = sizeof *sinp;
	if (server)
	{
		if (bind(s, sinp, l) < 0)
		{
			perror("bind");
			exit(1);
		}
		if (t == SOCK_STREAM)
		{
			listen(s, 5);
			if ((s = accept(s, (struct sockaddr *)0, (int *)0)) < 0)
			{
				perror("accept");
				exit(1);
			}
		}
	}
	else
	{
		if (connect(s, sinp, l) < 0)
		{
			perror("connect");
			return -1;
		}
	}
	if
	(
		t == SOCK_STREAM
		&&
		setsockopt
		(
			s,
			SOL_SOCKET,
			SO_RCVBUF,
			(sockopt = bufsize, (char *)&sockopt),
			sizeof sockopt
		)
		<
		0
	)
	{
		perror("setsockopt(SO_RCVBUF)");
		return -1;
	}
	if
	(
		t == SOCK_STREAM
		&&
		setsockopt
		(
			s,
			SOL_SOCKET,
			SO_SNDBUF,
			(sockopt = bufsize, (char *)&sockopt),
			sizeof sockopt
		)
		<
		0
	)
	{
		perror("setsockopt(SO_SNDBUF)");
		return -1;
	}
	if
	(
		t == SOCK_STREAM
		&&
		setsockopt
		(
			s,
			SOL_SOCKET,
			SO_LINGER,
			(lg.l_onoff = 1, lg.l_linger = linger_time,  &lg),
			sizeof lg
		)
		<
		0
	)
	{
		perror("setsockopt(SO_LINGER)");
		return -1;
	}
	if
	(
		t == SOCK_STREAM
		&&
		setsockopt
		(
			s,
			SOL_SOCKET,
			SO_KEEPALIVE,
			(sockopt = 1, (char *)&sockopt),
			sizeof sockopt
		)
		<
		0
	)
	{
		perror("setsockopt(SO_KEEPALIVE)");
		return -1;
	}
	return s;
}

timeout(s)
int	s;
{
	longjmp(env, 1);
}

void
terminate(s)
int	s;
{
	kill(pid, s);
}

Write(f, buf, len, cnt, wcnt)
int	f;
char	*buf;
int	len;
long	*cnt;
long	*wcnt;
{
	int	i;
	int total,left;

	while (( i = write(f,buf+total,len - total)) != len - total) {
		if (i >= 0) {
	  		*cnt += i;
			total += i;
			if (total == len)
			break;
		} else return( -1 );
		if (total == len )
			break;
	}
	(*wcnt)++;
	return total;
}
