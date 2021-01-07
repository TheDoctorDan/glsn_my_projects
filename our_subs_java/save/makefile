


.SUFFIXES: .class .java

.java.class:
	javac $<

CLASSPATH=/usr/gleeson/our_subs/java

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


PCCSI_CLASSES=  net/pccsi/*.class net/pccsi/*/*.class net/pccsi/*/*/*.class

include AMLIBTECH_CLASSES.M


#jars:  subdirs pccsi_net.jar amlibtech_com.jar
jars:  subdirs includes amlibtech_com.jar


includes: 
	{ \
	echo "AMLIBTECH_CLASSES= \\" > AMLIBTECH_CLASSES.M; \
	echo `find com |grep '\.class$$'` >> AMLIBTECH_CLASSES.M; \
	}

subdirs :
	{ \
	echo "for i in com"; \
	echo "do"; \
	echo "	if [ -d $$""i ]"; \
	echo "	then"; \
	echo "		cd $$""i"; \
	echo "		make"; \
	echo "		cd .."; \
	echo "	fi"; \
	echo "done"; \
	} | sh



pccsi_net.jar :	${PCCSI_CLASSES}
	jar cf $@ ${PCCSI_CLASSES}

amlibtech_com.jar : ${AMLIBTECH_CLASSES}
	jar cf $@ `find com |grep '\.class$$'`
	javadoc -d DOC `find com -type d |sed 's/\//./g'` 

