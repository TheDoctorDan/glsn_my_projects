/* ftw_old_new.h */
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
#include "atxy.h"
#include <isam.h>
#include "lftfil.h"
#include "fdsx001.h"
#include "message.h"
#include "/usr/gleeson/our_subs/old_src/old_isam/idefs"

FILE *tty;
int	main_err;


/* ftw_old_new.c */


extern void exit(), ftw_old_new1();

main(argc, argv)
char	*argv[];
int	argc;
{
	int fsrc;

	main_err = 0;
	if (argc != 3) {
		fprintf(stderr,"usage : ftw_old_new old_isam_filename, isam_filensame\n");
		exit(1);
	} else{
		fsrc = open(argv[1], 0);
		if (fsrc < 0) {
			fprintf(stderr, "ftw_old_new:can't open file: %s\n", argv[1]);
			perror("ftw_old_new");
			exit(1);
		} else {
			ftw_old_new1(fsrc,argv[1],argv[2]);
		}
		close(fsrc);
	}
	return(main_err);
}


/* ftw_old_new1.c */


extern void ftw_old_new2();

void ftw_old_new1(fsrc, name,new_name)
int	fsrc;
char	*name;
char	*new_name;
{
	int	record_count;
	int	fdest;

	printf("File name: %s\n", name);
	if (lseek(fsrc, KEY_LEN_DIS, 0) == -1l) 
		goto seekerror;
	if (read(fsrc, (char *) & old_isam_key_len, sizeof old_isam_key_len) == -1) 
		goto lread_err;
	old_isam_node_size = sizeof(old_isam_node) + old_isam_key_len;
	old_isam_node_size +=  old_isam_node_size % 2;
	old_isam_block_size = old_isam_node_size * NUMKEYS ;
	if (lseek(fsrc, REC_SIZE_DIS, 0) == -1l) 
		goto seekerror;
	if (read(fsrc, (char *) & old_isam_record_size, sizeof old_isam_record_size) == -1) 
		goto lread_err;
	if (lseek(fsrc, DIS_BEG_OF_TREE, 0) == -1l) 
		goto seekerror;
	if (read(fsrc, (char *) & old_isam_begining_of_tree, sizeof old_isam_begining_of_tree) == -1) 
		goto lread_err;
	if (lseek(fsrc, DIS_REC_COUNT, 0) == -1l) 
		goto seekerror;
	if (read(fsrc, (char *) &record_count, sizeof record_count) == -1) 
		goto lread_err;

	printf("Key size: %d\n", old_isam_key_len);
	printf("Record size: %d\n", old_isam_record_size);
	printf("Begining of Tree at : %ld\n", old_isam_begining_of_tree);
	printf("Records allocated: %d\n", record_count);
	fflush(stdout);
	if (old_isam_record_size > 2999) {
		fprintf(stderr, "\nftw_old_new: record length too long for program\n");
		exit(1);
	}

	if (isam_create(new_name, old_isam_key_len, old_isam_record_size)) {
		exit(1);
	}
	fdest = open(new_name, 2);
	if(fdest<0){
		fprintf(stderr,"ftw_old_new: can't open destination file :%s\n",new_name);
		perror("ftw_old_new");
		exit(1);
	}



	if (old_isam_begining_of_tree) 
		ftw_old_new2(fsrc, fdest, old_isam_begining_of_tree, old_isam_record_size);
	close(fdest);
	goto l900;

seekerror:
	fprintf(stderr, "ftw_old_new1:seek error.\n");
	perror("ftw_old_new1");
	main_err++;
	goto l900;
lread_err:
	fprintf(stderr, "ftw_old_new1:read error.\n");
	perror("ftw_old_new1");
	main_err++;
l900:
	;
}


/* ftw_old_new2.c */


extern old_isam_node *old_isam_alloc();

void ftw_old_new2(fsrc, fdest, blk_pos, rl)
int	fsrc, fdest, rl;
long	blk_pos;
{
	old_isam_node	*work, *block[NUMKEYS];
	int	i;
	char	record[3000], dmys[20];

	block[0] = old_isam_alloc(old_isam_block_size);
	if (block[0] == NULL)
		exit(2);
	if (lseek(fsrc, blk_pos, 0) == -1l) 
		goto seekerror;
	if (read(fsrc, block[0], (unsigned) old_isam_block_size) == -1) 
		goto lread_err;
	for(i=0; i < 10; i++){ 
		work = (old_isam_node * )((long) block[0] + old_isam_node_size * i);
		if (work->link == 0L) {
			;
		} else {
			if (work->data != '1') 
				ftw_old_new2(fsrc, fdest, work->link, rl);
			else{
				if (lseek(fsrc, work->link, 0) == -1l) 
					goto seekerror;
				if (read(fsrc, record, (unsigned) rl) == -1) {
					message(1, "error on read of record", dmys);
					exit(1);
				}
				if (isam_insert(fdest, work->key) <= 0l) {
					message(1, "error on insert of key", dmys);
					exit(1);
				}
				if (write(fdest, record, (unsigned) rl) == -1) {
					message(1, "error on write of record", dmys);
					exit(1);
				}
			}
		}
	}		/*  end of loop */
	free(block[0]);
	goto l900;

seekerror:
	fprintf(stderr, "ftw_old_new2:seek error.\n");
	perror("ftw_old_new2");
	main_err++;
	goto l900;
lread_err:
	fprintf(stderr, "ftw_old_new2:read error.\n");
	perror("ftw_old_new2");
	main_err++;
l900:
	;
}

