/* try.c */
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



#define c6mdy_8mdy(d,s)	sprintf(d,"%2.2s%2.2s%2.2s",	s,s+3,s+6)
#define c5md_8mdy(d,s)	sprintf(d,"%2.2s/%2.2s",	s,s+3)
#define c5my_8mdy(d,s)	sprintf(d,"%2.2s/%2.2s",	s,s+6)
#define c4md_8mdy(d,s)	sprintf(d,"%2.2s%2.2s",		s,s+3)
#define c4my_8mdy(d,s)	sprintf(d,"%2.2s%2.2s",		s,s+6)
#define c6ymd_8mdy(d,s)	sprintf(d,"%2.2s%2.2s%2.2s",	s+6,s,s+3)
#define c5ym_8mdy(d,s)	sprintf(d,"%2.2s/%2.2s",	s+6,s)
#define c4ym_8mdy(d,s)	sprintf(d,"%2.2s%2.2s",		s+6,s)

#define c8mdy_6ymd(d,s)	sprintf(d,"%2.2s/%2.2s/%2.2s",	s+2,s+4,s)
#define c6mdy_6ymd(d,s)	sprintf(d,"%2.2s%2.2s%2.2s",	s+2,s+4,s)
#define c5md_6ymd(d,s)	sprintf(d,"%2.2s/%2.2s",	s+2,s+4)
#define c5my_6ymd(d,s)	sprintf(d,"%2.2s/%2.2s",	s+2,s)
#define c4md_6ymd(d,s)	sprintf(d,"%2.2s%2.2s",		s+2,s+4)
#define c4my_6ymd(d,s)	sprintf(d,"%2.2s%2.2s",		s+2,s)
#define c5ym_6ymd(d,s)	sprintf(d,"%2.2s/%2.2s",	s,s+2)
#define c4ym_6ymd(d,s)	sprintf(d,"%2.2s%2.2s",		s,s+2)

#define c8mdy_6mdy(d,s)	sprintf(d,"%2.2s/%2.2s/%2.2s",	s,s+2,s+4)
#define c5md_6mdy(d,s)	sprintf(d,"%2.2s/%2.2s",	s,s+2)
#define c5my_6mdy(d,s)	sprintf(d,"%2.2s/%2.2s",	s,s+4)
#define c4md_6mdy(d,s)	sprintf(d,"%2.2s%2.2s",		s,s+2)
#define c4my_6mdy(d,s)	sprintf(d,"%2.2s%2.2s",		s,s+4)
#define c6ymd_6mdy(d,s)	sprintf(d,"%2.2s%2.2s%2.2s",	s+4,s,s+2)
#define c5ym_6mdy(d,s)	sprintf(d,"%2.2s/%2.2s",	s+4,s)
#define c4ym_6mdy(d,s)	sprintf(d,"%2.2s%2.2s",		s+4,s)

#define c5my_5ym(d,s)	sprintf(d,"%2.2/s%2.2s",	s+3,s)
#define c4my_5ym(d,s)	sprintf(d,"%2.2s/%2.2s",	s+3,s)
#define c4ym_5ym(d,s)	sprintf(d,"%2.2s%2.2s",		s,s+3)

#define c4my_5my(d,s)	sprintf(d,"%2.2s%2.2s",		s,s+3)
#define c5ym_5my(d,s)	sprintf(d,"%2.2s/%2.2s",	s+3,s)
#define c4ym_5my(d,s)	sprintf(d,"%2.2s%2.2s",		s+3,s)

#define c5my_4my(d,s)	sprintf(d,"%2.2s/%2.2s",	s,s+2)
#define c5ym_4my(d,s)	sprintf(d,"%2.2s/%2.2s",	s+2,s)
#define c4ym_4my(d,s)	sprintf(d,"%2.2s%2.2s",		s+2,s)

#define c5my_4ym(d,s)	sprintf(d,"%2.2s/%2.2s",	s+2,s)
#define c4my_4ym(d,s)	sprintf(d,"%2.2s%2.2s",		s+2,s)
#define c5ym_4ym(d,s)	sprintf(d,"%2.2s/%2.2s",	s,s+2)


main()
{
	char	date8[9];
	char	date6[7];
	strcpy(date8,"mm/dd/yy");
	c6ymd_8mdy(date6,date8);
	printf("     date6:%s\n",date6);
	printf("should be :yymmdd\n");
}
