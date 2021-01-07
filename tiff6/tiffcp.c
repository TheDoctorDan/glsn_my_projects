#ifndef lint
static char rcsid[] = "$Header: /usr/people/sam/tiff/tools/RCS/tiffcp.c,v 1.19 92/03/27 12:11:24 sam Exp $";
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

#include <stdio.h>
#include "tiffio.h"

#ifndef aix
typedef	unsigned char u_char;
typedef	unsigned short u_short;
typedef	unsigned int u_int;
typedef	unsigned long u_long;
#endif

#if defined(VMS)
#define unlink delete
#endif

#define	streq(a,b)	(strcmp(a,b) == 0)
#define	CopyField(tag, v) \
    if (TIFFGetField(in, tag, &v)) TIFFSetField(out, tag, v)
#define	CopyField2(tag, v1, v2) \
    if (TIFFGetField(in, tag, &v1, &v2)) TIFFSetField(out, tag, v1, v2)
#define	CopyField3(tag, v1, v2, v3) \
    if (TIFFGetField(in, tag, &v1, &v2, &v3)) TIFFSetField(out, tag, v1, v2, v3)
#define	CopyField4(tag, v1, v2, v3, v4) \
    if (TIFFGetField(in, tag, &v1, &v2, &v3, &v4)) TIFFSetField(out, tag, v1, v2, v3, v4)

static	short config;
static	u_short compression;
static	u_short predictor;
static	u_short fillorder;
static	long rowsperstrip;
static	long g3opts;
static	int ignore = 0;			/* if true, ignore read errors */
static	char *filename;

static	int tiffcp();
static	int cpContig2ContigByRow();
static	int cpContig2SeparateByRow();
static	int cpSeparate2ContigByRow();
static	int cpSeparate2SeparateByRow();
static	int cpDecodedStrips();
static	void usage();

main(argc, argv)
	char *argv[];
{
	short defconfig = -1;
	u_short defcompression = -1;
	u_short defpredictor = 0;
	u_short deffillorder = 0;
	long defrowsperstrip = -1;
	long defg3opts = 0;
	TIFF *in, *out;

	argc--, argv++;
	if (argc < 2)
		usage();
	for (; argc > 2 && argv[0][0] == '-'; argc--, argv++) {
		if (streq(argv[0], "-none")) {
			defcompression = COMPRESSION_NONE;
			continue;
		}
		if (streq(argv[0], "-packbits")) {
			defcompression = COMPRESSION_PACKBITS;
			continue;
		}
		if (streq(argv[0], "-lzw")) {
			defcompression = COMPRESSION_LZW;
			continue;
		}
		if (streq(argv[0], "-g3")) {
			defcompression = COMPRESSION_CCITTFAX3;
			continue;
		}
		if (streq(argv[0], "-g4")) {
			defcompression = COMPRESSION_CCITTFAX4;
			continue;
		}
		if (streq(argv[0], "-contig")) {
			defconfig = PLANARCONFIG_CONTIG;
			continue;
		}
		if (streq(argv[0], "-separate")) {
			defconfig = PLANARCONFIG_SEPARATE;
			continue;
		}
		if (streq(argv[0], "-lsb2msb")) {
			deffillorder = FILLORDER_LSB2MSB;
			continue;
		}
		if (streq(argv[0], "-msb2lsb")) {
			deffillorder = FILLORDER_MSB2LSB;
			continue;
		}
		if (streq(argv[0], "-rowsperstrip")) {
			argc--, argv++;
			defrowsperstrip = atoi(argv[0]);
			continue;
		}
		if (streq(argv[0], "-2d")) {
			defg3opts = GROUP3OPT_2DENCODING;
			continue;
		}
		if (streq(argv[0], "-fill")) {
			defg3opts |= GROUP3OPT_FILLBITS;
			continue;
		}
		if (streq(argv[0], "-predictor")) {
			argc--, argv++;
			defpredictor = atoi(argv[0]);
			continue;
		}
		if (streq(argv[0], "-ignore")) {
			ignore = 1;
			continue;
		}
		usage();
	}
	out = TIFFOpen(argv[argc-1], "w");
	if (out == NULL)
		exit(-2);
	for (; argc > 1; argc--, argv++) {
		in = TIFFOpen(filename = argv[0], "r");
		if (in != NULL) {
			do {
				config = defconfig;
				compression = defcompression;
				predictor = defpredictor;
				fillorder = deffillorder;
				rowsperstrip = defrowsperstrip;
				g3opts = defg3opts;
				if (!tiffcp(in, out) || !TIFFWriteDirectory(out))
					goto bad;
			} while (TIFFReadDirectory(in));
			(void) TIFFClose(in);
		}
	}
	(void) TIFFClose(out);
	exit(0);
bad:
	unlink(TIFFFileName(out));
	exit(1);
}

