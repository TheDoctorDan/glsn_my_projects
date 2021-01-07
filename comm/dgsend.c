/* dgsend.c */
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

extern void exit(), perror();
extern int ioctl();

int	main_err;
struct termio was_t, is_t;

static t_signal (*signal_save[NSIG +1])();

FILE	*fptty;
int	ftty;
char	c, back[1025];
int	first_line, lines_per;

main(argc, argv)
char	*argv[];
int	argc;
{
	FILE	*fp;
	int	i, found;
	main_err = 0;
	fprintf(stderr,"20 seconds\n");
	sleep(20);
	fprintf(stderr,"begin\n");
	set();
	
	for(i=1 ; i<argc; i++){

	fp=fopen(argv[i],"r");
	if(fp == NULL){
		fprintf(stderr,"dgsend: Error on open of %s\n",argv[i]);
		perror("dgsend");
		main_err=1;
	} else {
		printf("\03");
		fflush(stdout);
		printf("3/TAC\r");
		lwait();
		dumpit(fp);
fprintf(stderr,"dumpit terminated \n");
		lwait();
fprintf(stderr,"lwait terminated \n");
		printf("END\r");
fprintf(stderr,"END sent \n");
lwait2();
lwait2();
		fprintf(stderr,"tac terminated for %s\n",argv[i]);
		printf("\03");
		fprintf(stderr,"^C\n");
lwait2();
lwait2();
		printf("B\r");
		fprintf(stderr,"B\n");
lwait2();
lwait2();
		printf("NEW\r");
		fprintf(stderr,"NEW\n");
lwait2();
lwait2();
		printf("LOAD 3/TEXT\r");
		fprintf(stderr,"LOAD 3/TEXT\n");
lwait2();
lwait2();
		printf("\03");
		fprintf(stderr,"^C\n");
lwait2();
lwait2();
		printf("SAVE <00> 3/%s!\r",argv[i]);
		fprintf(stderr,"SAVE <00> 3/%s!\n",argv[i]);
lwait2();
lwait2();
lwait2();
lwait2();
		fprintf(stderr,"%s sent\n",argv[i]);
		fclose(fp);
	}
	}


lend:
	reset();
	return(main_err);

usage:
	fprintf(stderr,"dgsend: usage [+num] [-num] [-r xxx] [ file [file ...] ]\n");
	main_err=2;
	goto lend;
}

set()
{
	fptty=fopen("/dev/tty","r");
	if(fptty == NULL){
		fprintf(stderr,"dgsend: Error on open of tty\n");
		perror("dgsend");
		exit(1);
	}
	ftty= fileno(fptty);
	finitat(fptty);
	if(ioctl(ftty,TCGETA, &was_t)){
		fprintf(stderr,"dgsend: Error on call to ioctl\n");
		perror("dgsend");
		exit(1);
	}
	signal_save[SIGINT] = signal(SIGINT,SIG_IGN);
	signal_save[SIGQUIT] = signal(SIGQUIT,SIG_IGN);
	bytecpy(&is_t, &was_t, sizeof was_t);
	is_t.c_lflag = is_t.c_lflag & ~ICANON ;
	is_t.c_lflag = is_t.c_lflag & ~ECHO ;
	is_t.c_cc[4]=1;
	if(ioctl(ftty,TCSETA, &is_t)){
		fprintf(stderr,"dgsend: Error on call to ioctl\n");
		perror("dgsend");
		exit(1);
	}
}

reset()
{
	if(ioctl(ftty,TCSETA, &was_t)){
		fprintf(stderr,"dgsend: Error on call to ioctl\n");
		perror("dgsend");
		exit(1);
	}
	signal(SIGINT,signal_save[SIGINT]);
	signal(SIGQUIT,signal_save[SIGQUIT]);
}

dumpit(fdes)
FILE	*fdes;
{
	char	c2;
	c2=getc(fdes);
	while (!feof(fdes)){
		if(c2=='\n'){
			c2='\r';
			printf("%c",c2);
			lwait();
		} else
			printf("%c",c2);
		c2=getc(fdes);
	}
	printf("\r");
}




lwait(){
	int	i;
	fflush(stdout);
	i=0;
xwait:
	if(read(ftty,&c,1) != 1) goto xwait;
	if(c != '\03'){
/*		back[i++]=c; */
		goto xwait;
	}
}


lwait2(){
	int	i;
	fflush(stdout);
	i=0;
xwait:
	if(read(ftty,&c,1) != 1) goto xwait;
	if(c != '\n'){
/*		back[i++]=c; */
		goto xwait;
	}
}
