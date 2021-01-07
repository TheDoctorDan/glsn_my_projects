/* prom_send.c */
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
extern unsigned sleep();

main(argc, argv)
char	*argv[];
int	argc;
{
	FILE * fp;
	int	main_err;
	main_err = 0;

	if (argc == 1) {
		prom_send1(stdin);
	} else
		while (--argc > 0)
			if ((fp = fopen(*++argv, "r")) == NULL) {
				fprintf(stderr, "prom_send: can't open %s\n", *argv);
				perror("prom_send");
				exit(1);
			}
			else {
				printf("\ninput file :%s\n", *argv);
				printf("-----------==============\n");
				prom_send1(fp);
				fclose(fp);
			}

	return(main_err);
}


prom_send1(stream)
FILE *stream;
{
	FILE * ttyw;
	char	c, inf[81];
	ttyw = fopen("/dev/prom_prog", "r+");
	if (ttyw == NULL) {
		fprintf(stderr, "prom_send: open of /dev/prom_prog failed\n");
		perror("prom_send");
		exit(1);
	}
	system("stty 1400:1804:8bd:0:7f:1c:8:18:1:1:0:0 </dev/prom_prog");
	system("stty 4800 </dev/prom_prog");
	fprintf(ttyw, "T");
	fflush(ttyw);
	sleep(3);
	c = getc(stream);
	while (!feof(stream)) {
		putc(c, ttyw);
		c = getc(stream);
	}
	fclose(ttyw);
}


