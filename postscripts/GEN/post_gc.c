/* genicomm print driver for Post.  File "post_gc.c" */
/*
|------------------------------------------------------------------------------|
|	Copyright (c) 1985, 1986, 1987, 1988, 1989, 1990, 1991, 1992, 1993     |
|	Gleeson and Associates                                                 |
|	All Rights Reserved                                                    |
|                                                                              |
|	THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|	Gleeson And Associates                                                 |
|	The copyright notice above does not evidence any                       |
|	actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/

#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#include <malloc.h>
#include <signal.h>

#include "postlib.h"

#include "bitops.h"

/* Routines defined and referenced only within this module */


/* External data (initialised to zero) */

int	retcode;

int	arec;

extern void	perror();
extern void	exit();

FILE	*errfh = NULL;
FILE *parfp;

struct PSparm parm;


/* Options */

#define DEFSIZE 0 /* A4 page size */

#define MAXSIZE 1

int	optsize = DEFSIZE;
int	optgc;
int	optcopies;
int	optbeg, optend;
int	optxsize, optysize;
int	optlmarg, optrmarg, optumarg, optdmarg;
int	opthoff, optvoff;

int	pagenum;
/*int	hoffset, voffset, vmarg;*/

#define NUM_GENICOM_HDPIS 3
static struct {
	int	hdpi;
	char	*start_string;
} GENICOM_HDPIS[NUM_GENICOM_HDPIS] = {
	{ 72, "\033[0q" },
	{ 144, "\033[1q" },
	{ 288, "\033[5q" }
};



#define NUM_GENICOM_VDPIS 3
static struct {
	int	vdpi, num_out_bufs;
	char	*end_of_line_movement[4];
} GENICOM_VDPIS[NUM_GENICOM_VDPIS] = {
	{ 72, 1, "\r\033[60e", "", "", "" },
	{ 144, 2, "\r\033[5e", "\r\033[55e", "", "" },
	{ 288, 4, "\r\033[2e", "\r\033[3e", "\r\033[2e", "\r\033[53e" },

};


int	hdpi_index = 1;
int	vdpi_index = 1;

/* Page size tables.
 * (See Figures 2-2 and 2-3 in the LaserJet 2P Technical Reference Manual.)
 *
 *                     A4    
 */

static struct {
	char	*showsize;
	int	xsize, ysize, ppoff, ploff
} LJ_PAGESIZE_TABLE[MAXSIZE] = {
	{ "A4", 	2480, 	3507, 	71, 	59 }
};






int	compsize;
char	*compbuf;

/* Arguments */

char	*argto = "par:";





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
	return;
}


/* Printer reset */

void prtreset(void)
{
	/* graphic off */
	fprintf(parfp, "\033\\\n");
}


/* Dump a line of pixels */

void prtdumpline(char **buf, int len)
{
	int	i, c, move;
	int	try_move;
	int	buf_num;
	int	max_out_bufs;

	max_out_bufs = GENICOM_VDPIS[vdpi_index].num_out_bufs;
	for (buf_num = 0; buf_num < max_out_bufs; buf_num++) {
		while (len && buf[buf_num][len - 1] == '@')
			len--;
		try_move = 1;
		move = 0;
		for (i = 0; i < len; i++) {
			c = buf[buf_num][i];
			if (c == '\177')
				c = '?';
			if (i + 20 < len) {
				if (strncmp(&buf[buf_num][i], "@@@@@@@@@@@@@@@@@@@@", 20) == 0)
					try_move = 1;
			}

			try_move=0;
			if (try_move) {
				if (c == '@') {
					move = 1;
				} else {
					if (move)
						fprintf(parfp, "\033[%d`", i * 720 / GENICOM_HDPIS[hdpi_index].hdpi);
					fprintf(parfp, "%c", c);
					move = 0;
					try_move = 0;
				}
			} else {
				fprintf(parfp, "%c", c);
			}
		}
		fprintf(parfp, "%s", GENICOM_VDPIS[vdpi_index].end_of_line_movement[buf_num]);
	}
}



/* Printer dump */

