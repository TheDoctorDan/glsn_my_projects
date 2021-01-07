.SUFFIXES: .c .o
HOME=/usr/gleeson
GROUP=gleeson
MODE=711

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



comm: prom_recv prom_send dg dg00 dg_dsp ga dgsend knockknock


#prom_recv--------------------------------------------------------------------------
prom_recv: $(HOME)/bin/prom_recv

$(HOME)/bin/prom_recv:  \
	prom_recv.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	prom_recv.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


prom_recv.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/stdio.h /usr/include/ctype.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h /usr/include/string.h   \
 \
	$O/fgetline.h $O/atxy.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




#prom_send--------------------------------------------------------------------------
prom_send: $(HOME)/bin/prom_send

$(HOME)/bin/prom_send:  \
	prom_send.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	prom_send.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


prom_send.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/stdio.h /usr/include/ctype.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h /usr/include/string.h   \
 \
	$O/fgetline.h $O/atxy.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




#dg--------------------------------------------------------------------------
dg: $(HOME)/bin/dg

$(HOME)/bin/dg:  \
	dg.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	dg.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


dg.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/stdio.h /usr/include/ctype.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h /usr/include/string.h   \
 \
	$O/fgetline.h $O/atxy.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




#dg00--------------------------------------------------------------------------
dg00: $(HOME)/bin/dg00

$(HOME)/bin/dg00:  \
	dg00.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	dg00.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


dg00.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/stdio.h /usr/include/ctype.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h /usr/include/string.h   \
 \
	$O/fgetline.h $O/atxy.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




#dg_dsp--------------------------------------------------------------------------
dg_dsp: $(HOME)/bin/dg_dsp

$(HOME)/bin/dg_dsp:  \
	dg_dsp.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	dg_dsp.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


dg_dsp.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/stdio.h /usr/include/ctype.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h /usr/include/string.h   \
 \
	$O/fgetline.h $O/atxy.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




#ga--------------------------------------------------------------------------
ga: $(HOME)/bin/ga

$(HOME)/bin/ga:  \
	ga.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	ga.o \
	$O/fgetline.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


ga.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/stdio.h /usr/include/ctype.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h /usr/include/string.h   \
 \
	$O/fgetline.h $O/atxy.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




#dgsend--------------------------------------------------------------------------
dgsend: $(HOME)/bin/dgsend

$(HOME)/bin/dgsend:  \
	dgsend.o \
	$O/atxy.o $O/bytecpy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	dgsend.o \
	$O/atxy.o $O/bytecpy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


dgsend.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/ctype.h /usr/include/stdio.h /usr/include/string.h /usr/include/termio.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h /usr/include/sys/termio.h   \
 \
	$O/atxy.h $O/bytecpy.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




#knockknock--------------------------------------------------------------------------
knockknock: $(HOME)/bin/knockknock

$(HOME)/bin/knockknock:  \
	knockknock.o \
	$O/atxy.o $O/bytecpy.o $O/fgetline.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	$(GOL)/libwin.a $(CC_MALLOC_LIB) $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	knockknock.o \
	$O/atxy.o $O/bytecpy.o $O/fgetline.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/la2gdate.o $O/lftfil.o $O/lg2adate.o $O/name_case.o $O/ritfil.o $O/sdsx001.o $O/str_index.o $O/tty_set.o \
	$O/initdb.o \
	-lwin $(LD_MALLOC_FLAG) -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


knockknock.o  : \
	$(GOI)/win.h /usr/include/malloc.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h /usr/include/ctype.h /usr/include/stdio.h /usr/include/string.h /usr/include/termio.h $(GOI)/isam.h /usr/include/math.h /usr/include/memory.h /usr/include/sys/termio.h   \
 \
	$O/atxy.h $O/bytecpy.h $O/fgetline.h $O/bitops.h $O/cladate_rec.h $O/dll.h $O/dopen.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lg2adate.h $O/lpkeys.h $O/name_case.h $O/ritfil.h $O/sdsx001.h $O/str_index.h $O/tty_set.h $O/uni_all.h




