# RE_SID: @(%)/net/shipyard/files2/builds/capybara/unix/pcnfsvr/pcnfsd/SCCS/s.Makefile 1.2 95/09/28 14:36:23 SMI
#
#	@(#)Makefile	1.2	28 Sep 1995
#
# Makefile for rpc.pcnfsd
#

SVR_SRCS=  \
	pcnfsd_svc.c \
	pcnfsd_xdr.c \
	pcnfsd_v1.c \
	pcnfsd_v2.c \
	pcnfsd_misc.c \
	pcnfsd_cache.c \
	pcnfsd_print.c

TST_SRCS= \
	pcnfsd_clnt.c \
	pcnfsd_test.c \
	pcnfsd_xdr.c

HDRS=	pcnfsd.h common.h

def:
	make gleeson

gleeson: $(SYSTEM_MFG)


no_type:
	@echo "Usage: make {sunos.41x | sunos.403c | svr4 | bsd | ultrix.4x | mips}"

sunos.41x:	$(SVR_SRCS) $(TST_SRCS) $(HDRS) Makefile.sunos.41x
	@-mkdir sunos.41x
	make -f Makefile.sunos.41x

sunos.403c:	$(SVR_SRCS) $(TST_SRCS) $(HDRS) Makefile.sunos.403c
	@-mkdir sunos.403c 
	make -f Makefile.sunos.403c

#
# the svr4 builds require architecture information
#
svr4-sparc:	svr4

svr4-ppc:	svr4

svr4-i386:	svr4

svr4:	$(SVR_SRCS) $(TST_SRCS) $(HDRS) pcnfsd.8c Makefile.svr4
	sed s/8C/1M/ pcnfsd.8c > pcnfsd.1m
	@-mkdir svr4-`uname -p`
	make -f Makefile.svr4

motorola:	$(SVR_SRCS) $(TST_SRCS) $(HDRS) Makefile.motorola
	@-mkdir motorola-`uname -p`
	make -f Makefile.motorola

mips:	$(SVR_SRCS) $(TST_SRCS) $(HDRS) Makefile.mips
	@-mkdir mips-`uname -t`
	make -f Makefile.mips

bsd:	$(SVR_SRCS) $(TST_SRCS) $(HDRS) Makefile.bsd
	@-mkdir bsd
	make -f Makefile.bsd

ultrix.4x:	$(SVR_SRCS) $(TST_SRCS) $(HDRS) Makefile.ultrix.4x
	@-mkdir ultrix.4x
	make -f Makefile.ultrix.4x


clean:
	rm -rf sunos.41x sunos.403c svr4* bsd ultrix.4x motorola-* mips-*

clean_sunos.41x:
	rm -rf sunos.41x

clean_sunos.403c:
	rm -rf sunos.403c

clean_svr4-sparc:	clean_svr4

clean_svr4-i386:	clean_svr4

clean_svr4:
	rm -rf svr4-`uname -p` pcnfsd.1m

clean_bsd:
	rm -rf bsd

clean_ultrix.4x:
	rm -rf ultrix.4x
