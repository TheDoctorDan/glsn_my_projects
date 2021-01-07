/* release_comparison.c */
/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 1996, 1997, 1998, 1999, 2000, 2001, 2002       |
|       Gleeson and Associates                                                 |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       Gleeson And Associates                                                 |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/

#include <stdio.h>
#include <strings.h>

extern char *strtok(char *s, const char *delim);

#define NUM_ELEMENTS 30

int main(int argc, char **argv)
{
	char	first_arg[NUM_ELEMENTS][31];
	int	first_arg_count;

	char	second_arg[NUM_ELEMENTS][31];
	int	second_arg_count;

	char	*ptr;
	char	*s;
	int	i;
	int	minimum_count;


	if(argc !=3){
		fprintf(stderr,"release_comparison : Needs two arguments\n");
		exit(1);
	}

	s=argv[1];
	first_arg_count=0;
	ptr=strtok(s,"-.,");
	while(ptr != NULL){
		sprintf(first_arg[first_arg_count],"%30.30s", ptr);
		first_arg_count++;
		ptr=strtok(NULL,"-.,");
	}


	s=argv[2];
	second_arg_count=0;
	ptr=strtok(s,"-.,");
	while(ptr != NULL){
		sprintf(second_arg[second_arg_count],"%30.30s", ptr);
		second_arg_count++;
		ptr=strtok(NULL,"-.,");
	}

	minimum_count =first_arg_count < second_arg_count?first_arg_count : second_arg_count;


	for(i=0;i<minimum_count;i++){
		if(strcmp(first_arg[i], second_arg[i])!=0){
			if(strcmp(first_arg[i], second_arg[i])<0){
				printf("less\n");
				return(0);
			}else{
				printf("more\n");
				return(0);
			}
		}
	}
	if(first_arg_count < second_arg_count){
		printf("less\n");
	}else if(first_arg_count > second_arg_count){
		printf("more\n");
	}else
		printf("equal\n");



	return(0);
}
