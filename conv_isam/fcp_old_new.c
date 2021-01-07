/* fcp_old_new.c */
/* 
	copy an isam file to a isam file record by record  bottom up
 */
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
#include <isam.h>
#include <old_isam.h>
#include "message.h"
#include "atxy.h"
#include "ritfil.h"

int	main_err;


main(argc, argv)
char	*argv[];
int	argc;
{
	int	fsrc, fdest, rl, kl;
	char	key[200], record[2000], dmys[20];

	main_err = 0;
	initat();
	if (argc != 3) {
		fprintf(stderr, "\nfcp_old_new: usage  fcp_old_new source destination\n");
		main_err++;
		goto l920;
	} else {
		fsrc = open(argv[1], 0);
		if(fsrc<0){
			fprintf(stderr,"fcp_old_new: can't open source file :%s\n",argv[1]);
			perror("fcp_old_new");
			main_err++;
			goto l920;
		}
		kl = old_isam_fkl(fsrc);
		rl = old_isam_frl(fsrc);
		printf("key length=%d\n", kl);
		printf("record length=%d\n", rl);
		fflush(stdout);
		if (kl > 199) {
			fprintf(stderr, "\nfcp_old_new: key length too long for program\n");
			main_err++;
			goto l910;
		}
		if (rl > 1999) {
			fprintf(stderr, "\nfcp_old_new: record length too long for program\n");
			main_err++;
			goto l910;
		}
		if (isam_create(argv[2], kl, rl)) {
			main_err++;
			goto l910;
		}
		fdest = open(argv[2], 2);
		if(fdest<0){
			fprintf(stderr,"fcp_old_new: can't open destination file :%s\n",argv[2]);
			perror("fcp_old_new");
			main_err++;
			goto l910;
		}
		*key=0;
		ritfil(key,kl,'\177');
		while (old_isam_next(fsrc, key) > 0l) {
			if (read(fsrc, record, (unsigned) rl) == -1) {
				message(1, "error on read of record", dmys);
				goto l900;
			}
			if (isam_insert(fdest, key) <= 0l) 
				goto l800;
			if (write(fdest, record, (unsigned) rl) == -1) 
				goto l810;
		}
		goto l900;
l800:
		fprintf(stderr, "fcp_old_new: error on write of key\n");
		main_err++;
		goto l900;
l810:
		fprintf(stderr, "fcp_old_new: error on write of record\n");
		main_err++;
	}
l900:
		close(fsrc);
l910:
		close(fdest);
l920:
		return(main_err);
}