void prtdump()
{
	char	*buf;
	int	ysize;

	int	bit_row;
	int	len, i, j, line_bit;
	char	**out_buf;
	int	buf_num;
	int	max_out_bufs;

	max_out_bufs = GENICOM_VDPIS[vdpi_index].num_out_bufs;

	out_buf = (char * *)malloc(sizeof(char *) * max_out_bufs );
	if (out_buf == NULL) {
		fprintf(stderr, "prtdump:malloc failed\n");
		perror("post_gc");
		exit(1);
	}

	for (buf_num = 0; buf_num < max_out_bufs; buf_num++) {
		out_buf[buf_num] = malloc((unsigned) parm.page.xsize + 1);
		if (out_buf[buf_num] == NULL) {
			fprintf(stderr, "prtdump:malloc failed\n");
			perror("post_gc");
			exit(1);
		}
	}


	/* set for 144h x 72v dpi */
	fprintf(parfp, "%s", GENICOM_HDPIS[hdpi_index].start_string);
	/* start graphics */
	fprintf(parfp, "\033P");
	fflush(parfp);


	/* Loop for the rows */

	buf = parm.page.buf[0];
	ysize = parm.page.ysize;
	buf_num = max_out_bufs;


	bit_row = 99;
	/*
	?=1f=00011111 all on
	p=80=00010000 bottom on on
	byte | with '@' if '\177' -> '?'
	*/


	while (ysize--) {
		buf_num++;
		if (buf_num >= max_out_bufs) {
			bit_row++;
			if (bit_row > 5) {
				if (bit_row != 100) {
					prtdumpline(out_buf, parm.page.xsize);
				}
				bit_row = 0;
				for (buf_num = 0; buf_num < max_out_bufs; buf_num++) {
					memset(out_buf[buf_num], '@', parm.page.xsize);
					out_buf[parm.page.xsize] = 0;
				}
			}
			buf_num = 0;
		}

		len = parm.page.xbytes;
		while (len && buf[len - 1] == 0)
			len--;

		for (i = 0; i < len; i++) {
			for (j = 0; j < 8; j++) {
				line_bit = i * 8 + j;
				if (char_test_bit(buf[i], 7 - j)) {
					if (line_bit > parm.page.xsize)
						continue;
					out_buf[buf_num][line_bit] = char_set_bit(out_buf[buf_num][line_bit], bit_row);
				}
			}
		}

		buf += parm.page.xbytes;
	}
	if (bit_row != 99)
		prtdumpline(out_buf, parm.page.xsize);


	/* graphic off */
	fprintf(parfp, "\033\\\n");
	fflush(parfp);

	for (buf_num = 0; buf_num < max_out_bufs; buf_num++)
		free(out_buf[buf_num]);

}




