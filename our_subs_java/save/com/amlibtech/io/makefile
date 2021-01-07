
.SUFFIXES: .class .java .c .o


.java.class:
	javac $<


.c.o:
	$(CC) -c $< $(CFLAGS) -I/usr/gleeson/dgleeson/j2sdk_nb/j2sdk1.4.2/include -I/usr/gleeson/dgleeson/j2sdk_nb/j2sdk1.4.2/include/linux



#------------------------------------------------------------------------------|
#       Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004       |
#       American Liberator Technologies                                        |
#       All Rights Reserved                                                    |
#                                                                              |
#       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
#       American Liberator Technologies                                        |
#       The copyright notice above does not evidence any                       |
#       actual or intended publication of such source code.                    |
#------------------------------------------------------------------------------|


all : \
	JIsam.class \
	subdirs

subdirs :
	{ \
	echo "for i in *"; \
	echo "do"; \
	echo "	if [ -d $$""i ]"; \
	echo "	then"; \
	echo "		cd $$""i"; \
	echo "		make"; \
	echo "		cd .."; \
	echo "	fi"; \
	echo "done"; \
	} | sh


