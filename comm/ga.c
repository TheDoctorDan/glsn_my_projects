/* ga.c */
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
				fprintf(stderr, "ga: can't open %s\n", *argv);
				perror("ga");
				exit(1);
			}
			else {
				printf("\noutput to :%s\n", *argv);
				printf("-----------==============\n");
				ga1(fp);
				fclose(fp);
			}

	return(main_err);
}


ga1(stream)
FILE *stream;
{
	FILE * ttyw, *ttyr;
	char	c, inf[81];
	ttyw = fopen("/dev/tty15", "r+");
	if (ttyw == NULL) {
		fprintf(stderr, "ga: open of /dev/tty09 failed\n");
		perror("ga");
		exit(1);
	}
	printf("input command :");
	if (getline(inf, 80) < 1)
		exit(1);
	fprintf(ttyw, "%s\r", inf);
	printf("\n%s\n", inf);
	fclose(ttyw);
	ttyr = fopen("/dev/tty15", "r");
	if (ttyr == NULL) {
		fprintf(stderr, "ga: open of /dev/tty15 failed\n");
		perror("ga");
		exit(1);
	}
	c = getc(ttyr);
	while (!feof(ttyr) && c != 4) {
		putc(c, stream);
		c = getc(ttyr);
	}
	fclose(ttyr);
}


