BIN=/usr/gleeson/bin/
I1=/usr/gleeson/our_subs/src/others
I2=/usr/gleeson/src/include
O=$(I1)/
I=$(I2)/
STD=$O/tty_set.o $Odopen.o $Oatxy.o $Oinitdb.o /usr/lib/libwin.a /usr/lib/libisam.a $Oecho.o
SLIB=$O/tty_set.o $Odopen.o $Oatxy.o $Oinitdb.o $Oecho.o -lwin -lisam -llockf -ltermcap -lm
ISAM=/usr/gleeson/our_subs/include/isam.h
CFLAGS= -O -I$(I1) -I$(I2)
CFLG= -O
GROUP=gleeson
MODE=700

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




isam: fcp_old_new ftw_old_new




#fcp_old_new--------------------------------------------------------------------
fcp_old_new: $(BIN)fcp_old_new


$(BIN)fcp_old_new: $(STD) \
	/usr/lib/liboisam.a \
	fcp_old_new.o \
	$Oritfil.o $Omessage.o \
	$Obytecpy.o $Ofgetline.o
	$(CC) $(CFLG) -s fcp_old_new*.o \
	$Oritfil.o $Omessage.o \
	$Obytecpy.o $Ofgetline.o \
	-loisam \
	$(SLIB) \
	-o $@
	chmod 711 $@


fcp_old_new.o: $(ISAM) $Oatxy.h \
	/usr/include/old_isam.h \
	$Oritfil.h $Omessage.h


#ftw_old_new--------------------------------------------------------------------
ftw_old_new: $(BIN)ftw_old_new

$(BIN)ftw_old_new: $(STD) \
	ftw_old_new.o \
	$Omessage.o \
	/usr/lib/liboisam.a \
	$Obytecpy.o $Ofgetline.o
	$(CC) $(CFLG) -s ftw_old_new*.o \
	$Omessage.o \
	$Obytecpy.o $Ofgetline.o \
	-loisam \
	$(SLIB) \
	-o $@
	chmod 4711 $@


ftw_old_new.o \
	: \
	$(ISAM) $Oatxy.h \
	$Omessage.h \
	/usr/include/old_isam.h \
	/usr/gleeson/our_subs/old_src/old_isam/idefs


#-------------------------------------------------------------------------------
