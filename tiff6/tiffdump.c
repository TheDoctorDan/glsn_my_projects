#ifndef lint
static char rcsid[] = "$Header: /usr/people/sam/tiff/tools/RCS/tiffdump.c,v 1.22 92/03/24 14:11:26 sam Exp $";
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
#if defined(VMS)
#include <unixio.h>
#include <file.h>
#else
#include <fcntl.h>
#endif
#if defined(MSDOS)
#include <malloc.h>
#else
#define	O_BINARY	0
#endif
#include "tiff.h"

#define	bcopy(src,dst,len)	memcpy(dst, src, len)

char	*curfile;
int	swabflag;
int	bigendian;
int	typeshift[13];	/* data type shift counts */
long	typemask[13];	/* data type masks */
long	ReadDirectory();

main(argc, argv)
	char *argv[];
{
	int one = 1, fd;
	int multiplefiles = (argc > 1);

	bigendian = (*(char *)&one == 0);
	for (argc--, argv++; argc > 0; argc--, argv++) {
		fd = open(argv[0], O_RDONLY|O_BINARY, 0);
		if (fd < 0) {
			perror(argv[0]);
			exit(-1);
		}
		if (multiplefiles)
			printf("%s:\n", argv[0]);
		curfile = *argv;
		swabflag = 0;
		dump(fd);
		close(fd);
	}
	exit(0);
}

TIFFHeader h;

#define	ord(e)	((int)e)

/*
 * Initialize shift & mask tables and byte
 * swapping state according to the file
 * byte order.
 */
static
InitByteOrder(magic)
	int magic;
{
	typemask[0] = 0;
	typemask[ord(TIFF_BYTE)] = 0xff;
	typemask[ord(TIFF_SBYTE)] = 0xff;
	typemask[ord(TIFF_UNDEFINED)] = 0xff;
	typemask[ord(TIFF_SHORT)] = 0xffff;
	typemask[ord(TIFF_SSHORT)] = 0xffff;
	typemask[ord(TIFF_LONG)] = 0xffffffff;
	typemask[ord(TIFF_SLONG)] = 0xffffffff;
	typemask[ord(TIFF_RATIONAL)] = 0xffffffff;
	typemask[ord(TIFF_SRATIONAL)] = 0xffffffff;
	typemask[ord(TIFF_FLOAT)] = 0xffffffff;
	typemask[ord(TIFF_DOUBLE)] = 0xffffffff;
	typeshift[0] = 0;
	typeshift[ord(TIFF_LONG)] = 0;
	typeshift[ord(TIFF_SLONG)] = 0;
	typeshift[ord(TIFF_RATIONAL)] = 0;
	typeshift[ord(TIFF_SRATIONAL)] = 0;
	typeshift[ord(TIFF_FLOAT)] = 0;
	typeshift[ord(TIFF_DOUBLE)] = 0;
	if (magic == TIFF_BIGENDIAN) {
		typeshift[ord(TIFF_BYTE)] = 24;
		typeshift[ord(TIFF_SBYTE)] = 24;
		typeshift[ord(TIFF_SHORT)] = 16;
		typeshift[ord(TIFF_SSHORT)] = 16;
		swabflag = !bigendian;
	} else {
		typeshift[ord(TIFF_BYTE)] = 0;
		typeshift[ord(TIFF_SBYTE)] = 0;
		typeshift[ord(TIFF_SHORT)] = 0;
		typeshift[ord(TIFF_SSHORT)] = 0;
		swabflag = bigendian;
	}
}

