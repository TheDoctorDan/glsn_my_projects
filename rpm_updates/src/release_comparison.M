#release_comparison--------------------------------------------------------------------------
release_comparison: $(HOME)/bin/release_comparison

$(HOME)/bin/release_comparison:  \
	release_comparison.o \
	$O/Goutfile.o $O/ampm2dymin.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/dymin2ampm.o $O/dymin2mil.o $O/lG2adate.o $O/la2gdate.o $O/lftfil.o $O/mil2dymin.o $O/name_case.o $O/nround.o $O/ritfil.o $O/str_index.o $O/tty_set.o \
	$O/basename.o $O/initdb.o $O/justify.o $O/strncatt.o $O/strncpyt.o \
	$(CC_MALLOC_LIB)  \
	$(GOL)/libwin.a $(GOL)/libisam.a $(GOL)/libmisam.a 
	$(CC) $(CFLG) \
	release_comparison.o \
	$O/Goutfile.o $O/ampm2dymin.o $O/atxy.o $O/bitops.o $O/cladate_rec.o $O/dll.o $O/dopen.o $O/dymin2ampm.o $O/dymin2mil.o $O/lG2adate.o $O/la2gdate.o $O/lftfil.o $O/mil2dymin.o $O/name_case.o $O/nround.o $O/ritfil.o $O/str_index.o $O/tty_set.o \
	$O/basename.o $O/initdb.o $O/justify.o $O/strncatt.o $O/strncpyt.o \
	$(LD_MALLOC_FLAG)  \
	-lwin -lisam -lmisam  \
	-llockf -lgtc -lm  \
	$(LC_S) -o $@
	chgrp $(GROUP) $@
	chmod $(MODE) $@


release_comparison.o \
	: \
	$(GOI)/win.h /usr/gleeson/our_subs/src/others/lpdev.h /usr/gleeson/our_subs/src/others/lpprinters.h /usr/gleeson/our_subs/src/window/window_defs.h /usr/gleeson/our_subs/src/window/window_ext.h /usr/gleeson/our_subs/src/window/window_keys.h $(GOI)/isam.h \
 \
	$O/Goutfile.h $O/ampm2dymin.h $O/atxy.h $O/bitops.h $O/cladate_rec.h $O/day_minute.h $O/dll.h $O/dopen.h $O/dymin2ampm.h $O/dymin2mil.h $O/lG2adate.h $O/la2gdate.h $O/ladate.h $O/lftfil.h $O/lp_fax_info.h $O/lpkeys.h $O/mil2dymin.h $O/name_case.h $O/nround.h $O/ritfil.h $O/str_index.h $O/tty_set.h $O/uni_all.h




