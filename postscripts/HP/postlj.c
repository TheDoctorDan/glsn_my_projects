/* LaserJet print driver for Post.  File "postlj.c"
 * (C) Adrian Aylward 1990, 1992
 *
 * You may freely copy, use, and modify this file.
 *
 * This program prints PostScript files to a LaserJet.  It sends the output
 * to the PAR: handler.  Page size and orientation are read from the command
 * line.  There are no printer status checks; if the output hangs check your
 * printer is ready.  It is totally Amiga specific.
 *
 * The program was tested using Lattice C V5.05.  It has various Lattice
 * dependencies.
 *
 * V1.6 First full source release
 * V1.7 New options for custom page size and DeskJet etc.
 */

#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#include <malloc.h>
#include <signal.h>

#include "postlib.h"

#undef  POSTVERNO
#define POSTVERNO 15  /* We need post.library version 1.5+ */


/* Routines defined and referenced only within this module */


/* External data (initialised to zero) */

int	retcode;

int	arec;

/* BPTR errfh; */
FILE	*errfh = NULL;
FILE *parfp;

/*
struct library *PSbase;
*/
struct PSparm parm;


/* Options */

#define DEFSIZE 3 /* A4 page size */
#define DEFLAND 0 /* Portrait orientation */
#define DEFLJ   2 /* LaserJet IIP,III,IIIp etc. */

#define MAXSIZE 8
#define MAXLJ   3

int	optsize = DEFSIZE;
int	optland = DEFLAND;
int	optlj   = DEFLJ;
int	optgc;
int	optcopies;
int	optbeg, optend;
int	optxsize, optysize;
int	optlmarg, optrmarg, optumarg, optdmarg;
int	opthoff, optvoff;

int	pagenum;
int	hoffset, voffset, vmarg;

/* Page size tables.
 * (See Figures 2-2 and 2-3 in the LaserJet 2P Technical Reference Manual.)
 *
 *                     Let   Legal Exec  A4    COM10 Mon   C5    DL
 */

static struct {
	char	*showsize;
	int	psize, xsize, ysize, ppoff, ploff
} LJ_PAGESIZE_TABLE[MAXSIZE] = {
	{ "letter", 	2, 	2550, 	3300, 	75, 	60 },
	{ "legal", 	3, 	2550, 	4200, 	75, 	60 },
	{ "executive", 	1, 	2175, 	3150, 	75, 	60 },
	{ "A4", 	26, 	2480, 	3507, 	71, 	59 },
	{ "COM-10", 	81, 	1237, 	2850, 	75, 	60 },
	{ "monarch", 	80, 	1162, 	2250, 	75, 	60 },
	{ "C5", 	91, 	1913, 	2704, 	71, 	59 },
	{ "DL", 	90, 	1299, 	2598, 	71, 	59 }
}; 



char	*showlj[MAXLJ] = { 
	"DeskJet", "LaserJet II", "LaserJet IIP/III" };


char	*showland[2] = { 
	"vertical", "horizontal" };


int	compsize;
char	*compbuf;

/* Arguments */

char	*argto = "par:";
/*
int	argfilec, argmemc;
char	*argfilev[5], *argmemv[5];
*/





/* Signal an interrupt */

void sigint(int a)
{
	PSsignalint(arec, 1);
}


/* Signal a floating point error */

void sigfpe(int a)
{
	PSsignalfpe(arec);
}



/* Printer setup */

void prtsetup(void)
{
	/* Printer reset, Page size, Orientation, Perf skip off, Top Mgn 0 */

	fprintf(parfp, "\33E\33&l%da%do0l0E", LJ_PAGESIZE_TABLE[optsize].psize, optland);

	/* Long edge offset, Short edge offset */

	if (optland)
		fprintf(parfp, "\33&l%du%dZ",  voffset, hoffset);
	else
		fprintf(parfp, "\33&l%du%dZ", -hoffset, voffset);
}


/* Printer reset */

void prtreset(void)
{
	fprintf(parfp, "\33E");
}


/* Dump a line of pixels */

