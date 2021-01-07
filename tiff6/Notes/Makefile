#	$Header: /usr/people/sam/tiff/RCS/Makefile,v 1.21 92/03/29 15:54:34 sam Exp $

NULL=

default:

clean:
	-cd libtiff; make clean; rm -f Makefile
	-cd tools; make clean; rm -f Makefile
	-cd contrib/fax2ps; make clean
	-cd contrib/dbs; make clean
	-cd contrib/dbs/xtiff; make clean
	-cd man/man3; make clean

tiff.tar: clean
	tar cvf tiff.tar \
	    Makefile \
	    HOWTO \
	    README \
	    CHANGES-v2.3 \
	    CHANGES-v2.4 \
	    CHANGES-v2.5 \
	    CHANGES-v3.0 \
	    VERSION \
	    Copyright \
	    doc/ClassF.txt \
	    doc/TIFF6.ps \
	    man/man1/*.1 \
	    man/man3/*.3t \
	    libtiff/README \
	    libtiff/*.[ch] \
	    libtiff/Makefile* \
	    tools/*.[ch] \
	    tools/Makefile* \
	    contrib/dbs/README \
	    contrib/dbs/*.c \
	    contrib/dbs/Imakefile \
	    contrib/dbs/xtiff/Imakefile \
	    contrib/dbs/xtiff/README \
	    contrib/dbs/xtiff/*.[ch] \
	    contrib/fax2ps/README \
	    contrib/fax2ps/Makefile \
	    contrib/fax2ps/*.[ch] \
	    contrib/fax2ps/fax2ps.1 \
	    contrib/ras \
	    contrib/vms \
	    contrib/xv \
	    ${NULL}
	compress tiff.tar
tiffpics.tar:
	tar cvf tiffpics.tar \
	    pics/README \
	    pics/cramps.tif \
	    pics/cramps-tile.tif \
	    pics/fax2d.tif \
	    pics/g3test.tif \
	    pics/jello.tif \
	    pics/jim___cg.tif \
	    pics/jim___dg.tif \
	    pics/jim___gg.tif \
	    pics/jim___ah.tif \
	    pics/strike.tif \
	    pics/oxford.tif \
	    pics/quad-lzw.tif \
	    pics/quad-tile.tif \
	    pics/text.tif \
	    pics/ycbcr-cat.tif \
	    pics/smallliz.tif \
	    pics/zackthecat.tif \
	    pics/fax2d.g3 \
	    pics/g3test.g3 \
	    ${NULL}
	compress tiffpics.tar

both:	tiff.tar tiffpics.tar
