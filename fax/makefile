.SUFFIXES: .c .o
HOME=/usr/gleeson
MODE=4711
GROUP=gleeson
LPGROUP=lp_admin

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


fax_util: rle_dec 


#rle_dec--------------------------------------------------------------------------
rle_dec: $(HOME)/bin/rle_dec

$(HOME)/bin/rle_dec:  \
	rle_dec.o \
	$O/bitops.o $O/atxy.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	rle_dec.o \
	$O/bitops.o $O/atxy.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


rle_dec.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/memory.h rle_tables.h /usr/include/ctype.h $(GOI)/isam.h /usr/include/math.h /usr/include/stdio.h /usr/include/string.h   \
 \
	$O/bitops.h $O/atxy.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




