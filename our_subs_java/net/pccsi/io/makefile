


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

all: \
	DataPath.class Data_Field_List.class Data_Field_Machine_Type.class Data_Field_Object.class Data_Field_Type.class Index_Data_File.class Isam_Data_Record.class JIsam_Data_Field.class Keys_And_Data_File.class Keys_Only_File.class No_Data_Base_Chosen_Exception.class \
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



