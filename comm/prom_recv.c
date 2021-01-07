/* prom_recv.c */
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
		printf("output must not be stdout\n");
		exit(1);
	} else
		while (--argc > 0)
			if ((fp = fopen(*++argv, "w")) == NULL) {
				fprintf(stderr, "prom_recv: can't open %s\n", *argv);
				perror("prom_recv");
				exit(1);
			}
			else {
				printf("\noutput file :%s\n", *argv);
				printf("-----------==============\n");
				prom_recv1(fp);
				fclose(fp);
			}

	return(main_err);
}


prom_recv1(stream)
FILE *stream;
{
	FILE * ttyw;
	char	c, inf[501], *cptr;
	ttyw = fopen("/dev/prom_prog", "r+");
	if (ttyw == NULL) {
		fprintf(stderr, "prom_recv: open of /dev/prom_prog failed\n");
		perror("prom_recv");
		exit(1);
	}
	system("stty 1400:1804:8bd:0:7f:1c:8:18:1:1:0:0 </dev/prom_prog");
	system("stty 4800 </dev/prom_prog");
	fprintf(ttyw, "U");
	fflush(ttyw);
	cptr=inf;
	c = getc(ttyw);
	while (!feof(ttyw)) {
		if(c!='\n'){
			putc(c, stream);
			if(c=='\r'){
				putc('\n', stream);
				if(strcmp(inf,"S9030000FC")==0)
					goto lend;
				cptr=inf;
			}else{
				*cptr++=c;
				*cptr=0;
			}
		}

		c = getc(ttyw);
	}
lend:
	fclose(ttyw);
}


