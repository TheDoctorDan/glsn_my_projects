
# .profile


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


if [ "$MACHINENAME" = "" ]
then
. /usr/gleeson/etc/profile
PATH="$PATH:."
fi
umask 0022
PATH=$PATH:/usr/gleeson/bin:/usr/gleeson/scripts:/usr/games
DATAPATH=".:."
HOME2=/usr/lib/terminfo
TERMCAP="/usr/gleeson/etc/printercap"
MAN=/usr/catman/gleeson/man3
PHONEBOOK="/usr/gleeson/phone.book"
MODELS="/usr/client/models"
I="-I/usr/gleeson/our_subs/src/others -I/usr/gleeson/our_subs/src/window -lm -lisam"
Sd=""
OPTIMIZE_OPT=""
if hp-mc680x0
then
	OPTIMIZE_OPT="+O1"
	Sd="-Dhpmc68 -DBYTEALIGN4 -DSYSTYPE_SYSV"
	I="-Dhpmc68 -DSYSTYPE_SYSV $I"
fi
if mips
then
	OPTIMIZE_OPT="-O"
	Sd="-G80 -Olimit 900 -Dmips -DSYSTYPE_SYSV -DBYTEALIGN8"
	I="-Dmips -DSYSTYPE_SYSV $I"
fi
if ncr
then
	OPTIMIZE_OPT="-O"
	Sd="-Wp,-Sd,500 -Wp,-Dncr,-DBYTEALIGN4"
	I="-Dncr -DBYTEALIGN4 $I"
	if [ "$MACHINENAME" = "safco2" ]
	then
		Sd="$Sd -Wp,-Dsafco"
		I="-Dsafco $I"
	fi
	if [ "$MACHINENAME" = "safco1" ]
	then
		Sd="$Sd -Wp,-Dsafco"
		I="-Dsafco $I"
	fi
	if tower32_200
	then
		I="-DSYSTYPE_SYSV $I"
		Sd="$Sd,-DSYSTYPE_SYSV"
	fi
fi
if motorola
then
	OPTIMIZE_OPT="-O"
	Sd="-Dmotorola -DBYTEALIGN8"
	I="-Dmotorola $I"
fi

if [ "$MACHINENAME" = "safco2" ]
then
	Sd="$Sd -Wp,-Dsafco"
	I="-Dsafco $I"
fi
if [ "$MACHINENAME" = "safco1" ]
then
	Sd="$Sd -Wp,-Dsafco"
	I="-Dsafco $I"
fi




unset EXINIT
export DATAPATH TERMCAP HOME2 MAN PHONEBOOK PATH I Sd OPTIMIZE_OPT
MOUSE_HORZ_RATIO=15
MOUSE_VERT_RATIO=15
export MOUSE_HORZ_RATIO MOUSE_VERT_RATIO
export MODELS
