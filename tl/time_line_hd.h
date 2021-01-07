/* time_line_hd.h */
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

#ifndef TIME_LINE_HD_TYPEDEF
#define TIME_LINE_HD_TYPEDEF 1

#include "keylength.h"

#define TIME_LINE_HD_SEQ_LEN 3
#define TIME_LINE_HD_KEY_LEN GL_CLIENT_KEY + KEY_DATE_LEN + TIME_LINE_HD_SEQ_LEN 

typedef struct {
	char	desc[31];
	char	first_time_line_pg[TIME_LINE_PG_KEY_LEN+1];
}t_time_line_hd_rec;

#endif