dump(fd)
	int fd;
{
	long off;
	int i;

	lseek(fd, 0L, 0);
	if (read(fd, &h, sizeof (h)) != sizeof (h))
		ReadError("TIFF header");
	/*
	 * Setup the byte order handling.
	 */
	if (h.tiff_magic != TIFF_BIGENDIAN && h.tiff_magic != TIFF_LITTLEENDIAN)
		Fatal("Not a TIFF file, bad magic number %u (0x%x)",
		    h.tiff_magic, h.tiff_magic);
	InitByteOrder(h.tiff_magic);
	/*
	 * Swap header if required.
	 */
	if (swabflag) {
		TIFFSwabShort(&h.tiff_version);
		TIFFSwabLong(&h.tiff_diroff);
	}
	/*
	 * Now check version (if needed, it's been byte-swapped).
	 * Note that this isn't actually a version number, it's a
	 * magic number that doesn't change (stupid).
	 */
	if (h.tiff_version != TIFF_VERSION)
		Fatal("Not a TIFF file, bad version number %u (0x%x)",
		    h.tiff_version, h.tiff_version); 
	printf("Magic: 0x%x <%s-endian> Version: 0x%x\n",
	    h.tiff_magic,
	    h.tiff_magic == TIFF_BIGENDIAN ? "big" : "little",
	    h.tiff_version);
	i = 0;
	off = h.tiff_diroff;
	while (off) {
		if (i > 0)
			putchar('\n');
		printf("Directory %d: offset %lu (0x%lx)\n", i++, off, off);
		off = ReadDirectory(fd, off);
	}
}

static int datawidth[] = {
    0,	/* nothing */
    1,	/* TIFF_BYTE */
    1,	/* TIFF_ASCII */
    2,	/* TIFF_SHORT */
    4,	/* TIFF_LONG */
    8,	/* TIFF_RATIONAL */
    1,	/* TIFF_SBYTE */
    1,	/* TIFF_UNDEFINED */
    2,	/* TIFF_SSHORT */
    4,	/* TIFF_SLONG */
    8,	/* TIFF_SRATIONAL */
    4,	/* TIFF_FLOAT */
    8,	/* TIFF_DOUBLE */
};
#define	NWIDTHS	(sizeof (datawidth) / sizeof (datawidth[0]))
static	int TIFFFetchData();

/*
 * Read the next TIFF directory from a file
 * and convert it to the internal format.
 * We read directories sequentially.
 */
