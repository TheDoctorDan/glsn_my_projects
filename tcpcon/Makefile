#	Set the follwing for your site
#
#	INSTALLDIR	Place to install binaries
#	XCFLAGS		Extra libraries needed for bsd stuff
#	XLIBS		Extra libraries needed for bsd stuff
#	
#	Values for MIPS RISC/OS 3/4
#CFLAGS=-g
#INSTALLDIR=/usr/local
#XCFLAGS=-I/usr/include/bsd -DSYSTYPE_SYSV
#XLIBS=-lbsd
#
#	Values for BSD 4.3
INSTALLDIR=/usr/local
XCFLAGS= -DSYSTYPE_BSD43
XLIBS=
#

PROGS=nd tcpcon

all: $(PROGS)

tcpcon:	tcpcon.c
	$(CC) $(XCFLAGS) $(CFLAGS) -o $@ tcpcon.c $(LIBS) $(XLIBS)

nd:	nd.c
	$(CC) $(XCFLAGS) $(CFLAGS) -o $@ nd.c $(LIBS) $(XLIBS)

install: all
	/etc/install -o  -f $(INSTALLDIR) nd
	/etc/install -o  -f $(INSTALLDIR) tcpcon
	ln $(INSTALLDIR)/tcpcon $(INSTALLDIR)/tcpserv
	ln $(INSTALLDIR)/tcpcon $(INSTALLDIR)/udpcon
	ln $(INSTALLDIR)/tcpcon $(INSTALLDIR)/udpserv
	chown bin $(INSTALLDIR)/tcpserv $(INSTALLDIR)/udpcon $(INSTALLDIR)/udpserv
	chgrp bin $(INSTALLDIR)/tcpserv $(INSTALLDIR)/udpcon $(INSTALLDIR)/udpserv
	chmod 755 $(INSTALLDIR)/tcpserv $(INSTALLDIR)/udpcon $(INSTALLDIR)/udpserv

clean:

clobber: clean
	rm -f $(PROGS)
