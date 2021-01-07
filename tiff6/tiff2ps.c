#ifndef lint
static char rcsid[] = "$Header: /usr/people/sam/tiff/tools/RCS/tiff2ps.c,v 1.21 92/03/31 10:53:36 sam Exp $";
#endif

/*
 * Copyright (c) 1988, 1989, 1990, 1991, 1992 Sam Leffler
 * Copyright (c) 1991, 1992 Silicon Graphics, Inc.
 *
 * Permission to use, copy, modify, distribute, and sell this software and 
 * its documentation for any purpose is hereby granted without fee, provided
 * that (i) the above copyright notices and this permission notice appear in
 * all copies of the software and related documentation, and (ii) the names of
 * Sam Leffler and Silicon Graphics may not be used in any advertising or
 * publicity relating to the software without the specific, prior written
 * permission of Sam Leffler and Silicon Graphics.
 * 
 * THE SOFTWARE IS PROVIDED "AS-IS" AND WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS, IMPLIED OR OTHERWISE, INCLUDING WITHOUT LIMITATION, ANY 
 * WARRANTY OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  
 * 
 * IN NO EVENT SHALL SAM LEFFLER OR SILICON GRAPHICS BE LIABLE FOR
 * ANY SPECIAL, INCIDENTAL, INDIRECT OR CONSEQUENTIAL DAMAGES OF ANY KIND,
 * OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER OR NOT ADVISED OF THE POSSIBILITY OF DAMAGE, AND ON ANY THEORY OF 
 * LIABILITY, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE 
 * OF THIS SOFTWARE.
 */

#include <math.h>
#include <stdio.h>
#include "tiffio.h"

#ifndef aix
typedef	unsigned char u_char;
typedef	unsigned short u_short;
typedef unsigned long u_long;
#endif

extern	double atof();

char	*filename;

main(argc, argv)
	int argc;
	char *argv[];
{
	int dirnum = -1, c;
	TIFF *tif;
	char *cp;
	float width = 0.0;
	float height = 0.0;
	extern char *optarg;
	extern int optind;

	while ((c = getopt(argc, argv, "h:H:w:W:d:")) != -1)
		switch (c) {
		case 'd':
			dirnum = atoi(optarg);
			break;
		case 'h': case 'H':
			height = atof(optarg);
			break;
		case 'w': case 'W':
			width = atof(optarg);
			break;
		case '?':
			usage(-1);
		}
	if (argc - optind < 1)
		usage(-2);
	tif = TIFFOpen(filename = argv[optind], "r");
	if (tif != NULL) {
		if (dirnum != -1 && !TIFFSetDirectory(tif, dirnum))
			exit(-1);
		TIFF2PS(stdout, tif, width, height);
	}
	exit(0);
}

usage(code)
	int code;
{
	fprintf(stderr, "Usage: tiff2ps %s %s %s file\n"
	    , "[-w inches]"
	    , "[-h inches]"
	    , "[-d dirnum]"
	);
	exit(code);
}

static	u_short samplesperpixel;
static	u_short bitspersample;
static	u_short planarconfiguration;
static	u_short photometric;
static	u_short matte = 0;

static
checkImage(tif)
	TIFF *tif;
{
	switch (bitspersample) {
	case 1: case 2:
	case 4: case 8:
		break;
	default:
		TIFFError(filename, "Can not handle %d-bit/sample image",
		    bitspersample);
		return (0);
	}
	switch (photometric) {
	case PHOTOMETRIC_RGB:
		if (matte && bitspersample != 8) {
			TIFFError(filename,
			    "Can not handle %d-bit/sample RGB image with alpha",
			    bitspersample);
			return (0);
		}
		/* fall thru... */
	case PHOTOMETRIC_SEPARATED:
	case PHOTOMETRIC_PALETTE:
	case PHOTOMETRIC_MINISBLACK:
	case PHOTOMETRIC_MINISWHITE:
		break;
	default:
		TIFFError(filename,
		    "Can not handle image with PhotometricInterpretation=%d",
		    photometric);
		return (0);
	}
	if (planarconfiguration == PLANARCONFIG_SEPARATE && matte)
		TIFFWarning(filename, "Ignoring alpha data");
	return (1);
}