static
CheckAndCorrectColormap(n, r, g, b)
	int n;
	u_short *r, *g, *b;
{
	int i;

	for (i = 0; i < n; i++)
		if (r[i] >= 256 || g[i] >= 256 || b[i] >= 256)
			return;
	TIFFWarning(filename, "Scaling 8-bit colormap");
#define	CVT(x)		(((x) * ((1L<<16)-1)) / 255)
	for (i = 0; i < n; i++) {
		r[i] = CVT(r[i]);
		g[i] = CVT(g[i]);
		b[i] = CVT(b[i]);
	}
#undef CVT
}

static struct cpTag {
	long	tag;
	short	count;
	TIFFDataType type;
} tags[] = {
	{ TIFFTAG_SUBFILETYPE,		1, TIFF_LONG },
	{ TIFFTAG_PHOTOMETRIC,		1, TIFF_SHORT },
	{ TIFFTAG_THRESHHOLDING,	1, TIFF_SHORT },
	{ TIFFTAG_DOCUMENTNAME,		1, TIFF_ASCII },
	{ TIFFTAG_IMAGEDESCRIPTION,	1, TIFF_ASCII },
	{ TIFFTAG_MAKE,			1, TIFF_ASCII },
	{ TIFFTAG_MODEL,		1, TIFF_ASCII },
	{ TIFFTAG_ORIENTATION,		1, TIFF_SHORT },
	{ TIFFTAG_MINSAMPLEVALUE,	1, TIFF_SHORT },
	{ TIFFTAG_MAXSAMPLEVALUE,	1, TIFF_SHORT },
	{ TIFFTAG_XRESOLUTION,		1, TIFF_RATIONAL },
	{ TIFFTAG_YRESOLUTION,		1, TIFF_RATIONAL },
	{ TIFFTAG_PAGENAME,		1, TIFF_ASCII },
	{ TIFFTAG_XPOSITION,		1, TIFF_RATIONAL },
	{ TIFFTAG_YPOSITION,		1, TIFF_RATIONAL },
	{ TIFFTAG_GROUP4OPTIONS,	1, TIFF_LONG },
	{ TIFFTAG_RESOLUTIONUNIT,	1, TIFF_SHORT },
	{ TIFFTAG_PAGENUMBER,		2, TIFF_SHORT },
	{ TIFFTAG_SOFTWARE,		1, TIFF_ASCII },
	{ TIFFTAG_DATETIME,		1, TIFF_ASCII },
	{ TIFFTAG_ARTIST,		1, TIFF_ASCII },
	{ TIFFTAG_HOSTCOMPUTER,		1, TIFF_ASCII },
	{ TIFFTAG_WHITEPOINT,		1, TIFF_RATIONAL },
	{ TIFFTAG_PRIMARYCHROMATICITIES,-1,TIFF_RATIONAL },
	{ TIFFTAG_HALFTONEHINTS,	2, TIFF_SHORT },
	{ TIFFTAG_BADFAXLINES,		1, TIFF_LONG },
	{ TIFFTAG_CLEANFAXDATA,		1, TIFF_SHORT },
	{ TIFFTAG_CONSECUTIVEBADFAXLINES,1, TIFF_LONG },
	{ TIFFTAG_INKSET,		1, TIFF_SHORT },
	{ TIFFTAG_INKNAMES,		1, TIFF_ASCII },
	{ TIFFTAG_DOTRANGE,		2, TIFF_SHORT },
	{ TIFFTAG_TARGETPRINTER,	1, TIFF_ASCII },
	{ TIFFTAG_SAMPLEFORMAT,		1, TIFF_SHORT },
	{ TIFFTAG_YCBCRCOEFFICIENTS,	-1,TIFF_RATIONAL },
	{ TIFFTAG_YCBCRSUBSAMPLING,	2, TIFF_SHORT },
	{ TIFFTAG_YCBCRPOSITIONING,	1, TIFF_SHORT },
	{ TIFFTAG_REFERENCEBLACKWHITE,	-1,TIFF_RATIONAL },
	{ TIFFTAG_MATTEING,		1, TIFF_SHORT },
};
#define	NTAGS	(sizeof (tags) / sizeof (tags[0]))

