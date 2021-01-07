/*
	Written by Ross Cartlidge (rossc@extro.ucc.su.oz)
	University Computer Service
	March 1989

	nd - Program to connect a tty to a tcp socket
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

#if defined(SYSTYPE_BSD43)
#define sigset signal
#define sighold(s) sigblock(sigmask(s))
#define sigrelse(s)	sigsetmask(sigsetmask(-1) & ~sigmask(s))
#endif

int		pid;
int		to;
int		term;
char		*piddir;

main(argc, argv)
int	argc;
char	*argv[];
{
	int		i;
	char		sh_c[2048];
	int		ret;
	char		*dev		= (char *)0;
	int		failtime	= 15; /* > 2m/10 for init */
	int		cleantime	= 2; /* tcp connection to cleanup*/
	int		respawn		= 0; /* respawn process*/
	int		daemon		= 0; /* run as background process*/
	char		*tag		= (char *)0;
	int		c;
	int		errflg		= 0;
	int		retval;
	void		terminate();
	void		timeout();
	extern int	errno;
	extern char	*sys_errlist[];
	extern char	*optarg;
	extern int	optind;

	openlog(argv[0], 0,  LOG_LOCAL0);
	while ((c = getopt(argc, argv, "r&l:f:c:d:t:")) != -1)
		switch (c)
		{
		case 'f':
			failtime = atoi(optarg);
			break;
		case 'c':
			cleantime = atoi(optarg);
			break;
		case 'l':
			dev = optarg;
			break;
		case 'd':
			piddir = optarg;
			break;
		case 't':
			tag = optarg;
			break;
		case 'r':
			respawn++;
			break;
		case '&':
			daemon++;
			break;
		case '?':
			errflg++;
		}
	if (errflg || optind >= argc)
	{
		syslog(LOG_ERR, "Usage: %s [ -d <pty> ] <command>", argv[0]);
		exit(2);
	}
	if (daemon)
	{
		if (fork())
			exit(0);
#if defined(SYSTYPE_BSD43)
		ioctl(0, TIOCNOTTY, 0);
		setpgrp(0, getpid());
#endif
#if defined(SYSTYPE_SYSV)
		setpgrp();
#endif
	}
	strcpy(sh_c, "exec");
	for (i = optind; i < argc; i++)
	{
		strcat(sh_c, " ");
		strcat(sh_c, argv[i]);
	}
	do
	{
		sighold(SIGTERM);
		sighold(SIGHUP);
		sigset(SIGTERM, terminate);
		sigset(SIGHUP, terminate);
		if (pid = fork())
		{
			char	pidf[64];
			FILE	*pidfs;

			if (piddir)
			{
				if (!tag)
				{
					if (dev)
					{
						if (tag = strrchr(dev , '/'))
							tag++;
						else
							tag = dev;
						sprintf(pidf, "%s/%s", piddir, tag);
					}
					else
						sprintf(pidf, "%s/pid.%d", piddir, pid);
				}
				else
					sprintf(pidf, "%s/%s", piddir, tag);
				if ((pidfs = fopen(pidf, "w")) == NULL)
					syslog(LOG_ERR, "open(%s) - %s", pidf, sys_errlist[errno]);
				else
				{
					fprintf(pidfs, "%d\n", pid);
					fclose(pidfs);
				}
			}
			if (failtime > 0)
			{
				sigset(SIGALRM, timeout);
				alarm(failtime);
			}
			else
				to = 1;
			sigrelse(SIGTERM);
			sigrelse(SIGHUP);
			while (wait(&ret) != -1 || errno == EINTR)
				;
			if ((ret & 0xff) == 0)
			{
				if ((ret >> 8 & 0xff) != 0)
				{
					unlink(pidf);
					syslog(LOG_ERR, "Failed(%d) %s", ret >> 8, sh_c);
					sighold(SIGALRM);
					while (!to)
						sigpause(SIGALRM);
					sigrelse(SIGALRM);
					retval = 1;
					continue;
				}
				else
					syslog(LOG_DEBUG, "Completed %s", sh_c);
			}
			else
				syslog(LOG_DEBUG, "Killed(%d) %s", ret, sh_c);
			unlink(pidf);
			sleep(cleantime);
			retval = 0;
			continue;
		}
#if defined(SYSTYPE_BSD43)
		ioctl(0, TIOCNOTTY, 0);
		setpgrp(0, getpid());
#endif
#if defined(SYSTYPE_SYSV)
		setpgrp();
#endif
		close(0);
		close(1);
		close(2);
		sigset(SIGTERM, SIG_DFL);
		sigset(SIGHUP, SIG_DFL);
		sigrelse(SIGTERM);
		sigrelse(SIGHUP);
		if (dev)
		{
			if (open(dev, O_RDWR) == -1)
			{
				syslog(LOG_ERR, "open(%s) - %s", dev, sys_errlist[errno]);
				exit(2);
			}
			dup(0);
			dup(0);
		}
		else
		{
			open("/dev/null", O_RDONLY);
			open("/dev/null", O_WRONLY);
			dup(1);
		}
		syslog(LOG_DEBUG, "Started %s", sh_c);
		execl("/bin/sh", "sh", "-c", sh_c, (char *)0);
		syslog(LOG_ERR, "Exec failed %s", sh_c);
	}
	while (!term && respawn);
}

void
terminate(s)
int	s;
{
	if (s == SIGTERM)
		term = 1;
	kill(-pid, SIGTERM);
}

void
timeout(s)
int	s;
{
	to = 1;
}