#define PS_UNIT_SIZE	72.0
#define	PSUNITS(npix,res)	((npix) * (PS_UNIT_SIZE / (res)))

static	char RGBcolorimage[] = "\
/bwproc {\n\
    rgbproc\n\
    dup length 3 idiv string 0 3 0\n\
    5 -1 roll {\n\
	add 2 1 roll 1 sub dup 0 eq {\n\
	    pop 3 idiv\n\
	    3 -1 roll\n\
	    dup 4 -1 roll\n\
	    dup 3 1 roll\n\
	    5 -1 roll put\n\
	    1 add 3 0\n\
	} { 2 1 roll } ifelse\n\
    } forall\n\
    pop pop pop\n\
} def\n\
/colorimage where {pop} {\n\
    /colorimage {pop pop /rgbproc exch def {bwproc} image} bind def\n\
} ifelse\n\
";

/*
 * Adobe Photoshop requires a comment line of the form:
 *
 * %ImageData: <cols> <rows> <depth>  <main channels> <pad channels>
 *	<block size> <1 for binary|2 for hex> "data start"
 *
 * It is claimed to be part of some future revision of the EPS spec.
 */
static
PhotoshopBanner(fd, w, h, bs, nc, startline)
	FILE *fd;
	u_long w, h;
	int bs, nc;
	char *startline;
{
	fprintf(fd, "%%ImageData: %ld %ld %d %d 0 %d 2 \"",
	    w, h, bitspersample, nc, bs);
	fprintf(fd, startline, nc);
	fprintf(fd, "\"\n");
}

static	u_short tf_bytesperrow;
static	u_short ps_bytesperrow;
static	char *hex = "0123456789abcdef";