static void
cpOtherTags(in, out)
	TIFF *in, *out;
{
	struct cpTag *p;
	short shortv, shortv2;
	float floatv, *floatav;
	char *stringv;
	u_long longv;

	for (p = tags; p < &tags[NTAGS]; p++)
		switch (p->type) {
		case TIFF_SHORT:
			if (p->count == 1) {
				CopyField(p->tag, shortv);
			} else if (p->count == 2) {
				CopyField2(p->tag, shortv, shortv2);
			}
			break;
		case TIFF_LONG:
			CopyField(p->tag, longv);
			break;
		case TIFF_RATIONAL:
			if (p->count == 1) {
				CopyField(p->tag, floatv);
			} else if (p->count == -1) {
				CopyField(p->tag, floatav);
			}
			break;
		case TIFF_ASCII:
			CopyField(p->tag, stringv);
			break;
		}
}

static int
tiffcp(in, out)
	TIFF *in, *out;
{
	short bitspersample, samplesperpixel, shortv, bystrip;
	u_long w, l;

	CopyField(TIFFTAG_IMAGEWIDTH, w);
	CopyField(TIFFTAG_IMAGELENGTH, l);
	CopyField(TIFFTAG_BITSPERSAMPLE, bitspersample);
	if (compression != (u_short)-1)
		TIFFSetField(out, TIFFTAG_COMPRESSION, compression);
	else
		CopyField(TIFFTAG_COMPRESSION, compression);
	if (fillorder != 0)
		TIFFSetField(out, TIFFTAG_FILLORDER, fillorder);
	else
		CopyField(TIFFTAG_FILLORDER, shortv);
	CopyField(TIFFTAG_SAMPLESPERPIXEL, samplesperpixel);
	if (rowsperstrip < 0) {
		/*
		 * RowsPerStrip is left unspecified: use either the
		 * value from the input image or, if nothing is defined,
		 * something that approximates 8Kbyte strips.
		 */
		if (!TIFFGetField(in, TIFFTAG_ROWSPERSTRIP, &rowsperstrip))
			rowsperstrip = (8*1024)/TIFFScanlineSize(out);
	}
	if (rowsperstrip == 0)
		rowsperstrip = 1L;
	TIFFSetField(out, TIFFTAG_ROWSPERSTRIP, rowsperstrip);
	if (config != -1)
		TIFFSetField(out, TIFFTAG_PLANARCONFIG, config);
	else
		CopyField(TIFFTAG_PLANARCONFIG, config);
	if (g3opts != 0)
		TIFFSetField(out, TIFFTAG_GROUP3OPTIONS, g3opts);
	else
		CopyField(TIFFTAG_GROUP3OPTIONS, g3opts);
	if (samplesperpixel <= 4) {
		u_short *tr, *tg, *tb, *ta;
		CopyField4(TIFFTAG_TRANSFERFUNCTION, tr, tg, tb, ta);
	}
	if (predictor != 0)
		TIFFSetField(out, TIFFTAG_PREDICTOR, predictor);
	else
		CopyField(TIFFTAG_PREDICTOR, predictor);
	{ u_short *red, *green, *blue;
	  if (TIFFGetField(in, TIFFTAG_COLORMAP, &red, &green, &blue)) {
		CheckAndCorrectColormap(1<<bitspersample, red, green, blue);
		TIFFSetField(out, TIFFTAG_COLORMAP, red, green, blue);
	  }
	}
/* TileWidth & TileLength */
/* SMinSampleValue & SMaxSampleValue */
/* JPEG stuff */
	cpOtherTags(in, out);

	(void) TIFFGetField(in, TIFFTAG_PLANARCONFIG, &shortv);
	if (shortv != config && bitspersample != 8 && samplesperpixel > 1) {
		fprintf(stderr,
"Can't handle different planar configuration w/ bits/sample != 8\n");
		return (0);
	}
	{ long irps = -1L;
	  TIFFGetField(in, TIFFTAG_ROWSPERSTRIP, &irps);
	  bystrip = (rowsperstrip == irps);
	}
#define	pack(a,b,c)	((u_long)(((a)<<9)|((b)<<1)|(c)))
	switch (pack(shortv, config, bystrip)) {
	case pack(PLANARCONFIG_CONTIG, PLANARCONFIG_CONTIG, 0):
		return (cpContig2ContigByRow(in, out, l));
	case pack(PLANARCONFIG_CONTIG, PLANARCONFIG_CONTIG, 1):
		return (cpDecodedStrips(in, out, l));
	case pack(PLANARCONFIG_SEPARATE, PLANARCONFIG_SEPARATE, 0):
	case pack(PLANARCONFIG_SEPARATE, PLANARCONFIG_SEPARATE, 1):
		return (cpSeparate2SeparateByRow(in, out, l, samplesperpixel));
	case pack(PLANARCONFIG_CONTIG, PLANARCONFIG_SEPARATE, 0):
	case pack(PLANARCONFIG_CONTIG, PLANARCONFIG_SEPARATE, 1):
		return (cpContig2SeparateByRow(in, out, l, samplesperpixel, w));
	case pack(PLANARCONFIG_SEPARATE, PLANARCONFIG_CONTIG, 0):
	case pack(PLANARCONFIG_SEPARATE, PLANARCONFIG_CONTIG, 1):
		return (cpSeparate2ContigByRow(in, out, l, samplesperpixel, w));
	default:
		fprintf(stderr,
	    "tiffcp: %s: Unknown planar configuration (in=%d out=%d).\n",
		    TIFFFileName(in), shortv, config);
		return (0);
	}
	/*NOTREACHED*/
}

