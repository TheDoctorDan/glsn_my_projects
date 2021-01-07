#ifndef lint
static char rcsid[] = "$Header: /usr/people/sam/tiff/tools/RCS/tiffinfo.c,v 1.11 92/02/10 19:04:25 sam Exp $";
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

int	showdata = 0;			/* show data */
int	readdata = 0;			/* read data in file */
int	stoponerr = 0;			/* stop on first read error */

usage(code)
	int code;
{
	fprintf(stderr, "Usage: tiffinfo [-cdDSjs] [-#] TIFF-files\n");
	fprintf(stderr, "-D    read data\n");
	fprintf(stderr, "-S    stop reading data on 1st read error\n");
	fprintf(stderr, "-d    show data\n");
	fprintf(stderr, "-c    show color/gray response curves and colormap\n");
	fprintf(stderr, "-s    show data strip offsets and byte counts\n");
	fprintf(stderr, "-j    show JPEG tables\n");
	fprintf(stderr, "-#    show directory number #\n");
	exit(code);
}

main(argc, argv)
	char *argv[];
{
	int dirnum = -1, multiplefiles, c;
	int verbose = 0;
	int order = 0;
	char *cp;
	TIFF *tif;
	extern int optind;
	long flags = 0;

	while ((c = getopt(argc, argv, "cdDSjlmsv0123456789")) != -1)
		switch (c) {
		case '0': case '1': case '2': case '3':
		case '4': case '5': case '6': case '7':
		case '8': case '9':
			dirnum = atoi(argv[optind]);
			break;
		case 'd':
			showdata++;
			/* fall thru... */
		case 'D':
			readdata++;
			break;
		case 'S':
			stoponerr = 1;
			break;
		case 'c':
			flags |= TIFFPRINT_COLORMAP | TIFFPRINT_CURVES;
			break;
		case 'l':
			order = FILLORDER_LSB2MSB;
			break;
		case 'm':
			order = FILLORDER_MSB2LSB;
			break;
		case 's':
			flags |= TIFFPRINT_STRIPS;
			break;
		case 'j':
			flags |= TIFFPRINT_JPEGQTABLES |
				 TIFFPRINT_JPEGACTABLES |
				 TIFFPRINT_JPEGDCTABLES;
			break;
		case 'v':
			verbose = 1;
			break;
		case '?':
			usage(-1);
			/*NOTREACHED*/
		}
	if (optind >= argc)
		usage(-2);
	multiplefiles = (argc - optind > 1);
	for (; optind < argc; optind++) {
		if (multiplefiles || verbose)
			printf("%s:\n", argv[optind]);
		tif = TIFFOpen(argv[optind], "r");
		if (tif != NULL) {
			if (dirnum == -1) {
				do {
					if (order)
						TIFFSetField(tif, TIFFTAG_FILLORDER, order);
					TIFFPrintDirectory(tif, stdout, flags);
					if (readdata)
						TIFFReadData(tif);
				} while (TIFFReadDirectory(tif));
			} else {
				if (TIFFSetDirectory(tif, dirnum)) {
					if (order)
						TIFFSetField(tif, TIFFTAG_FILLORDER, order);
					TIFFPrintDirectory(tif, stdout, flags);
					if (readdata)
						TIFFReadData(tif);
				}
			}
			TIFFClose(tif);
		}
	}
	exit(0);
}

static
ShowStrip(strip, pp, nrow, scanline)
	int strip;
	register u_char *pp;
	u_long nrow;
	int scanline;
{
	register int cc;

	printf("Strip %u:\n", strip);
	while (nrow-- > 0) {
		for (cc = 0; cc < scanline; cc++) {
			printf(" %02x", *pp++);
			if (((cc+1) % 24) == 0)
				putchar('\n');
		}
		putchar('\n');
	}
}

TIFFReadContigStripData(tif)
	TIFF *tif;
{
	u_char *buf;
	int scanline = TIFFScanlineSize(tif);

	buf = (u_char *)malloc(TIFFStripSize(tif));
	if (buf) {
		u_long row, h;
		u_long rowsperstrip = (u_long)-1;

		TIFFGetField(tif, TIFFTAG_IMAGELENGTH, &h);
		TIFFGetField(tif, TIFFTAG_ROWSPERSTRIP, &rowsperstrip);
		for (row = 0; row < h; row += rowsperstrip) {
			u_long nrow = (row+rowsperstrip > h ?
			    h-row : rowsperstrip);
			u_int strip = TIFFComputeStrip(tif, row, 0);
			if (TIFFReadEncodedStrip(tif, strip, buf, nrow*scanline) < 0) {
				if (stoponerr)
					break;
			} else if (showdata)
				ShowStrip(strip, buf, nrow, scanline);
		}
		free(buf);
	}
}

