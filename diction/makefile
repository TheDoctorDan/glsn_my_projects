.SUFFIXES: .c .o
BIN=/usr/gleeson/bin/
MODE=711
GROUP=gleeson

# no comma for ti
DICT= -Wp,-DDICT=\"/usr/gleeson/lib/dict.d\"

.c.o:
	$(CC) -c $< $(CFLAGS)


#------------------------------------------------------------------------------|
#	Copyright (c) 1985, 1986, 1987, 1988, 1989, 1990, 1991, 1992, 1993     |
#	Gleeson and Associates                                                 |
#	All Rights Reserved                                                    |
#                                                                              |
#	THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
#	Gleeson And Associates                                                 |
#	The copyright notice above does not evidence any                       |
#	actual or intended publication of such source code.                    |
#------------------------------------------------------------------------------|




#diction.exe--------------------------------------------------------------------
diction.exe: $(BIN)diction.exe

$(BIN)diction.exe: $(STD) \
	diction.o
	$(CC) $(CFLG) \
	diction.o \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@



diction.o : diction.c
	$(CC) -c $< $(CFLAGS) $(DICT)