TIFF2PS(fd, tif, width, height)
	FILE* fd;
	TIFF *tif;
	float width, height;
{
	u_long w, h;
	u_short res_tag, res_unit;
	int bgc;
	float ox, oy, xres, yres;
	long t;
	extern char *ctime();

	tf_bytesperrow = TIFFScanlineSize(tif);
	TIFFGetFieldDefaulted(tif, TIFFTAG_BITSPERSAMPLE, &bitspersample);
	TIFFGetFieldDefaulted(tif, TIFFTAG_SAMPLESPERPIXEL, &samplesperpixel);
	TIFFGetField(tif, TIFFTAG_PLANARCONFIG, &planarconfiguration);
	TIFFGetField(tif, TIFFTAG_PHOTOMETRIC, &photometric);
	TIFFGetField(tif, TIFFTAG_MATTEING, &matte);
	if (!checkImage(tif))
		return;

	TIFFGetField(tif, TIFFTAG_IMAGEWIDTH, &w);
	TIFFGetField(tif, TIFFTAG_IMAGELENGTH, &h);
	if (!TIFFGetField(tif, TIFFTAG_XPOSITION, &ox))
		ox = 0;
	if (!TIFFGetField(tif, TIFFTAG_YPOSITION, &oy))
		oy = 0;
	if (!TIFFGetField(tif, TIFFTAG_RESOLUTIONUNIT, &res_unit))
		res_tag = RESUNIT_NONE;
	switch (res_tag) {
	case RESUNIT_CENTIMETER:
		res_unit = 2.54; /* centimeters per inch */
		break;
	case RESUNIT_INCH:
	case RESUNIT_NONE:
	default:
		res_unit = 1.0;
		break;
	}
	if (width == 0.0) {
		if (!TIFFGetField(tif, TIFFTAG_XRESOLUTION, &xres))
			xres = PS_UNIT_SIZE / res_unit;
	} else
		xres = (float) w / width;
	if (height == 0.0) {
		if (!TIFFGetField(tif, TIFFTAG_YRESOLUTION, &yres))
			yres = PS_UNIT_SIZE / res_unit;
	} else
		yres = (float) h / height;

	t = time(0);
	fprintf(fd, "%%!PS-Adobe-3.0 EPSF-3.0\n");
	fprintf(fd, "%%%%Creator: tiff2ps\n");
	fprintf(fd, "%%%%Title: %s\n", filename);
	fprintf(fd, "%%%%CreationDate: %s", ctime(&t));
	fprintf(fd, "%%%%Origin: %ld %ld\n", (long) ox, (long) oy);
	fprintf(fd, "%%%%BoundingBox: 0 0 %ld %ld\n",
	    (long) ceil(PSUNITS(w,xres)), (long) ceil(PSUNITS(h,yres)));
	fprintf(fd, "%%%%EndComments\n");
	fprintf(fd, "gsave\n");
	fprintf(fd, "10 dict begin\n");

	ps_bytesperrow = tf_bytesperrow;
	switch (photometric) {
	case PHOTOMETRIC_RGB:
		if (planarconfiguration == PLANARCONFIG_CONTIG) {
			fprintf(fd, "%s", RGBcolorimage);
			PSColorContigPreamble(fd, w, h, xres, yres, 3);
			PSDataColorContig(fd, tif, w, h, 3);
		} else {
			PSColorSeparatePreamble(fd, w, h, xres, yres, 3);
			PSDataColorSeparate(fd, tif, w, h, 3);
		}
		break;
	case PHOTOMETRIC_SEPARATED:
		/*  should emit CMYKcolorimage */
		if (planarconfiguration == PLANARCONFIG_CONTIG) {
			PSColorContigPreamble(fd, w, h, xres, yres, 4);
			PSDataColorContig(fd, tif, w, h, 4);
		} else {
			PSColorSeparatePreamble(fd, w, h, xres, yres, 4);
			PSDataColorSeparate(fd, tif, w, h, 4);
		}
		break;
	case PHOTOMETRIC_PALETTE:
		fprintf(fd, "%s", RGBcolorimage);
		PhotoshopBanner(fd, w, h, 1, 3, "false 3 colorimage");
		fprintf(fd, "/scanLine %d string def\n", ps_bytesperrow);
		fprintf(fd, "%f %f scale\n",
		    (float) PSUNITS(w,xres), (float) PSUNITS(h,yres));
		fprintf(fd, "%lu %lu 8\n", w, h);
		fprintf(fd, "[%lu 0 0 -%lu 0 %lu]\n", w, h, h);
		fprintf(fd, "{currentfile scanLine readhexstring pop} bind\n");
		fprintf(fd, "false 3 colorimage\n");
		PSDataPalette(fd, tif, w, h);
		break;
	case PHOTOMETRIC_MINISBLACK:
	case PHOTOMETRIC_MINISWHITE:
		PhotoshopBanner(fd, w, h, 1, 1, "image");
		fprintf(fd, "/scanLine %d string def\n", ps_bytesperrow);
		fprintf(fd, "%f %f scale\n",
		    (float) PSUNITS(w,xres), (float) PSUNITS(h,yres));
		fprintf(fd, "%lu %lu %d\n", w, h, bitspersample);
		fprintf(fd, "[%lu 0 0 -%lu 0 %lu]\n", w, h, h);
		fprintf(fd, "{currentfile scanLine readhexstring pop} bind\n");
		fprintf(fd, "image\n");
		PSDataBW(fd, tif, w, h);
		break;
	}
	putc('\n', fd);
	fprintf(fd, "end\n");
	fprintf(fd, "grestore\n");
	fprintf(fd, "showpage\n");
	fprintf(fd, "%%%%Trailer\n");
}

PSColorContigPreamble(fd, w, h, xres, yres, nc)
	FILE *fd;
	u_long w, h;
	float xres, yres;
	int nc;
{
	ps_bytesperrow = (u_short) nc * (tf_bytesperrow / samplesperpixel);
	PhotoshopBanner(fd, w, h, 1, nc, "false %d colorimage");
	fprintf(fd, "/line %d string def\n", ps_bytesperrow);
	fprintf(fd, "%f %f scale\n",
	    (float) PSUNITS(w,xres), (float) PSUNITS(h,yres));
	fprintf(fd, "%lu %lu %d\n", w, h, bitspersample);
	fprintf(fd, "[%lu 0 0 -%lu 0 %lu]\n", w, h, h);
	fprintf(fd, "{currentfile line readhexstring pop} bind\n");
	fprintf(fd, "false %d colorimage\n", nc);
}

