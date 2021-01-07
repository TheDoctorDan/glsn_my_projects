

.SUFFIXES: .class .java

.java.class:
	javac $<


CLASSPATH=/usr/gleeson/our_subs/java

#------------------------------------------------------------------------------|
#       Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004       |
#       Gleeson and Associates                                                 |
#       All Rights Reserved                                                    |
#                                                                              |
#       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
#       Gleeson And Associates                                                 |
#       The copyright notice above does not evidence any                       |
#       actual or intended publication of such source code.                    |
#------------------------------------------------------------------------------|



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



