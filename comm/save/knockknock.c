/* knockknock.c */
/*
|------------------------------------------------------------------------------|
|	Copyright (c) 1985, 1986, 1987, 1988, 1989, 1990, 1991, 1992, 1993     |
|	Gleeson and Associates                                                 |
|	All Rights Reserved                                                    |
|                                                                              |
|	THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|	Gleeson And Associates                                                 |
|	The copyright notice above does not evidence any                       |
|	actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/

#include <stdio.h>
#include <string.h>
#include <termio.h>
#include <signal.h>
#include <ctype.h>
#include "atxy.h"
#include "bytecpy.h"
#include "fgetline.h"

extern void exit(), perror();
extern int	ioctl();
struct termio was_t, is_t;
static int	(*signal_save[NSIG +1])();
extern unsigned	sleep();
extern char	*mktemp();

int	fitty;
char	c;

int	main_err, timeout, start_time;
;

main(argc, argv)
int	argc;
char	*argv[];
{
	FILE	 * flp;
	char	dmys[101], command[300], filename[100], entry[101], phone[101], response[101], name[101];
	int	ier, status, i;
	main_err = 0;
	timeout = 0;
	if (argc != 4) {
		fprintf(stderr, "knockknock: usage  knockknock /dev/ttyxx  user file  modem_type\n");
		exit(1);
	}

	if (strcmp(argv[3], "AJ1259AD") == 0)
		goto good_modem;
	if (strcmp(argv[3], "HAYES") == 0)
		goto good_modem;
	if (strcmp(argv[3], "M224E") == 0)
		goto good_modem;
	if (strcmp(argv[3], "NCR") == 0)
		goto good_modem;
	fprintf(stderr, "%s is not a known modem type\n");
	exit(1);
good_modem:
	strcpy(filename, "/tmp/knockXXXXXX");
	mktemp(filename);
	goto again;
failed:
	fflush(stdout);
again:
	strcpy(command, "stty 526:1805:9a9:31:7f:1c:8:18:1:0:0:0 <");
	strcpy(command, "stty 526:1805:da9:31:7f:1c:8:18:1:0:0:0 <");
	strcat(command, argv[1]);
	strcat(command, " >/dev/ttyb 2>/dev/ttyb");
	system(command);
	if (!isatty(stdin->_file)) {
		fprintf(stderr, "knockknock: %s is not a tty \n", argv[1]);
		exit(1);
	}
	strcpy(command, "stty 526:1805:9a9:31:7f:1c:8:18:1:0:0:0 <");
	strcat(command, argv[1]);
	strcat(command, " >/dev/ttyb 2>/dev/ttyb");
	system(command);
	fitty = stdin->_file;
	;
	finitat(stdin);
	set();
	status = 0;
loop:
	if (in_line(dmys, 80) < 0)
		goto failed;
	switch (status) {
	case 0:
		if (strcmp(dmys, "knock knock") == 0) {
			fprintf(stdout, "Who's there?\n");
			status++;
		}
		break;
	case 1:
		strcpy(command, "grep '^");
		strcat(command, dmys);
		strcat(command, ":' /etc/passwd >/dev/null");
		ier = system(command);
		if (ier)
			status = 0;
		else {
			fprintf(stdout, "%s who?\n", dmys);
			status++;
		}
		break;
	case 2:
		strcpy(command, "grep '^");
		strcat(command, dmys);
		strcat(command, ":' ");
		strcat(command, argv[2]);
		strcat(command, " >");
		strcat(command, filename);
		ier = system(command);
		if (ier)
			status = 0;
		else {
			flp = fopen(filename, "r");
			if (flp == NULL) {
				fprintf(stderr, "knockknock: open of %s failed\n", filename);
				perror("knockknock");
				exit(1);
			}
			fgetline(flp, entry, 100);
			fclose(flp);
			unlink(filename);
			*phone = 0;
			strcpy(name, entry);
			for (i = 0; i < strlen(entry); i++) {
				if (entry[i] == ':') {
					name[i] = 0;
					strcpy(phone, &entry[i+1]);
					goto have_phone;
				}
			}
			fprintf(stdout, "no phone #\n");
			status = 0;
			break;
have_phone:
			fprintf(stdout, "OH %s\n", name);
			fprintf(stdout, "Call you right back\n");
			fflush(stdout);
			goto make_call;
		}
		break;
	default:
		status = 0;
	}
	goto loop;
make_call:
	fflush(stdout);
	sleep(3);
	strcpy(command, "stty 0 <");
	strcat(command, argv[1]);
	system(command);
	sleep(6);
	strcpy(command, "stty 526:1805:9a9:31:7f:1c:8:18:1:0:0:0 <");
	strcpy(command, "stty 526:1805:da9:31:7f:1c:8:18:1:0:0:0 <");
	strcat(command, argv[1]);
	system(command);
	if (strcmp(argv[3], "AJ1259AD") == 0) {
		sleep(10);
		fprintf(stderr,"sending control E\n");
		fprintf(stdout, "\r");
		fflush(stdout);
		for(i=0;i<10;i++){
			fprintf(stderr,"waiting for * \n");
			if (match_string("\n\n* "))
				goto aj_continue;
		}
		fprintf(stderr,"sending I \n");
		fprintf(stdout, "I\r");
		fflush(stdout);
		goto failed;
aj_continue:
		fprintf(stderr,"sending D%s \n",phone);
		fprintf(stdout, "D%s\r", phone);
		fflush(stdout);
		fprintf(stderr,"waiting for ON LINE \n");
		sprintf(dmys, "D%s\n\n\nDIALING: ON LINE", phone);
		if (!match_string(dmys)) {
			fprintf(stderr,"sending I \n");
			fprintf(stdout, "I\r");
			fflush(stdout);
			goto failed;
		}
		fprintf(stderr,"got ON LINE \n");
	}

	if (strcmp(argv[3], "NCR") == 0) {
		fprintf(stdout, "ATDT%s\r", phone);
		fflush(stdout);
		sprintf(dmys, "ATDT%s\n", phone);
		fprintf(stderr, "reading phone echo\n");
		if (!match_string(dmys))
			goto failed;
		sprintf(dmys, "\n\nCONNECT 1200\n\n");
		timeout = 80;
		fprintf(stderr, "reading response\n");
		if (!match_string(dmys)) {
			fprintf(stderr, "response invalid\n");
			timeout = 0;
			goto failed;
		}
		fprintf(stderr, "valid response received\n");
		timeout = 0;
	}

	if (strcmp(argv[3], "M224E") == 0) {
		fprintf(stdout, "ATDT%s\r", phone);
		fflush(stdout);
		sprintf(dmys, "ATDT%s\n", phone);
		fprintf(stderr, "reading phone echo\n");
		for(i=0;i<60;i++){
			fprintf(stderr,"waiting for ATDT\n");
			if (match_string(dmys))
			goto m224e_continue;
		}
		goto failed;
m224e_continue:
		sprintf(dmys, "\n\nCONNECT\n");
		timeout = 80;
		fprintf(stderr, "reading response\n");
		if (!match_string(dmys)) {
			fprintf(stderr, "response invalid\n");
			timeout = 0;
			goto failed;
		}
		fprintf(stderr, "valid response received\n");
		timeout = 0;
	}


	if (strcmp(argv[3], "HAYES") == 0) {
		fprintf(stdout, "ATDT%s\r", phone);
		fflush(stdout);
		sprintf(dmys, "ATDT%s\n", phone);
		fprintf(stderr, "reading phone echo\n");
		if (!match_string(dmys))
			goto failed;
		fprintf(stderr, "reading response\n");
hayes_loop:
		if (in_line(dmys, 80) < 0)
			goto failed;
		if(strcmp(dmys,"")==0)
			goto hayes_loop;
		if(strcmp(dmys,"RING RESPONSE")==0)
			goto hayes_loop;
		if(strcmp(dmys,"CONNECT")!=0){
			fprintf(stderr, "response invalid\n");
			goto failed;
		}
		fprintf(stderr, "valid response received\n");
	}

	fflush(stdout);
	strcpy(command, "stty 526:1805:da9:3b:7f:1c:8:18:4:0:0:0 <");
	strcpy(command, "stty 526:1805:9a9:3b:7f:1c:8:18:4:0:0:0 <");
	strcat(command, argv[1]);
	strcat(command, " >/dev/ttyb 2>/dev/ttyb");
	system(command);
	fprintf(stderr, "starting ct\n");
	strcpy(command, "ct -w 12 -s 1200 1111111 <<xxx  >/dev/ttyb  2>/dev/ttyb\n");
	strcat(command, "y\n");
	strcat(command, "xxx\n");
	ier = system(command);
	fprintf(stderr, "ct returned %d\n", ier);
	return(main_err);
}



set()
{

	if (ioctl(fitty, TCGETA, &was_t)) {
		fprintf(stderr, "knockknock: Error on call to ioctl\n");
		perror("knockknock");
		exit(1);
	}
	signal_save[SIGINT] = signal(SIGINT, SIG_IGN);
	signal_save[SIGQUIT] = signal(SIGQUIT, SIG_IGN);
	bytecpy(&is_t, &was_t, sizeof was_t);
	is_t.c_lflag = is_t.c_lflag & ~ICANON ;
	is_t.c_lflag = is_t.c_lflag & ~ECHO ;
	is_t.c_cc[4] = 1;
	if (ioctl(fitty, TCSETA, &is_t)) {
		fprintf(stderr, "knockknock: Error on call to ioctl\n");
		perror("knockknock");
		exit(1);
	}
}


reset()
{
	printf("%s", at_aux_off);
	if (ioctl(fitty, TCSETA, &was_t)) {
		fprintf(stderr, "knockknock: Error on call to ioctl\n");
		perror("knockknock");
		exit(1);
	}
	signal(SIGINT, signal_save[SIGINT]);
	signal(SIGQUIT, signal_save[SIGQUIT]);
}


lwait()
{
	long	now_time;
xwait:
	if (timeout != 0) {
		now_time = time((long *) 0);
		if (now_time - start_time > timeout)
			return;
	}
	if (read(fitty, &c, 1) != 1) 
		goto xwait;
	fprintf(stderr, "%c", c);
}


int	in_line(answer, max)
char	*answer;
int	max;
/* note this is not limit on getc just of answer size */
/* char answer[max+1]; */
{
	int	i;
	static int	len;
	start_time = time((long *) 0);

	for (i = 0; i <= max; i++)
		answer[i] = 0;
	len = 0;
	lwait();
	while (c != '\n' && c != '\r' && !feof(stdin)) {
		if (c == '\b') {
			len--;
			if (len < 0)
				len = 0;
		} else {
			if (len < max)
				answer[len++] = c;
		}
		lwait();
	}
	return(len);
}


int	match_string(answer)
char	*answer;
{
	int	i, len;
	start_time = time((long *) 0);
	len = strlen(answer);
	i = 0;
loop:
	if (i >= len)
		return(1);
	lwait();
	if (c != answer[i])
		return(0);
	i++;
	goto loop;
}


