


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
	Attribute_Validation_Exception.class Data_Base_Delete_Exception.class Data_Base_Lock_Read_Exception.class \
	Data_Base_Read_Exception.class Data_Base_Write_Exception.class Delete_Not_Allowed_Exception.class \
	Parameter_Validation_Exception.class Stack_Validation_Exception.class



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