PSColorSeparatePreamble(fd, w, h, xres, yres, nc)
	FILE *fd;
	u_long w, h;
	float xres, yres;
	int nc;
{
	int i;

	PhotoshopBanner(fd, w, h, ps_bytesperrow, nc, "true %d colorimage");
	for (i = 0; i < nc; i++)
		fprintf(fd, "/line%d %d string def\n", i, ps_bytesperrow);
	fprintf(fd, "%f %f scale\n",
	    (float) PSUNITS(w,xres), (float) PSUNITS(h,yres));
	fprintf(fd, "%lu %lu %d\n", w, h, bitspersample);
	fprintf(fd, "[%lu 0 0 -%lu 0 %lu] \n", w, h, h);
	for (i = 0; i < nc; i++)
		fprintf(fd, "{currentfile line%d readhexstring pop}bind\n", i);
	fprintf(fd, "true %d colorimage\n", nc);
}

#define MAXLINE		36
#define	DOBREAK(len, howmany, fd) \
	if (((len) -= (howmany)) <= 0) {	\
		putc('\n', fd);			\
		(len) = MAXLINE-(howmany);	\
	}
#define	PUTHEX(c,fd)	putc(hex[((c)>>4)&0xf],fd); putc(hex[(c)&0xf],fd)

PSDataColorContig(fd, tif, w, h, nc)
	FILE* fd;
	TIFF* tif;
	u_long w, h;
	int nc;
{
	u_long row;
	int breaklen = MAXLINE, cc, es = samplesperpixel - nc;
	u_char *tf_buf;
	u_char *cp, c;

	tf_buf = (u_char *) malloc(tf_bytesperrow);
	if (tf_buf == NULL) {
		TIFFError(filename, "No space for scanline buffer");
		return;
	}
	for (row = 0; row < h; row++) {
		if (TIFFReadScanline(tif, tf_buf, row, 0) < 0)
			break;
		cp = tf_buf;
		if (matte) {
			int adjust;
			cc = 0;
			for (; cc < tf_bytesperrow; cc += samplesperpixel) {
				DOBREAK(breaklen, nc, fd);
				/*
				 * For images with alpha, matte against
				 * a white background; i.e.
				 *    Cback * (1 - Aimage)
				 * where Cback = 1.
				 */
				adjust = 255 - cp[nc];
				switch (nc) {
				case 4: c = *cp++ + adjust; PUTHEX(c,fd);
				case 3: c = *cp++ + adjust; PUTHEX(c,fd);
				case 2: c = *cp++ + adjust; PUTHEX(c,fd);
				case 1: c = *cp++ + adjust; PUTHEX(c,fd);
				}
				cp += es;
			}
		} else {
			cc = 0;
			for (; cc < tf_bytesperrow; cc += samplesperpixel) {
				DOBREAK(breaklen, nc, fd);
				switch (nc) {
				case 4: c = *cp++; PUTHEX(c,fd);
				case 3: c = *cp++; PUTHEX(c,fd);
				case 2: c = *cp++; PUTHEX(c,fd);
				case 1: c = *cp++; PUTHEX(c,fd);
				}
				cp += es;
			}
		}
	}
	free((char *) tf_buf);
}

PSDataColorSeparate(fd, tif, w, h, nc)
	FILE* fd;
	TIFF* tif;
	u_long w, h;
	int nc;
{
	u_long row;
	int breaklen = MAXLINE, cc, s, maxs;
	u_char *tf_buf;
	u_char *cp, c;

	tf_buf = (u_char *) malloc(tf_bytesperrow);
	if (tf_buf == NULL) {
		TIFFError(filename, "No space for scanline buffer");
		return;
	}
	maxs = (samplesperpixel > nc ? nc : samplesperpixel);
	for (row = 0; row < h; row++) {
		for (s = 0; s < maxs; s++) {
			if (TIFFReadScanline(tif, tf_buf, row, s) < 0)
				break;
			for (cp = tf_buf, cc = 0; cc < tf_bytesperrow; cc++) {
				DOBREAK(breaklen, 1, fd);
				c = *cp++;
				PUTHEX(c,fd);
			}
		}
	}
	free((char *) tf_buf);
}

#define	PUTRGBHEX(c,fd) \
	PUTHEX(rmap[c],fd); PUTHEX(gmap[c],fd); PUTHEX(bmap[c],fd)

