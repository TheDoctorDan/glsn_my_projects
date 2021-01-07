#ifndef lint
static char rcsid[] = "$Header: /usr/people/sam/tiff/tools/RCS/ppm2tiff.c,v 1.5 92/02/10 19:04:14 sam Exp $";
#endif

/*
 * Copyright (c) 1991, 1992 Sam Leffler
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
typedef	unsigned long u_long;
#endif

#define	howmany(x, y)	(((x)+((y)-1))/(y))
#define	streq(a,b)	(strcmp(a,b) == 0)

u_short	compression = -1;
u_short	photometric;
u_long	rowsperstrip = (u_long) -1;

static
BadPPM(file)
	char *file;
{
	fprintf(stderr, "%s: Not a PPM file.\n", file);
	exit(-2);
}

main(argc, argv)
	char *argv[];
{
	u_char *buf;
	int row, linebytes, spp;
	TIFF *out;
	FILE *in;
	int c, w, h, prec;
	char *infile;

	argc--, argv++;
	if (argc < 1)
		usage();
	for (; argc > 1 && argv[0][0] == '-'; argc--, argv++) {
		if (streq(argv[0], "-none")) {
			compression = COMPRESSION_NONE;
			continue;
		}
		if (streq(argv[0], "-packbits")) {
			compression = COMPRESSION_PACKBITS;
			continue;
		}
		if (streq(argv[0], "-lzw")) {
			compression = COMPRESSION_LZW;
			continue;
		}
		if (streq(argv[0], "-rowsperstrip")) {
			argc--, argv++;
			rowsperstrip = atoi(argv[0]);
			continue;
		}
		usage();
	}

	/*
	 * If only one file is specified, read input from
	 * stdin; otherwise usage is: ppm2tiff input output.
	 */
	if (argc > 1) {
		infile = argv[0];
		argv++;
		in = fopen(infile, "r");
		if (in == NULL) {
			fprintf(stderr, "%s: Can not open.\n", infile);
			exit(-1);
		}
	} else {
		infile = "<stdin>";
		in = stdin;
	}

	if (getc(in) != 'P')
		BadPPM(infile);
	switch (c = getc(in)) {
	case '5':			/* it's a PGM file */
		spp = 1;
		photometric = PHOTOMETRIC_MINISBLACK;
		break;
	case '6':			/* it's a PPM file */
		spp = 3;
		photometric = PHOTOMETRIC_RGB;
		break;
	default:
		BadPPM(infile);
	}
	if (fscanf(in, " %d %d %d", &w, &h, &prec) != 3)
		BadPPM(infile);
	if (getc(in) != '\n' || w <= 0 || h <= 0 || prec != 255)
		BadPPM(infile);

	out = TIFFOpen(argv[0], "w");
	if (out == NULL)
		exit(-4);
	TIFFSetField(out, TIFFTAG_IMAGEWIDTH, (u_long) w);
	TIFFSetField(out, TIFFTAG_IMAGELENGTH, (u_long) h);
	TIFFSetField(out, TIFFTAG_ORIENTATION, ORIENTATION_TOPLEFT);
	TIFFSetField(out, TIFFTAG_SAMPLESPERPIXEL, spp);
	TIFFSetField(out, TIFFTAG_BITSPERSAMPLE, 8);
	TIFFSetField(out, TIFFTAG_PLANARCONFIG, PLANARCONFIG_CONTIG);
	TIFFSetField(out, TIFFTAG_PHOTOMETRIC, photometric);
	if (compression == (u_short)-1)
		compression = COMPRESSION_LZW;
	TIFFSetField(out, TIFFTAG_COMPRESSION, compression);
	linebytes = spp * w;
	if (TIFFScanlineSize(out) > linebytes)
		buf = (u_char *)malloc(linebytes);
	else
		buf = (u_char *)malloc(TIFFScanlineSize(out));
	if (rowsperstrip == (u_long)-1)
		rowsperstrip = (8*1024)/linebytes;
	TIFFSetField(out, TIFFTAG_ROWSPERSTRIP,
	    rowsperstrip == 0 ? 1L : rowsperstrip);
	for (row = 0; row < h; row++) {
		if (fread(buf, linebytes, 1, in) != 1) {
			fprintf(stderr, "%s: scanline %d: Read error.\n",
			    infile, row);
			break;
		}
		if (TIFFWriteScanline(out, buf, row, 0) < 0)
			break;
	}
	(void) TIFFClose(out);
}

usage()
{
	fprintf(stderr, "usage: ppm2tif [options] [input] output\n");
	fprintf(stderr, "where options are:\n");
	fprintf(stderr,
	    " -lzw\t\tcompress output with Lempel-Ziv & Welch encoding\n");
	fprintf(stderr,
	    " -packbits\tcompress output with packbits encoding\n");
	fprintf(stderr,
	    " -none\t\tuse no compression algorithm on output\n");
	fprintf(stderr, "\n");
	fprintf(stderr,
	    " -rowsperstrip #\tmake each strip have no more than # rows\n");
	exit(-1);
}
