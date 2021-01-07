#!/bin/sh
# RE_SID: @(%)/net/shipyard/files2/builds/capybara/unix/pcnfsvr/SCCS/s.dopcnfsd.sh 1.1 94/05/15 19:46:18 SMI
#
#  @(#)dopcnfsd.sh	1.1	15 May 1994
# dopcnfsd.sh
#
# This script will unpack the pcnfsd version 2 sources, including a client
# test program. It does NOT make or install any of the individual components. 
# This must be done manually. Refer to the section on Installing PCNFSD in 
# the TODO????????
#
# To use this script you must first have copied the following files from the
# PC-NFS distribution diskette:
#		dopcnfsd.sh   ( this file )
#		pcnfsd.taz    ( compressed tar file )
# to some place on the system you are going to perform the install.  Usually
# Usually /usr/tmp is a good place.  This script does not take any 
# arguments.
#

PCNFSD=pcnfsd

fatalerror ( )   # handle fatal errors
{
    echo " " ; echo "*** FATAL ERROR: $@" 1>&2 ; exit 1
}

if [ $# != 0 ]
then
	fatalerror "   Usage: dopcnfsd"
fi

echo " "
echo "Unpack PCNFSD shell script"
echo " "

if [ ! -f $PCNFSD.taz ]
then
    fatalerror "The compressed tar file $PCNFSD.taz is missing." 
fi

echo " "
echo "Uncompressing the $PCNFSD.taz file..."
mv -f $PCNFSD.taz $PCNFSD.tar.Z
uncompress -v $PCNFSD.tar.Z
if [ $? != 0 ] ; then
	fatalerror "Uncompressing $PCNFSD.taz failed."
fi

echo " "
echo "Unpacking the $PCNFSD.tar file..."
tar -xvf $PCNFSD.tar
if [ $? != 0 ] ; then
	fatalerror "Extracting files from $PCNFSD.tar failed."
fi

echo " "
echo "Unpack PCNFSD script completed."
echo " "
echo "You can build the daemon and test programs by running"
echo "the makefile provided."
echo "Pre-compiled versions of the daemon and test program"
echo "are included in this distribution in the OS specific directories."
echo " "
echo "Refer to the ?????? for details."
echo " "
echo " "