TIFFReadSeparateStripData(tif)
	TIFF *tif;
{
	u_char *buf;
	int scanline = TIFFScanlineSize(tif);

	buf = (u_char *)malloc(TIFFStripSize(tif));
	if (buf) {
		u_long row, h;
		u_long rowsperstrip = (u_long)-1;
		u_short s, samplesperpixel;

		TIFFGetField(tif, TIFFTAG_IMAGELENGTH, &h);
		TIFFGetField(tif, TIFFTAG_ROWSPERSTRIP, &rowsperstrip);
		TIFFGetField(tif, TIFFTAG_SAMPLESPERPIXEL, &samplesperpixel);
		for (row = 0; row < h; row += rowsperstrip) {
			for (s = 0; s < samplesperpixel; s++) {
				u_long nrow = (row+rowsperstrip > h ?
				    h-row : rowsperstrip);
				u_int strip = TIFFComputeStrip(tif, row, s);
				if (TIFFReadEncodedStrip(tif, strip, buf, nrow*scanline) < 0) {
					if (stoponerr)
						break;
				} else if (showdata)
					ShowStrip(strip, buf, nrow, scanline);
			}
		}
		free(buf);
	}
}

static
ShowTile(row, col, sample, pp, nrow, rowsize)
	u_long row, col;
	int sample;
	register u_char *pp;
	int nrow, rowsize;
{
	register int cc;

	printf("Tile (%lu,%lu", row, col);
	if (sample > 0)
		printf(",%u", sample);
	printf("):\n");
	while (nrow-- > 0) {
		for (cc = 0; cc < rowsize; cc++) {
			printf(" %02x", *pp++);
			if (((cc+1) % 24) == 0)
				putchar('\n');
		}
		putchar('\n');
	}
}

TIFFReadContigTileData(tif)
	TIFF *tif;
{
	u_char *buf;
	int rowsize = TIFFTileRowSize(tif);

	buf = (u_char *)malloc(TIFFTileSize(tif));
	if (buf) {
		u_long tw, th, w, h;
		u_long row, col;

		TIFFGetField(tif, TIFFTAG_IMAGEWIDTH, &w);
		TIFFGetField(tif, TIFFTAG_IMAGELENGTH, &h);
		TIFFGetField(tif, TIFFTAG_TILEWIDTH, &tw);
		TIFFGetField(tif, TIFFTAG_TILELENGTH, &th);
		for (row = 0; row < h; row += th) {
			for (col = 0; col < w; col += tw) {
				if (TIFFReadTile(tif, buf, col, row, 0, 0) < 0) {
					if (stoponerr)
						break;
				} else if (showdata)
					ShowTile(row, col, -1, buf, th, rowsize);
			}
		}
		free(buf);
	}
}

TIFFReadSeparateTileData(tif)
	TIFF *tif;
{
	u_char *buf;
	int rowsize = TIFFTileRowSize(tif);
	u_short samplesperpixel;

	buf = (u_char *)malloc(TIFFTileSize(tif));
	if (buf) {
		u_long tw, th, w, h;
		u_long row, col;
		u_short s, samplesperpixel;

		TIFFGetField(tif, TIFFTAG_IMAGEWIDTH, &w);
		TIFFGetField(tif, TIFFTAG_IMAGELENGTH, &h);
		TIFFGetField(tif, TIFFTAG_TILEWIDTH, &tw);
		TIFFGetField(tif, TIFFTAG_TILELENGTH, &th);
		TIFFGetField(tif, TIFFTAG_SAMPLESPERPIXEL, &samplesperpixel);
		for (row = 0; row < h; row += th) {
			for (col = 0; col < w; col += tw) {
				for (s = 0; s < samplesperpixel; s++) {
					if (TIFFReadTile(tif, buf, col, row, 0, 0) < 0) {
						if (stoponerr)
							break;
					} else if (showdata)
						ShowTile(row, col, s, buf, th, rowsize);
				}
			}
		}
		free(buf);
	}
}

TIFFReadData(tif)
	TIFF *tif;
{
	u_short config;

	TIFFGetField(tif, TIFFTAG_PLANARCONFIG, &config);
	if (TIFFIsTiled(tif)) {
		if (config == PLANARCONFIG_CONTIG)
			TIFFReadContigTileData(tif);
		else
			TIFFReadSeparateTileData(tif);
	} else {
		if (config == PLANARCONFIG_CONTIG)
			TIFFReadContigStripData(tif);
		else
			TIFFReadSeparateStripData(tif);
	}
}