void prtdumpline(char *buf, int len)
{
	char	*ptr;
	int	b, c, l;

	/* Strip trailing zeros */

	while (len && buf[len - 1] == 0)
		len--;

	/* Compression */

	if (optgc) {
		ptr = compbuf;
		l = 0;
		while (len--) {
			b = *buf++;                  /* Pick up a byte */
			c = 1;
			while (len && *buf == b && c < 128) {
				c++;
				buf++;
				len--;                   /* See if it begins a run */
			}
			if (c == 2 &&                /* If a two byte run */
			l > 0 &&                 /*  and preceeded by literals */
			l <= 125 &&              /*  and not more than 125 of them */
			(len <= 2 ||             /*  and no more than 2 bytes left */
			*buf != *(buf + 1)))    /*      or not followed by a run */ {
				c = 1;                   /* Then make it a literal */
				buf--;
				len++;
			}
			if (c == 1)                  /* If not a run */ {
				l++;                     /* Then it must be a literal */
				c = 0;
			}
			if (l > 0 &&                 /* If we have some literals */
			(c > 1 ||                /*  and beginning a run */
			l == 127 ||             /*  or reached 127 */
			len == 0))              /*  or no more bytes left */ {
				*ptr++ = l - 1;          /* Then write out the literals */
				memcpy(ptr, buf - c - l, l);
				ptr += l;
				l = 0;
			}
			if (c > 1)                   /* If we have a run */ {
				*ptr++ = 1 - c;          /* Then write it */
				*ptr++ = b;
			}
		}
		len = ptr - compbuf;
		fprintf(parfp, "\33*b2m%dW", len);
		buf = compbuf;
	} else {
		/* No compression */
		fprintf(parfp, "\33*b%dW", len);
	}

	fwrite(buf, 1, len, parfp);
}



/* Printer dump */

void prtdump(int num)
{
	char	*buf;
	int	ysize;

	/* Set the number of copies */

	if (num == 0 || num > 99)
		num = 1;
	fprintf(parfp, "\33&l%dX", num);

	/* Set cursor to (0,vmarg), 300 dpi, aligned logical page, start graphics */

	fprintf(parfp, "\33*p0x%dY\33*t300R\33*r0f0A", vmarg);

	/* Loop for the rows */

	buf = parm.page.buf[0];
	ysize = parm.page.ysize;

	while (ysize--) {
		prtdumpline(buf, parm.page.xbytes);
		buf += parm.page.xbytes;
	}

	/* End graphics, form feed, reset number of copies */

	fprintf(parfp, "\33*rB\14\33&l1X");
}




/* Copy the page to the output */
void copypage(int num)
{
	pagenum++;
	if ((optbeg == 0 || pagenum >= optbeg) && (optend == 0 || pagenum <= optend)) {
		prtdump(optcopies == 0 ? num : optcopies);
		if (ferror(parfp))
			PSerror(arec, errioerror);
	}
}


void flushpage(int y1, int y2)
{
	;
}


/* Main program */

