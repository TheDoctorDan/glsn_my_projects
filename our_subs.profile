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
HOME2=/usr/share/terminfo
TERMCAP="/usr/gleeson/etc/printercap"
MAN=/usr/catman/gleeson/man3
PHONEBOOK="/usr/gleeson/phone.book"
MODELS="/usr/client/models"
I="-I/usr/gleeson/our_subs/src/others -I/usr/gleeson/our_subs/src/window -lm -lisam"

. /usr/gleeson/scripts/CCVARS_machine

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

/bin/tty >/dev/null
if [ $? -eq 0 ]
then
	if [ "$MACHINENAME" = "glsn1" ]
	then
		tt -b
		trap 'tt -e; exit 0' 1 2 3 4 5 6 7 8
		sh
		tt -e
		exit 0
	fi
fi
