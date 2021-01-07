.SUFFIXES: .c .o
HOME=/usr/gleeson
MODE=711
GROUP=gleeson

.c.o:
	$(CC) -c $< $(CFLAGS)


#------------------------------------------------------------------------------|
#	Copyright (c) 1985 thru 1999, 2000, 2001, 2002, 2003, 2004, 2005       |
#	Gleeson and Associates                                                 |
#	All Rights Reserved                                                    |
#                                                                              |
#	THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
#	Gleeson And Associates                                                 |
#	The copyright notice above does not evidence any                       |
#	actual or intended publication of such source code.                    |
#------------------------------------------------------------------------------|

all: adjust_lilo_conf release_comparison

include adjust_lilo_conf.M

include release_comparison.M
