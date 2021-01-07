From uucp Fri Feb  5 07:20 CST 1993
>From dgleeson Fri Feb  5 07:06 CST 1993 remote from glsn2

/* time_line.h */
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

#ifndef TIME_LINE_TYPEDEF
#define TIME_LINE_TYPEDEF 1

#include "keylength.h"

#define TIME_LINE_KEY_LEN 14

typedef struct {
	char	desc[31];
	struct{
		double	billable_hours, elapsed_days, elapsed_hours;
		char	target_date[9];
	}original_estimate, current_estimate;
	struct{
		unsigned int	flag:1;
		char	date[9];
		double	billable_hours;
	}completed;
	char	prev_time_line[TIME_LINE_KEY_LEN+1];
	char	next_time_line[TIME_LINE_KEY_LEN+1];
}t_time_line_rec;


#endif