static int
cpContig2ContigByRow(in, out, imagelength)
	TIFF *in, *out;
	u_long imagelength;
{
	u_char *buf = (u_char *)malloc(TIFFScanlineSize(in));
	u_long row;

	for (row = 0; row < imagelength; row++) {
		if (TIFFReadScanline(in, buf, row, 0) < 0 && !ignore)
			goto done;
		if (TIFFWriteScanline(out, buf, row, 0) < 0)
			goto bad;
	}
done:
	free(buf);
	return (1);
bad:
	free(buf);
	return (0);
}

static int
cpDecodedStrips(in, out, h)
	TIFF *in, *out;
	u_long h;
{
	u_long stripsize  = TIFFStripSize(in);
	u_char *buf = (u_char *)malloc(stripsize);

	if (buf) {
		u_int s, ns = TIFFNumberOfStrips(in);
		u_long row = 0;
		for (s = 0; s < ns; s++) {
			u_int cc = (row + rowsperstrip > h) ?
			    TIFFVStripSize(in, h - row) : stripsize;
			if (TIFFReadEncodedStrip(in, s, buf, cc) < 0 && !ignore)
				break;
			if (TIFFWriteEncodedStrip(out, s, buf, cc) < 0) {
				free(buf);
				return (0);
			}
			row += rowsperstrip;
		}
		free(buf);
		return (1);
	}
	return (0);
}

static int
cpSeparate2SeparateByRow(in, out, imagelength, samplesperpixel)
	TIFF *in, *out;
	u_long imagelength;
	short samplesperpixel;
{
	u_char *buf = (u_char *)malloc(TIFFScanlineSize(in));
	u_long row;
	int s;

	for (s = 0; s < samplesperpixel; s++) {
		for (row = 0; row < imagelength; row++) {
			if (TIFFReadScanline(in, buf, row, s) < 0 && !ignore)
				goto done;
			if (TIFFWriteScanline(out, buf, row, s) < 0)
				goto bad;
		}
	}
done:
	free(buf);
	return (1);
bad:
	free(buf);
	return (0);
}

