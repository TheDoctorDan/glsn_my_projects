#ifndef lint
static char rcsid[] = "$Header: /usr/people/sam/tiff/tools/RCS/tiffsplit.c,v 1.1 92/02/10 18:42:56 sam Exp $";
#endif

/*
 * Copyright (c) 1992 Sam Leffler
 * Copyright (c) 1992 Silicon Graphics, Inc.
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

#define	streq(a,b)	(strcmp(a,b) == 0)
#define	CopyField(tag, v) \
    if (TIFFGetField(in, tag, &v)) TIFFSetField(out, tag, v)
#define	CopyField2(tag, v1, v2) \
    if (TIFFGetField(in, tag, &v1, &v2)) TIFFSetField(out, tag, v1, v2)
#define	CopyField3(tag, v1, v2, v3) \
    if (TIFFGetField(in, tag, &v1, &v2, &v3)) TIFFSetField(out, tag, v1, v2, v3)

static	int ignore = 1;			/* if true, ignore read errors */
static	char *filename;
static	char fname[1024+1];

static	int tiffcp();
static	newfilename();
static	int cpStrips();
static	int cpTiles();

main(argc, argv)
	char *argv[];
{
	TIFF *in, *out;

	if (argc < 2) {
		fprintf(stderr, "usage: tiffsplit input.tif [prefix]\n");
		exit(-3);
	}
	if (argc > 2)
		strcpy(fname, argv[2]);
	in = TIFFOpen(filename = argv[1], "r");
	if (in != NULL) {
		do {
			char path[1024+1];
			newfilename();
			strcpy(path, fname);
			strcat(path, ".tif");
			out = TIFFOpen(path, "w");
			if (out == NULL)
				exit(-2);
			if (!tiffcp(in, out))
				exit(-1);
			TIFFClose(out);
		} while (TIFFReadDirectory(in));
		(void) TIFFClose(in);
	}
	exit(0);
}

static
newfilename()
{
	static int first = 1;
	static long fnum;
	static short defname;
	static char *fpnt;

	if (first) {
		if (fname[0]) {
			fpnt = fname + strlen(fname);
			defname = 0;
		} else {
			fname[0] = 'x';
			fpnt = fname + 1;
			defname = 1;
		}
		first = 0;
	}
#define	MAXFILES	676
	if (fnum == MAXFILES) {
		if (!defname || fname[0] == 'z') {
			fprintf(stderr, "tiffsplit: too many files.\n");
			exit(1);
		}
		fname[0]++;
		fnum = 0;
	}
	fpnt[0] = fnum / 26 + 'a';
	fpnt[1] = fnum % 26 + 'a';
	fnum++;
}

static int
tiffcp(in, out)
	TIFF *in, *out;
{
	short bitspersample, samplesperpixel, shortv;
	u_long w, l;
	float floatv;
	char *stringv;
	u_long longv;

	CopyField(TIFFTAG_SUBFILETYPE, longv);
	CopyField(TIFFTAG_TILEWIDTH, w);
	CopyField(TIFFTAG_TILELENGTH, l);
	CopyField(TIFFTAG_IMAGEWIDTH, w);
	CopyField(TIFFTAG_IMAGELENGTH, l);
	CopyField(TIFFTAG_BITSPERSAMPLE, bitspersample);
	CopyField(TIFFTAG_COMPRESSION, shortv);
	CopyField(TIFFTAG_PREDICTOR, shortv);
	CopyField(TIFFTAG_PHOTOMETRIC, shortv);
	CopyField(TIFFTAG_THRESHHOLDING, shortv);
	CopyField(TIFFTAG_FILLORDER, shortv);
	CopyField(TIFFTAG_ORIENTATION, shortv);
	CopyField(TIFFTAG_SAMPLESPERPIXEL, samplesperpixel);
	CopyField(TIFFTAG_MINSAMPLEVALUE, shortv);
	CopyField(TIFFTAG_MAXSAMPLEVALUE, shortv);
	CopyField(TIFFTAG_XRESOLUTION, floatv);
	CopyField(TIFFTAG_YRESOLUTION, floatv);
	CopyField(TIFFTAG_GROUP3OPTIONS, longv);
	CopyField(TIFFTAG_GROUP4OPTIONS, longv);
	CopyField(TIFFTAG_RESOLUTIONUNIT, shortv);
	CopyField(TIFFTAG_PLANARCONFIG, shortv);
	CopyField(TIFFTAG_ROWSPERSTRIP, longv);
	CopyField(TIFFTAG_XPOSITION, floatv);
	CopyField(TIFFTAG_YPOSITION, floatv);
	CopyField(TIFFTAG_IMAGEDEPTH, longv);
	CopyField(TIFFTAG_TILEDEPTH, longv);
	CopyField(TIFFTAG_MATTEING, shortv);
	{ u_short *red, *green, *blue;
	  CopyField3(TIFFTAG_COLORMAP, red, green, blue);
	}
	{ u_short shortv2;
	  CopyField2(TIFFTAG_PAGENUMBER, shortv, shortv2);
	}
	CopyField(TIFFTAG_ARTIST, stringv);
	CopyField(TIFFTAG_IMAGEDESCRIPTION, stringv);
	CopyField(TIFFTAG_MAKE, stringv);
	CopyField(TIFFTAG_MODEL, stringv);
	CopyField(TIFFTAG_SOFTWARE, stringv);
	CopyField(TIFFTAG_DATETIME, stringv);
	CopyField(TIFFTAG_HOSTCOMPUTER, stringv);
	CopyField(TIFFTAG_PAGENAME, stringv);
	CopyField(TIFFTAG_DOCUMENTNAME, stringv);
	if (TIFFIsTiled(in))
		return (cpTiles(in, out));
	else
		return (cpStrips(in, out));
}

static int
cpStrips(in, out)
	TIFF *in, *out;
{
	u_long bufsize  = TIFFStripSize(in);
	u_char *buf = (u_char *)malloc(bufsize);

	if (buf) {
		u_int s, ns = TIFFNumberOfStrips(in);
		u_long *bytecounts;

		TIFFGetField(in, TIFFTAG_STRIPBYTECOUNTS, &bytecounts);
		for (s = 0; s < ns; s++) {
			if (bytecounts[s] > bufsize) {
				buf = (u_char *)realloc(buf, bytecounts[s]);
				if (!buf)
					return (0);
				bufsize = bytecounts[s];
			}
			if (TIFFReadRawStrip(in, s, buf, bytecounts[s]) < 0 ||
			    TIFFWriteRawStrip(out, s, buf, bytecounts[s]) < 0) {
				free(buf);
				return (0);
			}
		}
		free(buf);
		return (1);
	}
	return (0);
}

static int
cpTiles(in, out)
	TIFF *in, *out;
{
	u_long bufsize  = TIFFTileSize(in);
	u_char *buf = (u_char *)malloc(bufsize);

	if (buf) {
		u_int t, nt = TIFFNumberOfTiles(in);
		u_long *bytecounts;

		TIFFGetField(in, TIFFTAG_TILEBYTECOUNTS, &bytecounts);
		for (t = 0; t < nt; t++) {
			if (bytecounts[t] > bufsize) {
				buf = (u_char *)realloc(buf, bytecounts[t]);
				if (!buf)
					return (0);
				bufsize = bytecounts[t];
			}
			if (TIFFReadRawTile(in, t, buf, bytecounts[t]) < 0 ||
			    TIFFWriteRawTile(out, t, buf, bytecounts[t]) < 0) {
				free(buf);
				return (0);
			}
		}
		free(buf);
		return (1);
	}
	return (0);
}