long
ReadDirectory(fd, off)
	int fd;
	long off;
{
	register TIFFDirEntry *dp;
	register int n;
	TIFFDirEntry *dir = 0;
	unsigned short dircount;
	char *cp;
	int space;
	long nextdiroff = 0;

	if (off == 0)			/* no more directories */
		goto done;
	if (lseek(fd, off, 0) != off) {
		Fatal("Seek error accessing TIFF directory");
		goto done;
	}
	if (read(fd, &dircount, sizeof (short)) != sizeof (short)) {
		ReadError("directory count");
		goto done;
	}
	if (swabflag)
		TIFFSwabShort(&dircount);
	dir = (TIFFDirEntry *)malloc(dircount * sizeof (TIFFDirEntry));
	if (dir == NULL) {
		Fatal("No space for TIFF directory");
		goto done;
	}
	n = read(fd, dir, dircount*sizeof (*dp));
	if (n != dircount*sizeof (*dp)) {
		n /= sizeof (*dp);
		Error(
	    "Could only read %u of %u entries in directory at offset 0x%x",
		    n, dircount, off);
		dircount = n;
	}
	if (read(fd, &nextdiroff, sizeof (long)) != sizeof (long))
		nextdiroff = 0;
	if (swabflag)
		TIFFSwabLong(&nextdiroff);
	for (dp = dir, n = dircount; n > 0; n--, dp++) {
		if (swabflag) {
			TIFFSwabArrayOfShort(&dp->tdir_tag, 2);
			TIFFSwabArrayOfLong(&dp->tdir_count, 2);
		}
		PrintTag(stdout, dp->tdir_tag);
		putchar(' ');
		PrintType(stdout, dp->tdir_type);
		putchar(' ');
		printf("%u<", dp->tdir_count);
		if (dp->tdir_type >= NWIDTHS) {
			printf(">\n");
			continue;
		}
		space = dp->tdir_count * datawidth[dp->tdir_type];
		if (space <= 4) {
			switch (dp->tdir_type) {
			case TIFF_ASCII: {
				char data[4];
				bcopy(&dp->tdir_offset, data, 4);
				if (swabflag)
					TIFFSwabLong(data);
				PrintData(stdout,
				    dp->tdir_type, dp->tdir_count, data);
				break;
			}
			case TIFF_BYTE:
			case TIFF_SBYTE:
				if (h.tiff_magic == TIFF_LITTLEENDIAN) {
					switch ((int)dp->tdir_count) {
					case 4:
						printf("0x%02x",
						    dp->tdir_offset&0xff);
					case 3:
						printf("0x%02x",
						    (dp->tdir_offset>>8)&0xff);
					case 2:
						printf("0x%02x",
						    (dp->tdir_offset>>16)&0xff);
					case 1:
						printf("0x%02x",
						    dp->tdir_offset>>24);
						break;
					}
				} else {
					switch ((int)dp->tdir_count) {
					case 4:
						printf("0x%02x",
						    dp->tdir_offset>>24);
					case 3:
						printf("0x%02x",
						    (dp->tdir_offset>>16)&0xff);
					case 2:
						printf("0x%02x",
						    (dp->tdir_offset>>8)&0xff);
					case 1:
						printf("0x%02x",
						    dp->tdir_offset&0xff);
						break;
					}
				}
				break;
			case TIFF_SHORT:
			case TIFF_SSHORT:
				if (h.tiff_magic == TIFF_LITTLEENDIAN) {
					switch (dp->tdir_count) {
					case 2:
						printf("%u ",
						    dp->tdir_offset&0xffff);
					case 1:
						printf("%u",
						    dp->tdir_offset>>16);
						break;
					}
				} else {
					switch (dp->tdir_count) {
					case 2:
						printf("%u ",
						    dp->tdir_offset>>16);
					case 1:
						printf("%u",
						    dp->tdir_offset&0xffff);
						break;
					}
				}
				break;
			case TIFF_LONG:
				printf("%lu", dp->tdir_offset);
				break;
			case TIFF_SLONG:
				printf("%ld", dp->tdir_offset);
				break;
			}
		} else {
			char *data = (char *)malloc(space);
			if (data) {
				if (TIFFFetchData(fd, dp, data))
					PrintData(stdout, dp->tdir_type,
					    dp->tdir_count, data);
				free(data);
			} else
				Error("No space for data for tag %u",
				    dp->tdir_tag);
		}
		printf(">\n");
	}
done:
	if (dir)
		free((char *)dir);
	return (nextdiroff);
}