static int
cpContig2SeparateByRow(in, out, imagelength, samplesperpixel, imagewidth)
	TIFF *in, *out;
	u_long imagelength;
	short samplesperpixel;
	u_long imagewidth;
{
	u_char *inbuf = (u_char *)malloc(TIFFScanlineSize(in));
	u_char *outbuf = (u_char *)malloc(TIFFScanlineSize(out));
	register u_char *inp, *outp;
	register u_long n;
	u_long row;
	int s;

	/* unpack channels */
	for (s = 0; s < samplesperpixel; s++) {
		for (row = 0; row < imagelength; row++) {
			if (TIFFReadScanline(in, inbuf, row, 0) < 0 && !ignore)
				goto done;
			inp = inbuf + s;
			outp = outbuf;
			for (n = imagewidth; n-- > 0;) {
				*outp++ = *inp;
				inp += samplesperpixel;
			}
			if (TIFFWriteScanline(out, outbuf, row, s) < 0)
				goto bad;
		}
	}
done:
	if (inbuf) free(inbuf);
	if (outbuf) free(outbuf);
	return (1);
bad:
	if (inbuf) free(inbuf);
	if (outbuf) free(outbuf);
	return (0);
}

static int
cpSeparate2ContigByRow(in, out, imagelength, samplesperpixel, imagewidth)
	TIFF *in, *out;
	u_long imagelength;
	int samplesperpixel;
	u_long imagewidth;
{
	u_char *inbuf = (u_char *)malloc(TIFFScanlineSize(in));
	u_char *outbuf = (u_char *)malloc(TIFFScanlineSize(out));
	register u_char *inp, *outp;
	register u_long n;
	u_long row;
	int s;

	for (row = 0; row < imagelength; row++) {
		/* merge channels */
		for (s = 0; s < samplesperpixel; s++) {
			if (TIFFReadScanline(in, inbuf, row, s) < 0 && !ignore)
				goto done;
			inp = inbuf;
			outp = outbuf + s;
			for (n = imagewidth; n-- > 0;) {
				*outp = *inp++;
				outp += samplesperpixel;
			}
		}
		if (TIFFWriteScanline(out, outbuf, row, 0) < 0)
			goto bad;
	}
done:
	if (inbuf) free(inbuf);
	if (outbuf) free(outbuf);
	return (1);
bad:
	if (inbuf) free(inbuf);
	if (outbuf) free(outbuf);
	return (0);
}

static void
usage()
{
	fprintf(stderr, "usage: tiffcp [options] input... output\n");
	fprintf(stderr, "where options are:\n");
	fprintf(stderr,
	    " -contig\tpack samples contiguously (e.g. RGBRGB...)\n");
	fprintf(stderr,
	    " -separate\tstore samples separately (e.g. RRR...GGG...BBB...)\n");
	fprintf(stderr, "\n");
	fprintf(stderr, " -lsb2msb\tforce lsb-to-msb FillOrder for output\n");
	fprintf(stderr, " -msb2lsb\tforce msb-to-lsb FillOrder for output\n");
	fprintf(stderr, "\n");
	fprintf(stderr,
	    " -lzw\t\tcompress output with Lempel-Ziv & Welch encoding\n");
	fprintf(stderr,
	    " -packbits\tcompress output with packbits encoding\n");
	fprintf(stderr,
	    " -g3\t\tcompress output with CCITT Group 3 encoding\n");
	fprintf(stderr,
	    " -g4\t\tcompress output with CCITT Group 4 encoding\n");
	fprintf(stderr,
	    " -none\t\tuse no compression algorithm on output\n");
	fprintf(stderr, "\n");
	fprintf(stderr,
	    " -2d\t\tuse 2d encoding when compressing with Group 3\n");
	fprintf(stderr,
	    " -fill\t\tzero-fill scanlines when compressing with Group 3\n");
	fprintf(stderr, "\n");
	fprintf(stderr,
	    " -rowsperstrip #\tmake each strip have no more than # rows\n");
	fprintf(stderr, " -predictor\tset LZW predictor value\n");
	fprintf(stderr, " -ignore\tignore read errors\n");
	exit(-1);
}
