


.SUFFIXES: .class .java

.java.class:
	javac $<


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
	Nav_Bar_Item.class Nav_Bar_List.class Nav_Base.class Nav_Tab_Item.class Nav_Tab_List.class



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