static	struct tagname {
	int	tag;
	char	*name;
} tagnames[] = {
    { TIFFTAG_SUBFILETYPE,	"SubFileType" },
    { TIFFTAG_OSUBFILETYPE,	"OldSubFileType" },
    { TIFFTAG_IMAGEWIDTH,	"ImageWidth" },
    { TIFFTAG_IMAGELENGTH,	"ImageLength" },
    { TIFFTAG_BITSPERSAMPLE,	"BitsPerSample" },
    { TIFFTAG_COMPRESSION,	"Compression" },
    { TIFFTAG_PHOTOMETRIC,	"Photometric" },
    { TIFFTAG_THRESHHOLDING,	"Threshholding" },
    { TIFFTAG_CELLWIDTH,	"CellWidth" },
    { TIFFTAG_CELLLENGTH,	"CellLength" },
    { TIFFTAG_FILLORDER,	"FillOrder" },
    { TIFFTAG_DOCUMENTNAME,	"DocumentName" },
    { TIFFTAG_IMAGEDESCRIPTION,	"ImageDescription" },
    { TIFFTAG_MAKE,		"Make" },
    { TIFFTAG_MODEL,		"Model" },
    { TIFFTAG_STRIPOFFSETS,	"StripOffsets" },
    { TIFFTAG_ORIENTATION,	"Orientation" },
    { TIFFTAG_SAMPLESPERPIXEL,	"SamplesPerPixel" },
    { TIFFTAG_ROWSPERSTRIP,	"RowsPerStrip" },
    { TIFFTAG_STRIPBYTECOUNTS,	"StripByteCounts" },
    { TIFFTAG_MINSAMPLEVALUE,	"MinSampleValue" },
    { TIFFTAG_MAXSAMPLEVALUE,	"MaxSampleValue" },
    { TIFFTAG_XRESOLUTION,	"XResolution" },
    { TIFFTAG_YRESOLUTION,	"YResolution" },
    { TIFFTAG_PLANARCONFIG,	"PlanarConfig" },
    { TIFFTAG_PAGENAME,		"PageName" },
    { TIFFTAG_XPOSITION,	"XPosition" },
    { TIFFTAG_YPOSITION,	"YPosition" },
    { TIFFTAG_FREEOFFSETS,	"FreeOffsets" },
    { TIFFTAG_FREEBYTECOUNTS,	"FreeByteCounts" },
    { TIFFTAG_GRAYRESPONSEUNIT,	"GrayResponseUnit" },
    { TIFFTAG_GRAYRESPONSECURVE,"GrayResponseCurve" },
    { TIFFTAG_GROUP3OPTIONS,	"Group3Options" },
    { TIFFTAG_GROUP4OPTIONS,	"Group4Options" },
    { TIFFTAG_RESOLUTIONUNIT,	"ResolutionUnit" },
    { TIFFTAG_PAGENUMBER,	"PageNumber" },
    { TIFFTAG_COLORRESPONSEUNIT,"ColorResponseUnit" },
    { TIFFTAG_TRANSFERFUNCTION,	"TransferFunction" },
    { TIFFTAG_SOFTWARE,		"Software" },
    { TIFFTAG_DATETIME,		"DateTime" },
    { TIFFTAG_ARTIST,		"Artist" },
    { TIFFTAG_HOSTCOMPUTER,	"HostComputer" },
    { TIFFTAG_PREDICTOR,	"Predictor" },
    { TIFFTAG_WHITEPOINT,	"Whitepoint" },
    { TIFFTAG_PRIMARYCHROMATICITIES,"PrimaryChromaticities" },
    { TIFFTAG_COLORMAP,		"Colormap" },
    { TIFFTAG_HALFTONEHINTS,	"HalftoneHints" },
    { TIFFTAG_TILEWIDTH,	"TileWidth" },
    { TIFFTAG_TILELENGTH,	"TileLength" },
    { TIFFTAG_TILEOFFSETS,	"TileOffsets" },
    { TIFFTAG_TILEBYTECOUNTS,	"TileByteCounts" },
    { TIFFTAG_BADFAXLINES,	"BadFaxLines" },
    { TIFFTAG_CLEANFAXDATA,	"CleanFaxData" },
    { TIFFTAG_CONSECUTIVEBADFAXLINES, "ConsecutiveBadFaxLines" },
    { TIFFTAG_INKSET,		"InkSet" },
    { TIFFTAG_INKNAMES,		"InkNames" },
    { TIFFTAG_DOTRANGE,		"DotRange" },
    { TIFFTAG_TARGETPRINTER,	"TargetPrinter" },
    { TIFFTAG_EXTRASAMPLES,	"ExtraSamples" },
    { TIFFTAG_SAMPLEFORMAT,	"SampleFormat" },
    { TIFFTAG_SMINSAMPLEVALUE,	"SMinSampleValue" },
    { TIFFTAG_SMAXSAMPLEVALUE,	"SMaxSampleValue" },
    { TIFFTAG_JPEGPROC,		"JPEGProcessingMode" },
    { TIFFTAG_JPEGIFOFFSET,	"JPEGInterchangeFormat" },
    { TIFFTAG_JPEGIFBYTECOUNT,	"JPEGInterchangeFormatLength" },
    { TIFFTAG_JPEGRESTARTINTERVAL,"JPEGRestartInterval" },
    { TIFFTAG_JPEGLOSSLESSPREDICTORS,"JPEGLosslessPredictors" },
    { TIFFTAG_JPEGPOINTTRANSFORM,"JPEGPointTransform" },
    { TIFFTAG_JPEGQTABLES,	"JPEGQTables" },
    { TIFFTAG_JPEGDCTABLES,	"JPEGDCTables" },
    { TIFFTAG_JPEGACTABLES,	"JPEGACTables" },
    { TIFFTAG_YCBCRCOEFFICIENTS,"YCbCrCoefficients" },
    { TIFFTAG_YCBCRSUBSAMPLING,	"YCbCrSubsampling" },
    { TIFFTAG_YCBCRPOSITIONING,	"YCbCrPositioning" },
    { TIFFTAG_REFERENCEBLACKWHITE, "ReferenceBlackWhite" },
    { TIFFTAG_MATTEING,		"OBSOLETE Matteing" },
    { TIFFTAG_DATATYPE,		"OBSOLETE DataType" },
    { TIFFTAG_IMAGEDEPTH,	"ImageDepth" },
    { TIFFTAG_TILEDEPTH,	"TileDepth" },
    { 32768,			"OLD BOGUS Matteing tag" },
};
#define	NTAGS	(sizeof (tagnames) / sizeof (tagnames[0]))

