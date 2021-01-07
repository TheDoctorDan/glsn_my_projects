.SUFFIXES: .c .o
HOME=/usr/gleeson
MODE=711
GROUP=gleeson

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





paul: name_case addr_dup




#addr_dup--------------------------------------------------------------------------
addr_dup: $(HOME)/bin/addr_dup

$(HOME)/bin/addr_dup:  \
	addr_dup.o \
	$O/atxy.o $O/message.o $O/str_index.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/ritfil.o $O/sdsx001.o $O/tty_set.o $O/justify.o \
	$O/fgetline.o $O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	addr_dup.o \
	$O/atxy.o $O/message.o $O/str_index.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/ritfil.o $O/sdsx001.o $O/tty_set.o $O/justify.o \
	$O/fgetline.o $O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	-o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


addr_dup.o : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/ctype.h $(GOI)/isam.h /usr/include/stdio.h /usr/include/string.h /usr/include/math.h /usr/include/memory.h   \
 \
	$O/atxy.h $O/message.h $O/str_index.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/tty_set.h $O/uni_all.h $O/justify.h




#name_case--------------------------------------------------------------------------
name_case: $(HOME)/bin/name_case

$(HOME)/bin/name_case:  \
	name_case.o \
	$O/fgetline.o $O/justify.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	name_case.o \
	$O/fgetline.o $O/justify.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	-o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


name_case.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/ctype.h /usr/include/stdio.h /usr/include/string.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h   \
 \
	$O/name_case.h $O/fgetline.h $O/justify.h $O/atxy.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




