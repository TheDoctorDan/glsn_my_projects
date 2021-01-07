.SUFFIXES: .c .o
HOME=/usr/gleeson
BIN=$(HOME)/bin

#	$Header: /usr/people/sam/tiff/tools/RCS/Makefile.mips,v 1.18 92/02/19 14:38:13 sam Exp $
#
# TIFF Library Tools
#
# Copyright (c) 1988, 1989, 1990, 1991, 1992 Sam Leffler
# Copyright (c) 1991, 1992 Silicon Graphics, Inc.
# 
# Permission to use, copy, modify, distribute, and sell this software and 
# its documentation for any purpose is hereby granted without fee, provided
# that (i) the above copyright notices and this permission notice appear in
# all copies of the software and related documentation, and (ii) the names of
# Sam Leffler and Silicon Graphics may not be used in any advertising or
# publicity relating to the software without the specific, prior written
# permission of Stanford and Silicon Graphics.
# 
# THE SOFTWARE IS PROVIDED "AS-IS" AND WITHOUT WARRANTY OF ANY KIND, 
# EXPRESS, IMPLIED OR OTHERWISE, INCLUDING WITHOUT LIMITATION, ANY 
# WARRANTY OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  
# 
# IN NO EVENT SHALL SAM LEFFLER OR SILICON GRAPHICS BE LIABLE FOR
# ANY SPECIAL, INCIDENTAL, INDIRECT OR CONSEQUENTIAL DAMAGES OF ANY KIND,
# OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
# WHETHER OR NOT ADVISED OF THE POSSIBILITY OF DAMAGE, AND ON ANY THEORY OF 
# LIABILITY, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE 
# OF THIS SOFTWARE.
#
NULL=
IPATH=	-I$(GOI) -I/usr/gleeson/our_subs/src/tiff
#
# If you don't want the public domain getopt code, then
# simply null this out and you'll get whatever is in your
# libc (or similar).
#
#GETOPT=	getopt.o
#
# Library-wide configuration defines:
#    MMAP_SUPPORT	add support for memory mapping read-only files
#    COLORIMETRY_SUPPORT add support for 6.0 colorimetry tags
#    JPEG_SUPPORT	add support for 6.0 JPEG tags & JPEG algorithms
#    YCBCR_SUPPORT	add support for 6.0 YCbCr tags
#    CMYK_SUPPORT	add support for 6.0 CMYK tags
#
# Note that if you change the library-wide configuration, you'll
# need to manual force a full rebuild. 
#
CONF_LIBRARY=\
	-DSYSV=1 \
	-DUSE_VARARGS=0 \
	-DUSE_PROTOTYPES=1 \
	-DCOLORIMETRY_SUPPORT \
	-DYCBCR_SUPPORT \
	${NULL}
COPTS=	-DSYSV=1
CFLAGS=	 ${COPTS} ${IPATH} ${CC_FLAGS}
#
LIBTIFF=$(GOL)/libtiff.a
LIBS=	${LIBTIFF}
OBJS=	\
	fax2tiff.o \
	gif2tiff.o \
	pal2rgb.o \
	ppm2tiff.o \
	rgb2ycbcr.o \
	tiff2bw.o \
	tiff2ps.o \
	tiffcmp.o \
	tiffcp.o \
	tiffdither.o \
	tiffdump.o \
	tiffinfo.o \
	tiffmedian.o \
	tiffsplit.o \
	${GETOPT} \
	${NULL}
ALL=\
	gif2tiff \
	pal2rgb \
	ppm2tiff \
	rgb2ycbcr \
	tiff2bw \
	tiff2ps \
	tiffcmp \
	tiffcp \
	tiffdither \
	tiffdump \
	tiffinfo \
	tiffmedian \
	tiffsplit \
	fax2tiff \
	${MACHALL} \
	${NULL}









all:	 ${ALL}

tiffinfo: $(BIN)/tiffinfo

$(BIN)/tiffinfo: tiffinfo.c ${GETOPT} ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} tiffinfo.c ${GETOPT} ${LIBS}

tiffcmp:$(BIN)/tiffcmp

$(BIN)/tiffcmp:tiffcmp.c ${GETOPT} ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} tiffcmp.c ${GETOPT} ${LIBS}

tiffcp:	$(BIN)/tiffcp

$(BIN)/tiffcp:	tiffcp.c ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} tiffcp.c ${LIBS}

tiffdump: $(BIN)/tiffdump

$(BIN)/tiffdump: tiffdump.c
	${CC} -o $@ ${CFLAGS} tiffdump.c




tiffmedian: $(BIN)/tiffmedian

$(BIN)/tiffmedian: tiffmedian.c ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} tiffmedian.c ${LIBS}




tiffsplit: $(BIN)/tiffsplit

$(BIN)/tiffsplit: tiffsplit.c ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} tiffsplit.c ${LIBS}



tiff2ps: $(BIN)/tiff2ps

$(BIN)/tiff2ps: tiff2ps.c ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} tiff2ps.c ${LIBS} -lm




# junky stuff...

# convert RGB image to B&W
tiff2bw: $(BIN)/tiff2bw

$(BIN)/tiff2bw: tiff2bw.c ${GETOPT} ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} tiff2bw.c ${GETOPT} ${LIBS}



# convert B&W image to bilevel w/ FS dithering
tiffdither: $(BIN)/tiffdither

$(BIN)/tiffdither: tiffdither.c ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} tiffdither.c ${LIBS}




# Group 3 FAX file converter
fax2tiff: $(BIN)/fax2tiff

$(BIN)/fax2tiff: fax2tiff.c ${GETOPT} ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} ${CONF_LIBRARY} fax2tiff.c ${GETOPT} ${LIBS}




# GIF converter
gif2tiff: $(BIN)/gif2tiff

$(BIN)/gif2tiff: gif2tiff.c ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} gif2tiff.c ${LIBS} -lm




# PBM converter
ppm2tiff: $(BIN)/ppm2tiff

$(BIN)/ppm2tiff: ppm2tiff.c ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} ppm2tiff.c ${LIBS}




# convert Palette image to RGB
pal2rgb: $(BIN)/pal2rgb

$(BIN)/pal2rgb: pal2rgb.c ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} pal2rgb.c ${LIBS}



# convert RGB image to YCbCr
rgb2ycbcr: $(BIN)/rgb2ycbcr

$(BIN)/rgb2ycbcr: rgb2ycbcr.c ${GETOPT} ${LIBTIFF}
	${CC} -o $@ ${CFLAGS} rgb2ycbcr.c ${GETOPT} ${LIBS} -lm




install: all

clean:
	rm -f ${ALL} ${OBJS} core a.out ycbcr