PrintTag(fd, tag)
	FILE *fd;
	unsigned short tag;
{
	register struct tagname *tp;

	for (tp = tagnames; tp < &tagnames[NTAGS]; tp++)
		if (tp->tag == tag) {
			fprintf(fd, "%s (%u)", tp->name, tag);
			return;
		}
	fprintf(fd, "%u (0x%x)", tag, tag);
}

PrintType(fd, type)
	FILE *fd;
	unsigned short type;
{
	static char *typenames[] = {
	    "0",
	    "BYTE",
	    "ASCII",
	    "SHORT",
	    "LONG",
	    "RATIONAL",
	    "SBYTE",
	    "UNDEFINED",
	    "SSHORT",
	    "SLONG",
	    "SRATIONAL",
	    "FLOAT",
	    "DOUBLE"
	};
#define	NTYPES	(sizeof (typenames) / sizeof (typenames[0]))

	if (type < NTYPES)
		fprintf(fd, "%s (%u)", typenames[type], type);
	else
		fprintf(fd, "%u (0x%x)", type, type);
}
#undef	NTYPES

PrintData(fd, type, count, data)
	FILE *fd;
	unsigned short type;
	long count;
	unsigned char *data;
{

	switch (type) {
	case TIFF_BYTE:
		while (count-- > 0)
			fprintf(fd, "%u%s", *data++, count > 0 ? " " : "");
		break;
	case TIFF_SBYTE:
		while (count-- > 0)
			fprintf(fd, "%d%s", *(char *)data++, count > 0 ? " " : "");
		break;
	case TIFF_UNDEFINED:
		while (count-- > 0)
			fprintf(fd, "0x%02x", *data++);
		break;
	case TIFF_ASCII:
		fprintf(fd, "%.*s", count, data);
		break;
	case TIFF_SHORT: {
		register unsigned short *wp = (unsigned short *)data;
		while (count-- > 0)
			fprintf(fd, "%u%s", *wp++, count > 0 ? " " : "");
		break;
	}
	case TIFF_SSHORT: {
		register short *wp = (short *)data;
		while (count-- > 0)
			fprintf(fd, "%d%s", *wp++, count > 0 ? " " : "");
		break;
	}
	case TIFF_LONG: {
		register unsigned long *lp = (unsigned long *)data;
		while (count-- > 0)
			fprintf(fd, "%lu%s", *lp++, count > 0 ? " " : "");
		break;
	}
	case TIFF_SLONG: {
		register long *lp = (long *)data;
		while (count-- > 0)
			fprintf(fd, "%ld%s", *lp++, count > 0 ? " " : "");
		break;
	}
	case TIFF_RATIONAL: {
		register unsigned long *lp = (unsigned long *)data;
		while (count-- > 0) {
			if (lp[1] == 0)
				fprintf(fd, "Nan (%lu/%lu)", lp[0], lp[1]);
			else
				fprintf(fd, "%g",
				    (double)lp[0] / (double)lp[1]);
			if (count > 0)
				fprintf(fd, " ");
			lp += 2;
		}
		break;
	}
	case TIFF_SRATIONAL: {
		register long *lp = (long *)data;
		while (count-- > 0) {
			if (lp[1] == 0)
				fprintf(fd, "Nan (%ld/%ld)", lp[0], lp[1]);
			else
				fprintf(fd, "%g",
				    (double)lp[0] / (double)lp[1]);
			if (count > 0)
				fprintf(fd, " ");
			lp += 2;
		}
		break;
	}
	case TIFF_FLOAT: {
		register float *fp = (float *)data;
		while (count-- > 0)
			fprintf(fd, "%g%s", *fp++, count > 0 ? " " : "");
		break;
	}
	case TIFF_DOUBLE: {
		register double *dp = (double *)data;
		while (count-- > 0)
			fprintf(fd, "%g%s", *dp++, count > 0 ? " " : "");
		break;
	}
	}
}

