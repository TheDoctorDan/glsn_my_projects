/* addr_dup.c */

/* Check for duplicate addresses */
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

#include <sys/types.h>
#include <stdio.h>
#include "atxy.h"
#include <string.h>
#include <ctype.h>
#include <isam.h>
#include "message.h"
#include "str_index.h"

extern void     perror(), exit();
extern char	*mktemp();

int	main_err;
char	*filename;

#define WORK_KEY_LEN 100
int	fwork;
char	work[WORK_KEY_LEN +1];
typedef struct{
	char	our_record_index[40], first_name[40],
		middle_init[40], last_name[80], title[40], address1[40],
		address2[40], city[40], state[40], zip[40], township[40],
		precinct[40], other[40];
}t_work_rec;
t_work_rec workx_rec, work_rec;



main(argc, argv)
char	*argv[];
int	argc;
{
	char	work_name[100];
	FILE * fp;
	main_err = 0;
	initat();

	strcat(work_name, "/tmp/");
	strcat(work_name, "Wpg000XXXXXX");
	mktemp(work_name);
	if (isam_create(work_name, WORK_KEY_LEN, sizeof work_rec))
		exit(1);
	fwork = open(work_name, 2);
	if (fwork < 0) {
		fprintf(stderr, "pg000: Can't open file: %s\n", work_name);
		perror("pg000");
		exit(1);
	}



	if (argc == 1){
		fprintf(stderr,"needs arguments\n");
		main_err++;
	} else
		while (--argc > 0)
			if ((fp = fopen(*++argv, "r")) == NULL) {
				fprintf(stderr, "addr_dup: can't open %s\n", *argv);
				perror("addr_dup");
				exit(1);
			}
			else {
				filename = *argv;
				addr_dup1(fp);
				fclose(fp);
			}
	close(fwork);
	unlink(work_name);
	return(main_err);
}


addr_dup1(stream)
FILE *stream;
{
	char	dmys[20];
	char	line[301];
	int	j, offset;

	fgets(line, 300,stream);
	while (!feof(stream)) {
		offset=0;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.our_record_index,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.first_name,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.middle_init,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.last_name,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.title,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.address1,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.address2,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.city,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.state,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.zip,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.township,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.precinct,&line[offset]);
		offset+=j+1;

		j=str_index(&line[offset],"\t");
		line[offset+j]=0;
		strcpy(work_rec.other,&line[offset]);
		offset+=j+1;




		strcpy(work,work_rec.address1);
		strcat(work,work_rec.address2);
		if(isam_find_read(fwork,work,&workx_rec) >0l){
			printf("%-50.50s  %s\n",workx_rec.last_name, work_rec.last_name);
			printf("%-50.50s  %s\n",workx_rec.address1, work_rec.address1);
			printf("%-50.50s  %s\n",workx_rec.address2, work_rec.address2);
			printf("\n");
		}else{
			if(isam_insert_write(fwork,work,&work_rec) <=0l){
				message(1,"error on write to work file",dmys);
				exit(1);
			}
		}
		fgets(line, 300,stream);
	}
}