int	main(int argc, char **argv)
{
	FILE	 * INPUT_FILE, *OUTPUT_FILE;
	/*	int	*ip, m, ch; */
	int	paper_width_in_pixels, paper_length_in_pixels, left_margin_in_pixels, right_margin_in_pixels, header_margin_in_pixels, footer_margin_in_pixels, horz_offset_in_pixels, vertical_offset_in_pixels;
	int	temp, i;

	int	c;
	extern int	optind;
	extern char	*optarg;

	/* Open the libraries */
	/*
	PSbase = OpenLibrary("post.library", POSTVERNO);
	if (PSbase == NULL) {
		fprintf(stderr, "postlj: can't open post.library\n");
		goto errorexit;
	}
	*/

	/* Parse the arguments and keywords.  See the usage string below */

	optxsize = optysize = -1;
	optlmarg = optrmarg = optumarg = optdmarg = -1;
	opthoff = optvoff = -1;
	optgc = -1;


	while ((c = getopt(argc, argv, "s:a:j:g:b:e:c:x:y:l:r:u:d:h:v:")) != EOF) {
		switch (c) {
		case 's':
			/*  Page size */
			/*
				0 = Letter
				1 = Legal
				2 = Executive
				3 = A4
				4 = COM10
				5 = Monarch
				6 = C5
				7 = DL
			*/
			if (sscanf(optarg, "%d", &optsize) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, MAXSIZE - 1);
				goto print_usage;
			}
			if (optsize < 0 || optsize > MAXSIZE - 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, MAXSIZE - 1);
				goto print_usage;
			}
			break;
		case 'a':
			/* aspect = orientation, portrait or landscape */
			/*
				0 = vertical
				1 = horizontal
			*/
			if (sscanf(optarg, "%d", &optland) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 1);
				goto print_usage;
			}
			if (optland < 0 || optland > 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 1);
				goto print_usage;
			}
			break;
		case 'j':
			/* LaserJet model */
			/*
				0 = DeskJet
				1 = LaserJet II
				2 = LaserJet IIP or III
			*/
			if (sscanf(optarg, "%d", &optlj) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, MAXLJ - 1);
				goto print_usage;
			}
			if (optlj < 0 || optlj > MAXLJ - 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, MAXLJ - 1);
				goto print_usage;
			}
			break;
		case 'g':
			/* Graphics compression */
			/* 0 = off, 1 = on */
			if (sscanf(optarg, "%d", &optgc) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 1);
				goto print_usage;
			}
			if (optgc < 0 || optgc > 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 1);
				goto print_usage;
			}
			break;
		case 'b':
			/* Page number to begin printing at */
			if (sscanf(optarg, "%d", &optbeg) != 1) {
				fprintf(stderr, "postlj: -%c requires number argument\n", c);
				goto print_usage;
			}
			break;
		case 'e':
			/* Page number to end printing after */
			if (sscanf(optarg, "%d", &optend) != 1) {
				fprintf(stderr, "postlj: -%c requires number argument\n", c);
				goto print_usage;
			}
			break;
		case 'c':
			/* Number of copies */
			if (sscanf(optarg, "%d", &optcopies) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 99);
				goto print_usage;
			}
			if (optcopies < 0 || optcopies > 99) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 99);
				goto print_usage;
			}
			break;
		case 'x':
			/* Paper x size */
			if (sscanf(optarg, "%d", &optxsize) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optxsize < 0 || optxsize > 29999) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'y':
			/* Paper y size */
			if (sscanf(optarg, "%d", &optysize) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optysize < 0 || optysize > 29999) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'l':
			/* Left  margin */
			if (sscanf(optarg, "%d", &optlmarg) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optlmarg < 0 || optlmarg > 29999) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'r':
			/* right  margin */
			if (sscanf(optarg, "%d", &optrmarg) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optrmarg < 0 || optrmarg > 29999) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'u':
			/* Upper margin */
			if (sscanf(optarg, "%d", &optumarg) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optumarg < 0 || optumarg > 29999) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'd':
			/* Lower margin */
			if (sscanf(optarg, "%d", &optdmarg) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optdmarg < 0 || optdmarg > 29999) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'h':
			/* Horizontal offset registration */
			if (sscanf(optarg, "%d", &opthoff) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (opthoff < 0 || opthoff > 29999) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'v':
			/* Vertical   offset registration */
			if (sscanf(optarg, "%d", &optvoff) != 1) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optvoff < 0 || optvoff > 29999) {
				fprintf(stderr, "postlj: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		default:
			fprintf(stderr, "postlj: unknown option -%c\n", c);
print_usage:
			fprintf(stderr, "LaserJet or DeskJet driver for Post. PostLJ version 1.7\n");
			fprintf(stderr, "Drives the printer directly for control of page size and better performance\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "  Usage:\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "    postlj -options files... \n");
			fprintf(stderr, "\n");
			fprintf(stderr, "      -sn     Page size (0 = Letter, 1 = Legal, 2 = Executive, 3 = A4,\n");
			fprintf(stderr, "                         4 = COM10, 5 = Monarch, 6 = C5, 7 = DL)\n");
			fprintf(stderr, "      -an     Aspect (0 = vertical, 1 = horizontal)\n");
			fprintf(stderr, "      -jn     LaserJet model (0 = DJ, 1 = LJII, 2 = LJIIP/III)\n");
			fprintf(stderr, "      -gn     Graphics compression (0 = off, 1 = on)\n");
			fprintf(stderr, "      -bnnnn  Page number to begin printing at\n");
			fprintf(stderr, "      -ennnn  Page number to end printing after\n");
			fprintf(stderr, "      -cnn    Number of copies\n");
			fprintf(stderr, "      -xnnnnn Paper x size\n");
			fprintf(stderr, "      -ynnnnn Paper y size\n");
			fprintf(stderr, "      -lnnnnn Left  margin\n");
			fprintf(stderr, "      -rnnnnn Right margin\n");
			fprintf(stderr, "      -unnnnn Upper margin\n");
			fprintf(stderr, "      -dnnnnn Lower margin\n");
			fprintf(stderr, "      -hnnnnn Horizontal offset registration\n");
			fprintf(stderr, "      -vnnnnn Vertical   offset registration\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "  Defaults are A4, portrait, LJIIP/III\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "  For example:\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "    postlj -a1 -j1 psfonts:init.ps myfile.ps\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "  Prints on A4 paper, portrait orientation, using a LaserJet II\n");
			goto errorexit;
		}
	}






	/* Determine the page size */


	paper_width_in_pixels = LJ_PAGESIZE_TABLE[optsize].xsize;
	paper_length_in_pixels = LJ_PAGESIZE_TABLE[optsize].ysize;
	horz_offset_in_pixels = LJ_PAGESIZE_TABLE[optsize].ppoff;
	vertical_offset_in_pixels = LJ_PAGESIZE_TABLE[optsize].ploff;
	if (optland) {
		temp = paper_width_in_pixels;
		paper_width_in_pixels = paper_length_in_pixels;
		paper_length_in_pixels = temp;
		temp = horz_offset_in_pixels;
		horz_offset_in_pixels = vertical_offset_in_pixels;
		vertical_offset_in_pixels = temp;
	}
	if (optlj == 0) /* DeskJet..., margins are 150 pixels */ {
		left_margin_in_pixels = right_margin_in_pixels = 150;
		header_margin_in_pixels = footer_margin_in_pixels = 150;
	} else if (optlj == 1) /* LJ II..., printable area is logical page */ {
		left_margin_in_pixels = right_margin_in_pixels = horz_offset_in_pixels;
		header_margin_in_pixels = footer_margin_in_pixels = vertical_offset_in_pixels;
	} else /* LJ IIP/III..., margins are 50 pixels */    {
		left_margin_in_pixels = right_margin_in_pixels = 50;
		header_margin_in_pixels = footer_margin_in_pixels = 50;
		vertical_offset_in_pixels = header_margin_in_pixels;
	}
	if (optxsize >= 0)
		paper_width_in_pixels = optxsize;
	if (optysize >= 0)
		paper_length_in_pixels = optysize;
	if (optlmarg >= 0)
		left_margin_in_pixels = optlmarg;
	if (optrmarg >= 0)
		right_margin_in_pixels = optrmarg;
	if (optumarg >= 0)
		header_margin_in_pixels = optumarg;
	if (optdmarg >= 0)
		footer_margin_in_pixels = optdmarg;
	if (opthoff  >= 0)
		horz_offset_in_pixels = opthoff;
	if (optvoff  >= 0)
		vertical_offset_in_pixels = optvoff;
	if (paper_width_in_pixels <= left_margin_in_pixels + right_margin_in_pixels || paper_length_in_pixels <= header_margin_in_pixels + footer_margin_in_pixels) {
		fprintf(stderr, "postlj: page size smaller than margins\n");
		goto errorexit;
	}
	hoffset = ((horz_offset_in_pixels - left_margin_in_pixels) * 720) / 300;
	voffset = ((vertical_offset_in_pixels - header_margin_in_pixels) * 720) / 300;
	vmarg = header_margin_in_pixels;
	if (optlj == 0)      /* DeskJet, logical page is at top margin */
		vmarg = 0;

	if (optgc == -1)      /* Default graphics compression */
		if (optlj == 1)
			optgc = 0;   /* Off for LaserJet II */
		else
			optgc = 1;   /* On  for LaserJet IIP/III and DeskJet */

	parm.page.depth = 1;
	parm.page.xoff = left_margin_in_pixels;
	parm.page.yoff = footer_margin_in_pixels;
	parm.page.xsize = paper_width_in_pixels - left_margin_in_pixels - right_margin_in_pixels;
	parm.page.ysize = paper_length_in_pixels - header_margin_in_pixels - footer_margin_in_pixels;
	parm.page.xbytes = (parm.page.xsize + 7) >> 3;
	parm.page.len = parm.page.xbytes * parm.page.ysize;
	parm.page.ybase = 0;
	parm.page.yheight = parm.page.ysize;
	parm.page.xden = parm.page.yden = 300;
	parm.page.ydir = -1;

	/* Allocate the page buffer */

	for (i = 0; i < parm.page.depth; i++) {
		if ((parm.page.buf[i] = malloc((unsigned)parm.page.len)) == NULL) {
			fprintf(stderr, "postlj: can't get page buffer\n");
			goto errorexit;
		}
		memset(parm.page.buf[i], 0, parm.page.len);
	}

	/* Allocate the print compression buffer */

	compsize = parm.page.xbytes + parm.page.xbytes / 128 + 2;
	compbuf = malloc((unsigned)compsize);
	if (compbuf == NULL) {
		fprintf(stderr, "postlj: can't get memory\n");
		goto errorexit;
	}

	/* Open a file to the par: handler and initialise the printer */
	/* lpopen */
	parfp = fopen(argto, "w");
	if (parfp == NULL) {
		fprintf(stderr, "postlj: can't open %s\n", argto);
		goto errorexit;
	}
	prtsetup();
	if (ferror(parfp)) {
		fprintf(stderr, "postlj: error writing printer file\n");
		goto errorexit;
	}

	/* Initialise for interpretation */

	signal(SIGINT, sigint);
	signal(SIGFPE, sigfpe);
	/*
	insertbreak();
	SetExcept(~0, SIGBREAKF_CTRL_C);
	breakset = 1;
	insertftrap();
	ftrapset = 1;
	*/

	parm.copyfunc =  copypage;

	INPUT_FILE = stdin;
	OUTPUT_FILE = stdout;
	errfh = stderr;
	/* parm.infh = Input(); */
	parm.infh = INPUT_FILE;
	/* parm.outfh = Output(); */
	parm.outfh = OUTPUT_FILE;
	parm.errfh = errfh;

	arec = PScreateact(&parm);
	if (arec == 0) {
		fprintf(stderr, "postlj: can't get memory\n");
		goto errorexit;
	}
	if ((unsigned) arec <= errmax) {
		arec = 0;
		retcode = 10;
		goto tidyexit;
	}

	/* Interpret the argument files */

	fprintf(stderr, "postlj: running on %s (%s, %s, (%d:%d:%d * %d:%d:%d))\n",
	    showlj[optlj], LJ_PAGESIZE_TABLE[optsize].showsize, showland[optland],
	    left_margin_in_pixels, paper_width_in_pixels - left_margin_in_pixels - right_margin_in_pixels, right_margin_in_pixels, header_margin_in_pixels, paper_length_in_pixels - header_margin_in_pixels - footer_margin_in_pixels, footer_margin_in_pixels);

	pagenum = 0;

	if (optind < argc) {
		for (; optind < argc; optind++) {
			fprintf(stderr, "main:calling PSintstring:argv[%d]=:%s:\n", optind, argv[optind]);
			if (PSintstring(arec, argv[optind], -1, PSFLAGFILE | PSFLAGCLEAR | PSFLAGERASE) != 0) {
				retcode = 10;
				goto tidyexit;
			}
		}
	} else {
		if (PSintstring(arec, "%stdin", -1, PSFLAGFILE | PSFLAGCLEAR | PSFLAGERASE) != 0) {
			retcode = 10;
			goto tidyexit;
		}
	}

	if (ferror(parfp)) {
		fprintf(stderr, "postlj: error writing printer file\n");
		goto errorexit;
	}
	fprintf(stderr, "postlj: FINISHED\n");
	goto tidyexit;

	/* Argument errors and usage query */



	/* Tidy up and exit */

errorexit:
	retcode = 20;

tidyexit:
	/*
	if (breakset) {
		SetExcept(0, SIGBREAKF_CTRL_C); 
		deletebreak();
		breakset = 0;
	}
	if (ftrapset) {
		deleteftrap();
		ftrapset = 0;
	}
*/
	signal(SIGINT, SIG_DFL);
	signal(SIGFPE, SIG_DFL);

	if (arec)
		PSdeleteact(arec);

	if (parfp) {
		prtreset();
		fclose(parfp);
	}

	if (compbuf)
		free(compbuf);

	for (i = 0; i < parm.page.depth; i++)
		if (parm.page.buf[i]) {
			free(parm.page.buf[i]);
			parm.page.buf[i] = NULL;
		}
	/*
	if (PSbase)
		CloseLibrary(PSbase);
	*/
	return(retcode);
}


