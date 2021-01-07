/*
	Written by Ross Cartlidge (rossc@extro.ucc.su.oz)
	University Computer Service
	June 1990

	tcpexec - Program to connect a process to a tcp socket
	Developed on a MIPS M/2000 running SysVr3
	Ported to BSD/SUN-OS
*/
#include	<fcntl.h>
#include	<sys/ioctl.h>
#include	<sys/types.h>
#include	<sys/signal.h>
#include	<syslog.h>
#include	<errno.h>
#include	<stdio.h>
#include	<string.h>

#include <sys/time.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

int	bufsize		= 256;
int	retry		= 1;
int	server;
int	linger_time	= 5;

main(argc, argv)
int	argc;
char	*argv[];
{
	struct hostent		*host;
	struct servent		*serv;
	struct sockaddr_in	sin;
	char			sh_c[2048];
	int			s;
	int			i;
	char			perror_fmt[128];
	char			usage[128];
	char			renv[64];
	char			*basename;
	int			c;
	int			con_type;
	int			errflg;
	extern int		errno;
	extern char		*optarg;
	extern int		optind;

	sprintf(perror_fmt, "PERROR_FMT=%s: %%t %%s%%m - (%%e)", argv[0]);
	sprintf
	(
		usage,
		"USAGE: %s [-a time] [-s] [-b bufsize] <IP Address> <TCP Port> cmd ...\n", argv[0]
	);
#if defined(SYSTYPE_SYSV)
	putenv(perror_fmt);
#endif
	while ((c = getopt(argc, argv, "a:rsb:")) != -1)
		switch (c)
		{
		case 'a':
			linger_time = atoi(optarg);
			break;
		case 'r':
			retry = 0;
			break;
		case 's':
			server = 1;
			break;
		case 'b':
			bufsize = atoi(optarg);
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
	if (server)
	{
		if (errflg || optind + 1 >= argc)
		{
			fputs(usage, stderr);
			exit(2);
		}
		optind--;	/* make port code same for connect */
	}
	else
	{
		if (errflg || optind + 2 >= argc)
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
	*sh_c = 0;
#if defined(SYSTYPE_BSD43)
	sprintf(renv, "%d", s);
	setenv("REMFD", renv, 1);
#else
	sprintf(renv, "REMFD=%d", s);
	putenv(renv);
#endif
	for (i = optind + 2; i < argc; i++)
	{
		strcat(sh_c, " ");
		strcat(sh_c, argv[i]);
	}
	execl("/bin/sh", "sh", "-c", sh_c, (char *)0);
	syslog(LOG_ERR, "Exec failed %s", sh_c);
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
		if (retry)
			while (connect(s, sinp, l) < 0)
				;
		else
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
