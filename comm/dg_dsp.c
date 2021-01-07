/* dg_dsp.c */
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
#include "fgetline.h"

extern void exit(), perror();

main(argc, argv)
char	*argv[];
int	argc;
{
	FILE * fp;
	int	main_err;
	main_err = 0;

	if (argc == 1) {
		printf("output must not be stdout\n");
		exit(1);
	} else
		while (--argc > 0)
			if ((fp = fopen(*++argv, "w")) == NULL) {
				fprintf(stderr, "dg_dsp: can't open %s\n", *argv);
				perror("dg_dsp");
				exit(1);
			}
			else {
				printf("\noutput to :%s\n", *argv);
				printf("-----------==============\n");
				dg_dsp1(fp);
				fclose(fp);
			}

	return(main_err);
}


dg_dsp1(stream)
FILE *stream;
{
	FILE * ttyw;
	int	i,j;
	char	c, inf[81], dmys[81];
	ttyw = fopen("/dev/tty15", "r+");
	if (ttyw == NULL) {
		fprintf(stderr, "dg_dsp: open of /dev/tty15 failed\n");
		perror("dg_dsp");
		exit(1);
	}
	system("stty 1025:0:5ad:0:0:0:0:0:1:0:0:0 </dev/tty15");
	system("stty -echo </dev/tty15");
	printf("input command :");
	if (getline(inf, 80) < 1)
		exit(1);
	fprintf(ttyw, "%s", "\03");
	fflush(ttyw);
	sleep(3);
	fprintf(ttyw, "%s", "\03");
	fflush(ttyw);
	sleep(3);
	fprintf(ttyw, "DSP DLB\r");
	fflush(ttyw);
	sleep(3);
	j=0;
	for(i=0; i<strlen(inf); i++){
		if(inf[i]==';'){
			dmys[j++]=0;
			fprintf(ttyw, "%s\r", dmys);
			fflush(ttyw);
			sleep(2);
			printf("\n%s\n", dmys);
			j=0;
		} else
			dmys[j++]=inf[i];
	}
	dmys[j++]=0;
	fprintf(ttyw, "%s\r", dmys);
	fflush(ttyw);
	printf("\n%s\n", dmys);
	c = getc(ttyw);
	while (!feof(ttyw) && c != 'X') {
		putc(c, stream);
		c = getc(ttyw);
	}
	fprintf(ttyw, "%s", "\03");
	fflush(ttyw);
	fclose(ttyw);
}


