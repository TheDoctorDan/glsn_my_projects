/* name_case.c */

/* change to upper lower case , and tab fields */
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
#include <ctype.h>
#include "fgetline.h"
#include "justify.h"

extern void exit(), perror();

int	main_err;
char	*filename;

main(argc, argv)
char	*argv[];
int	argc;
{
	FILE * fp;
	main_err = 0;

	if (argc == 1){
		fprintf(stderr,"needs arguments\n");
		main_err++;
	} else
		while (--argc > 0)
			if ((fp = fopen(*++argv, "r")) == NULL) {
				fprintf(stderr, "name_case: can't open %s\n", *argv);
				perror("name_case");
				exit(1);
			}
			else {
				filename = *argv;
				name_case1(fp);
				fclose(fp);
			}
	return(main_err);
}


name_case1(stream)
FILE *stream;
{
	char	line[301], our_record_index[40], first_name[40],
		middle_init[40], last_name[80], title[40], address1[40],
		address2[40], city[40], state[40], zip[40], township[40],
		precinct[40], other[40];
	int	j;

	j = fgetline(stream, line, 300);
	while (!feof(stream)) {
		sprintf(our_record_index, "%-6.6s", &line[0]);
		sprintf(first_name, "%-15.15s", &line[6]);
		sprintf(middle_init, "%-1.1s", &line[21]);
		sprintf(last_name, "%-25.25s", &line[22]);
		sprintf(title, "%-4.4s", &line[47]);
		sprintf(address1, "%-35.35s", &line[51]);
		sprintf(address2, "%-30.30s", &line[86]);
		sprintf(city, "%-21.21s", &line[116]);
		sprintf(state, "%-2.2s", &line[137]);
		sprintf(zip, "%-10.10s", &line[139]);
		sprintf(township, "%-2.2s", &line[149]);
		sprintf(precinct, "%-3.3s", &line[151]);
		sprintf(other, "%-25.25s", &line[154]);

		justify(our_record_index);
		justify(first_name);
		justify(middle_init);
		justify(last_name);
		justify(title);
		justify(address1);
		justify(address2);
		justify(city);
		justify(state);
		justify(zip);
		justify(township);
		justify(precinct);
		justify(other);

		name_proc(first_name);
		last_name_proc(last_name);
		name_proc(title);
		name_proc(address1);
		name_proc(address2);
		name_proc(city);

		printf("%s\t", our_record_index);
		printf("%s\t", first_name);
		printf("%s\t", middle_init);
		printf("%s\t", last_name);
		printf("%s\t", title);
		printf("%s\t", address1);
		printf("%s\t", address2);
		printf("%s\t", city);
		printf("%s\t", state);
		printf("%s\t", zip);
		printf("%s\t", township);
		printf("%s\t", precinct);
		printf("%s\t", other);
		printf("\t\t\t\t\t\t\t\t\t\t\t\t\n");

		j = fgetline(stream, line, 300);
	}
}

name_proc(s)
char	*s;
{
	char	*ip, *cp, result[301], source[301];
	int	first;
	*result=0;
	strcpy(source,s);
	cp=strtok(source," .");
	while(cp != NULL){
		ip=cp;
		first=1;
		while(*ip){
			if(first){
				first=0;
				if(islower(*ip))
					*ip -= 'a'-'A';
			}else{
				if(isupper(*ip))
					*ip += 'a'-'A';
			}
			if(*ip=='\'')
				first=1;
			if(*ip=='-')
				first=1;
			if(*ip=='.')
				first=1;
			ip++;
		}
		strcat(result,cp);
		cp=strtok(NULL," .");
		if(cp != NULL)
			strcat(result," ");
	}
	strcpy(s,result);
}


last_name_proc(s)
char	*s;
{
	char	*ip, *cp, result[301], source[301];
	int	first;
	strcpy(result,"The ");
	strcpy(source,s);
	cp=strtok(source," .");
	while(cp != NULL){
		ip=cp;
		first=1;
		while(*ip){
			if(first){
				first=0;
				if(islower(*ip))
					*ip -= 'a'-'A';
			}else{
				if(isupper(*ip))
					*ip += 'a'-'A';
			}
			if(*ip=='\'')
				first=1;
			if(*ip=='-')
				first=1;
			if(*ip=='.')
				first=1;
			ip++;
		}
		if(strcmp(cp,"Household")==0){
			strcat(result,"Family");
		}else
			strcat(result,cp);
		cp=strtok(NULL," .");
		if(cp != NULL)
			strcat(result," ");
	}
	strcpy(s,result);
}