/* Copy the page to the output */
void copypage(int num)
{
	int	copies, i;
	pagenum++;
	if ((optbeg == 0 || pagenum >= optbeg) && (optend == 0 || pagenum <= optend)) {
		copies = (optcopies == 0 ? num : optcopies);
		for (i = 0; i < copies; i++) {
			prtdump();
			if (ferror(parfp))
				PSerror(arec, errioerror);
		}
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
	int	paper_width_in_pixels, paper_length_in_pixels, left_margin_in_pixels, right_margin_in_pixels, header_margin_in_pixels, footer_margin_in_pixels, horz_offset_in_pixels, vertical_offset_in_pixels;
	int	i, hdpi, vdpi;

	int	c;
	extern int	optind;
	extern char	*optarg;


	/* Parse the arguments and keywords.  See the usage string below */

	optxsize = optysize = -1;
	optlmarg = optrmarg = optumarg = optdmarg = -1;
	opthoff = optvoff = -1;
	optgc = -1;


	while ((c = getopt(argc, argv, "s:g:b:e:c:x:y:l:r:u:d:h:v:")) != EOF) {
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
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, MAXSIZE - 1);
				goto print_usage;
			}
			if (optsize < 0 || optsize > MAXSIZE - 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, MAXSIZE - 1);
				goto print_usage;
			}
			break;
		case 'g':
			/* Graphics compression */
			/* 0 = off, 1 = on */
			if (sscanf(optarg, "%d", &optgc) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 1);
				goto print_usage;
			}
			if (optgc < 0 || optgc > 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 1);
				goto print_usage;
			}
			break;
		case 'b':
			/* Page number to begin printing at */
			if (sscanf(optarg, "%d", &optbeg) != 1) {
				fprintf(stderr, "post_gc: -%c requires number argument\n", c);
				goto print_usage;
			}
			break;
		case 'e':
			/* Page number to end printing after */
			if (sscanf(optarg, "%d", &optend) != 1) {
				fprintf(stderr, "post_gc: -%c requires number argument\n", c);
				goto print_usage;
			}
			break;
		case 'c':
			/* Number of copies */
			if (sscanf(optarg, "%d", &optcopies) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 99);
				goto print_usage;
			}
			if (optcopies < 0 || optcopies > 99) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 99);
				goto print_usage;
			}
			break;
		case 'x':
			/* Paper x size */
			if (sscanf(optarg, "%d", &optxsize) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optxsize < 0 || optxsize > 29999) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'y':
			/* Paper y size */
			if (sscanf(optarg, "%d", &optysize) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optysize < 0 || optysize > 29999) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'l':
			/* Left  margin */
			if (sscanf(optarg, "%d", &optlmarg) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optlmarg < 0 || optlmarg > 29999) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'r':
			/* right  margin */
			if (sscanf(optarg, "%d", &optrmarg) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optrmarg < 0 || optrmarg > 29999) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'u':
			/* Upper margin */
			if (sscanf(optarg, "%d", &optumarg) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optumarg < 0 || optumarg > 29999) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'd':
			/* Lower margin */
			if (sscanf(optarg, "%d", &optdmarg) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optdmarg < 0 || optdmarg > 29999) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'h':
			/* Horizontal offset registration */
			if (sscanf(optarg, "%d", &opthoff) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (opthoff < 0 || opthoff > 29999) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		case 'v':
			/* Vertical   offset registration */
			if (sscanf(optarg, "%d", &optvoff) != 1) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			if (optvoff < 0 || optvoff > 29999) {
				fprintf(stderr, "post_gc: -%c requires argument 0-%d\n", c, 29999);
				goto print_usage;
			}
			break;
		default:
			fprintf(stderr, "post_gc: unknown option -%c\n", c);
print_usage:
			fprintf(stderr, "LaserJet or DeskJet driver for Post. PostLJ version 1.7\n");
			fprintf(stderr, "Drives the printer directly for control of page size and better performance\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "  Usage:\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "    post_gc -options files... \n");
			fprintf(stderr, "\n");
			fprintf(stderr, "      -sn     Page size (0 = Letter, 1 = Legal, 2 = Executive, 3 = A4,\n");
			fprintf(stderr, "                         4 = COM10, 5 = Monarch, 6 = C5, 7 = DL)\n");
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
			fprintf(stderr, "    post_gc -a1 psfonts:init.ps myfile.ps\n");
			fprintf(stderr, "\n");
			fprintf(stderr, "  Prints on A4 paper, portrait orientation, using a LaserJet II\n");
			goto errorexit;
		}
	}





	hdpi = GENICOM_HDPIS[hdpi_index].hdpi;
	vdpi = GENICOM_VDPIS[vdpi_index].vdpi;

	/* Determine the page size */


	paper_width_in_pixels = LJ_PAGESIZE_TABLE[optsize].xsize * hdpi / 300;
	paper_length_in_pixels = LJ_PAGESIZE_TABLE[optsize].ysize * vdpi / 300;
	horz_offset_in_pixels = LJ_PAGESIZE_TABLE[optsize].ppoff * hdpi / 300;
	vertical_offset_in_pixels = LJ_PAGESIZE_TABLE[optsize].ploff * vdpi / 300;


	/* genicomm..., margins are 1/2 inch 36 pixels */
	/*
	left_margin_in_pixels = right_margin_in_pixels = .5 * hdpi;
	header_margin_in_pixels = footer_margin_in_pixels = .5 * vdpi;
	vertical_offset_in_pixels = header_margin_in_pixels;
	*/
	left_margin_in_pixels = right_margin_in_pixels = 0;
	header_margin_in_pixels = footer_margin_in_pixels = 0;
	vertical_offset_in_pixels = header_margin_in_pixels;

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
		fprintf(stderr, "post_gc: page size smaller than margins\n");
		goto errorexit;
	}

	/* hoffset and voffset are in decipoints */