TIFFSwabShort(wp)
	unsigned short *wp;
{
	register unsigned char *cp = (unsigned char *)wp;
	int t;

	t = cp[1]; cp[1] = cp[0]; cp[0] = t;
}

TIFFSwabLong(lp)
	unsigned long *lp;
{
	register unsigned char *cp = (unsigned char *)lp;
	int t;

	t = cp[3]; cp[3] = cp[0]; cp[0] = t;
	t = cp[2]; cp[2] = cp[1]; cp[1] = t;
}

TIFFSwabArrayOfShort(wp, n)
	unsigned short *wp;
	register int n;
{
	register unsigned char *cp;
	register int t;

	/*  unroll loop some */
	while (n-- > 0) {
		cp = (unsigned char *)wp;
		t = cp[1]; cp[1] = cp[0]; cp[0] = t;
		wp++;
	}
}

TIFFSwabArrayOfLong(lp, n)
	register unsigned long *lp;
	register int n;
{
	register unsigned char *cp;
	register int t;

	/*  unroll loop some */
	while (n-- > 0) {
		cp = (unsigned char *)lp;
		t = cp[3]; cp[3] = cp[0]; cp[0] = t;
		t = cp[2]; cp[2] = cp[1]; cp[1] = t;
		lp++;
	}
}

/*
 * Fetch a contiguous directory item.
 */
static
TIFFFetchData(fd, dir, cp)
	int fd;
	TIFFDirEntry *dir;
	char *cp;
{
	int cc, w;

	w = (dir->tdir_type < NWIDTHS ? datawidth[dir->tdir_type] : 0);
	cc = dir->tdir_count * w;
	if (lseek(fd, dir->tdir_offset, 0) == dir->tdir_offset &&
	    read(fd, cp, cc) == cc) {
		if (swabflag) {
			switch (dir->tdir_type) {
			case TIFF_SHORT:
			case TIFF_SSHORT:
				TIFFSwabArrayOfShort(cp, dir->tdir_count);
				break;
			case TIFF_LONG:
			case TIFF_SLONG:
				TIFFSwabArrayOfLong(cp, dir->tdir_count);
				break;
			case TIFF_RATIONAL:
			case TIFF_DOUBLE:
				TIFFSwabArrayOfLong(cp, 2*dir->tdir_count);
				break;
			}
		}
		return (cc);
	}
	Error("Error while reading data for tag %u", dir->tdir_tag);
	return (0);
}

ReadError(what)
	char *what;
{
	Fatal("Error while reading %s", what);
}

Error(fmt, a1, a2, a3, a4, a5)
	char *fmt;
{
	fprintf(stderr, "%s: ", curfile);
	fprintf(stderr, fmt, a1, a2, a3, a4, a5);
	fprintf(stderr, ".\n");
}

Fatal(fmt, a1, a2, a3, a4, a5)
	char *fmt;
{
	Error(fmt, a1, a2, a3, a4, a5);
	exit(-1);
}