static int
checkcmap(tif, n, r, g, b)
	TIFF *tif;
	int n;
	u_short *r, *g, *b;
{
	while (n-- > 0)
		if (*r++ >= 256 || *g++ >= 256 || *b++ >= 256)
			return (16);
	TIFFWarning(filename, "Assuming 8-bit colormap");
	return (8);
}

PSDataPalette(fd, tif, w, h)
	FILE* fd;
	TIFF* tif;
	u_long w, h;
{
	u_short *rmap, *gmap, *bmap;
	u_long row;
	int breaklen = MAXLINE, cc, nc;
	u_char *tf_buf;
	u_char *cp, c;

	if (!TIFFGetField(tif, TIFFTAG_COLORMAP, &rmap, &gmap, &bmap)) {
		TIFFError(filename, "Palette image w/o \"Colormap\" tag");
		return;
	}
	switch (bitspersample) {
	case 8:	case 4: case 2: case 1:
		break;
	default:
		TIFFError(filename, "Depth %d not supported", bitspersample);
		return;
	}
	nc = 3 * (8 / bitspersample);
	tf_buf = (u_char *) malloc(tf_bytesperrow);
	if (tf_buf == NULL) {
		TIFFError(filename, "No space for scanline buffer");
		return;
	}
	if (checkcmap(tif, 1<<bitspersample, rmap, gmap, bmap) == 16) {
		int i;
#define	CVT(x)		(((x) * 255) / ((1L<<16)-1))
		for (i = (1<<bitspersample)-1; i > 0; i--) {
			rmap[i] = CVT(rmap[i]);
			gmap[i] = CVT(gmap[i]);
			bmap[i] = CVT(bmap[i]);
		}
#undef CVT
	}
	for (row = 0; row < h; row++) {
		if (TIFFReadScanline(tif, tf_buf, row, 0) < 0)
			break;
		for (cp = tf_buf, cc = 0; cc < tf_bytesperrow; cc++) {
			DOBREAK(breaklen, nc, fd);
			switch (bitspersample) {
			case 8:
				c = *cp++; PUTRGBHEX(c, fd);
				break;
			case 4:
				c = *cp++; PUTRGBHEX(c&0xf, fd);
				c >>= 4;   PUTRGBHEX(c, fd);
				break;
			case 2:
				c = *cp++; PUTRGBHEX(c&0x3, fd);
				c >>= 2;   PUTRGBHEX(c&0x3, fd);
				c >>= 2;   PUTRGBHEX(c&0x3, fd);
				c >>= 2;   PUTRGBHEX(c, fd);
				break;
			case 1:
				c = *cp++; PUTRGBHEX(c&0x1, fd);
				c >>= 1;   PUTRGBHEX(c&0x1, fd);
				c >>= 1;   PUTRGBHEX(c&0x1, fd);
				c >>= 1;   PUTRGBHEX(c&0x1, fd);
				c >>= 1;   PUTRGBHEX(c&0x1, fd);
				c >>= 1;   PUTRGBHEX(c&0x1, fd);
				c >>= 1;   PUTRGBHEX(c&0x1, fd);
				c >>= 1;   PUTRGBHEX(c, fd);
				break;
			}
		}
	}
	free((char *) tf_buf);
}

PSDataBW(fd, tif, w, h)
	FILE* fd;
	TIFF* tif;
	u_long w, h;
{
	u_long row;
	int breaklen = MAXLINE, cc;
	u_char *tf_buf;
	u_char *cp, c;

	tf_buf = (u_char *) malloc(tf_bytesperrow);
	if (tf_buf == NULL) {
		TIFFError(filename, "No space for scanline buffer");
		return;
	}
	for (row = 0; row < h; row++) {
		if (TIFFReadScanline(tif, tf_buf, row, 0) < 0)
			break;
		for (cp = tf_buf, cc = 0; cc < tf_bytesperrow; cc++) {
			DOBREAK(breaklen, 1, fd);
			c = *cp++;
			if (photometric == PHOTOMETRIC_MINISWHITE)
				c = ~c;
			PUTHEX(c, fd);
		}
	}
	free((char *) tf_buf);
}