/*
	hoffset = ((horz_offset_in_pixels - left_margin_in_pixels) * 720) / hdpi;
	voffset = ((vertical_offset_in_pixels - header_margin_in_pixels) * 720) / vdpi;
	vmarg = 0;
	*/


	parm.page.depth = 1;
	parm.page.xoff = left_margin_in_pixels;
	parm.page.yoff = footer_margin_in_pixels;
	parm.page.xsize = paper_width_in_pixels - left_margin_in_pixels - right_margin_in_pixels;
	parm.page.ysize = paper_length_in_pixels - header_margin_in_pixels - footer_margin_in_pixels;
	parm.page.xbytes = (parm.page.xsize + 7) >> 3;
	parm.page.len = parm.page.xbytes * parm.page.ysize;
	parm.page.ybase = 0;
	parm.page.yheight = parm.page.ysize;
	parm.page.xden = hdpi;
	parm.page.yden = vdpi;
	parm.page.ydir = -1;

	/* Allocate the page buffer */

	for (i = 0; i < parm.page.depth; i++) {
		if ((parm.page.buf[i] = malloc((unsigned)parm.page.len)) == NULL) {
			fprintf(stderr, "post_gc: can't get page buffer\n");
			goto errorexit;
		}
		memset(parm.page.buf[i], 0, parm.page.len);
	}

	/* Allocate the print compression buffer */

	compsize = parm.page.xbytes + parm.page.xbytes / 128 + 2;
	compbuf = malloc((unsigned)compsize);
	if (compbuf == NULL) {
		fprintf(stderr, "post_gc: can't get memory\n");
		goto errorexit;
	}

	/* Open a file to the par: handler and initialise the printer */
	/* lpopen */
	parfp = fopen(argto, "w");
	if (parfp == NULL) {
		fprintf(stderr, "post_gc: can't open %s\n", argto);
		goto errorexit;
	}
	prtsetup();
	if (ferror(parfp)) {
		fprintf(stderr, "post_gc: error writing printer file\n");
		goto errorexit;
	}

	/* Initialise for interpretation */

	signal(SIGINT, sigint);
	signal(SIGFPE, sigfpe);

	parm.copyfunc =  copypage;

	INPUT_FILE = stdin;
	OUTPUT_FILE = stdout;
	errfh = stderr;

	parm.infh = INPUT_FILE;
	parm.outfh = OUTPUT_FILE;
	parm.errfh = errfh;

	arec = PScreateact(&parm);
	if (arec == 0) {
		fprintf(stderr, "post_gc: can't get memory\n");
		goto errorexit;
	}
	if ((unsigned) arec <= errmax) {
		arec = 0;
		retcode = 10;
		goto tidyexit;
	}

	/* Interpret the argument files */

	fprintf(stderr, "post_gc: running (%s, (%d:%d:%d * %d:%d:%d))\n", LJ_PAGESIZE_TABLE[optsize].showsize, left_margin_in_pixels, paper_width_in_pixels - left_margin_in_pixels - right_margin_in_pixels, right_margin_in_pixels, header_margin_in_pixels, paper_length_in_pixels - header_margin_in_pixels - footer_margin_in_pixels, footer_margin_in_pixels);

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
		fprintf(stderr, "post_gc: error writing printer file\n");
		goto errorexit;
	}
	fprintf(stderr, "post_gc: FINISHED\n");
	goto tidyexit;

	/* Argument errors and usage query */



	/* Tidy up and exit */

errorexit:
	retcode = 20;

tidyexit:
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
	return(retcode);
}


