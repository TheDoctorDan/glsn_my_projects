/* rle_dec.c */
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

#include <win.h>
#include <memory.h>

#include "rle_tables.h"

#include "bitops.h"

extern void     perror(), exit();

struct{
	int	xsize;
}page;

FILE	*flp;
	char	**out_buf;
	int	buf_num;
	int	max_out_bufs;
	int	bit_row, line_bit;

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

/* debug defs */
#define SEE_STRING_BUILD 0
#define SEE_RESULT_COUNT 0
#define SEE_BLACK_AND_WHITE 0
#define SEE_EOL 0

#define MINF(x,y) (x<y?x:y)
#define MAXF(x,y) (x>y?x:y)

#define MATCH_TYPES_EOL	1
#define MATCH_TYPES_WHITES_LESS_THAN_64	2
#define MATCH_TYPES_BLACKS_LESS_THAN_64	3
#define MATCH_TYPES_WHITES_MULT_OF_64	4
#define MATCH_TYPES_BLACKS_MULT_OF_64	5
#define MATCH_TYPES_EITHER_COLOR_MULT_OF_64	6

struct{
	int	count;
	int	type;
	int	index;
} match_result;

#define MODE_EOL_ONLY			1
#define MODE_WHITE_LONG_OR_SHORT	2
#define MODE_WHITE_SHORT_ONLY		3
#define MODE_BLACK_LONG_OR_SHORT	4
#define MODE_BLACK_SHORT_ONLY		5

int	current_mode = MODE_WHITE_LONG_OR_SHORT;
int	current_line_length_bits = 0;
int	current_line_length_pixels = 0;
char	coding_method='0';
int	in_fill_dropping_mode;

int	max_string_length = 13;
char	match_string[31];
int	match_string_length = 0;


/* Dump a line of pixels */

void prtdumpline(buf)
char **buf;
{
	int	i, c, move;
	int	try_move;
	int	local_buf_num;
	int	len;
	len=page.xsize;

	max_out_bufs = GENICOM_VDPIS[vdpi_index].num_out_bufs;
	for (local_buf_num = 0; local_buf_num < max_out_bufs; local_buf_num++) {
		while (len && buf[local_buf_num][len - 1] == '@')
			len--;
		try_move = 1;
		move = 0;
		for (i = 0; i < len; i++) {
			c = buf[local_buf_num][i];
			if (c == '\177')
				c = '?';
			if (i + 20 < len) {
				if (strncmp(&buf[local_buf_num][i], "@@@@@@@@@@@@@@@@@@@@", 20) == 0)
					try_move = 1;
			}

			try_move=0;
			if (try_move) {
				if (c == '@') {
					move = 1;
				} else {
					if (move)
						fprintf(flp, "\033[%d`", i * 720 / GENICOM_HDPIS[hdpi_index].hdpi);
					fprintf(flp, "%c", c);
					move = 0;
					try_move = 0;
				}
			} else {
				fprintf(flp, "%c", c);
			}
		}
		fprintf(flp, "%s", GENICOM_VDPIS[vdpi_index].end_of_line_movement[local_buf_num]);
	}
}





void do_EOL()
{
	buf_num++;
	if (buf_num >= max_out_bufs) {
		bit_row++;
		if (bit_row > 5) {
			if (bit_row != 100) {
				prtdumpline(out_buf);
			}
			bit_row = 0;
			for (buf_num = 0; buf_num < max_out_bufs; buf_num++) {
				memset(out_buf[buf_num], '@', page.xsize);
				out_buf[page.xsize] = 0;
			}
		}
		buf_num = 0;
	}
	line_bit=0;
}

int	out_white(count)
int	count;
{
	line_bit+=count;
	if(line_bit >=page.xsize)
		return(1);
	else
		return(0);
}

int	out_black(count)
int	count;
{
	int	i;
	for(i=0;i<count && line_bit <page.xsize ;i++){
		out_buf[buf_num][line_bit] = char_set_bit(out_buf[buf_num][line_bit], bit_row);
		line_bit++;
	}
	if(line_bit >=page.xsize)
	return(1);
	else
	return(0);
}



void	shift_string(num)
int	num;
{
	current_line_length_bits += num;
	strcpy(match_string, &match_string[num]);
	match_string_length -=num;
#if SEE_STRING_BUILD
	fprintf(stderr,"match_string:%s:\n",match_string);
#endif
}


void	add_to_string(letter)
char	letter;
{
	if (match_string_length >= max_string_length) {
		/* shift */
		if (match_string[0] == '0') {
			/* drop leading 0 as padding */
			if(!in_fill_dropping_mode){
				in_fill_dropping_mode=1;
				fprintf(stderr,"fill:");
			}
			fprintf(stderr,"%c", match_string[0]);
			strcpy(match_string, &match_string[1]);
			match_string_length--;
			current_line_length_bits += 1;
			current_mode = MODE_EOL_ONLY;
		} else {
			fprintf(stderr,"Can't drop leading 1\n");
			fprintf(stderr, "match_string:%s:\n", match_string);
			fprintf(stderr,"match_result.count:%d\n", match_result.count);
			/* resync on EOL */
			if(!in_fill_dropping_mode){
				in_fill_dropping_mode=1;
				fprintf(stderr,"fill:");
			}
			fprintf(stderr,"%c", match_string[0]);


			strcpy(match_string, &match_string[1]);
			match_string_length--;
			current_line_length_bits += 1;
			current_mode = MODE_EOL_ONLY;
		}
	}
	sprintf(&match_string[match_string_length],"%c", letter);
	match_string_length++;
#if SEE_STRING_BUILD
	fprintf(stderr,"match_string:%s:\n",match_string);
#endif
}



void	try_match()
{
	int	i;
again:
	memset(&match_result, 0, sizeof match_result);

	if (strncmp(match_string, EOL_STRING, MINF(match_string_length, EOL_STRING_LEN)) == 0) {
		match_result.count++;
		match_result.type = MATCH_TYPES_EOL;
	}

	for (i = 0; i < NUM_LESS_THAN_64; i++) {
		switch (current_mode) {

case MODE_WHITE_LONG_OR_SHORT:
case MODE_WHITE_SHORT_ONLY:
			if (strncmp(match_string, whites_less_than_64[i].bits, MINF(match_string_length, whites_less_than_64[i].len)) == 0){
				match_result.count++;
				match_result.type = MATCH_TYPES_WHITES_LESS_THAN_64;
				match_result.index = i;
			}
			break;

case MODE_BLACK_LONG_OR_SHORT:
case MODE_BLACK_SHORT_ONLY:

			if (strncmp(match_string, blacks_less_than_64[i].bits, MINF(match_string_length, blacks_less_than_64[i].len)) == 0) {
				match_result.count++;
				match_result.type = MATCH_TYPES_BLACKS_LESS_THAN_64;
				match_result.index = i;
			}
			break;
		}
	}

	for (i = 0; i < NUM_MULT_OF_64; i++) {
		switch (current_mode) {

case MODE_WHITE_LONG_OR_SHORT:
			if (strncmp(match_string, whites_mult_of_64[i].bits, MINF(match_string_length, whites_mult_of_64[i].len)) == 0) {
				match_result.count++;
				match_result.type = MATCH_TYPES_WHITES_MULT_OF_64;
				match_result.index = i;
			}
			break;

case MODE_BLACK_LONG_OR_SHORT:
			if (strncmp(match_string, blacks_mult_of_64[i].bits, MINF(match_string_length, blacks_mult_of_64[i].len)) == 0) {
				match_result.count++;
				match_result.type = MATCH_TYPES_BLACKS_MULT_OF_64;
				match_result.index = i;
			}
			break;
		}
	}

	for (i = 0; i < NUM_EXTRA_MULT_OF_64; i++) {
		if (strncmp(match_string, either_color_mult_of_64[i].bits, MINF(match_string_length, either_color_mult_of_64[i].len)) == 0) {
			match_result.count++;
			match_result.type = MATCH_TYPES_EITHER_COLOR_MULT_OF_64;
			match_result.index = i;
		}
	}
#if SEE_RESULT_COUNT
	fprintf(stderr,"match_result.count:%d:\n", match_result.count);
#endif

	if (match_result.count == 1) {
		if(in_fill_dropping_mode){
			fprintf(stderr,":\n");
			in_fill_dropping_mode=0;
		}
		switch (match_result.type) {
		case MATCH_TYPES_EOL:
			if (strncmp(match_string, EOL_STRING, EOL_STRING_LEN) == 0) {
#if SEE_EOL
				fprintf(stderr,"EOL: length pixels:%6d  current_line_length_bits:%6d\n", current_line_length_pixels, current_line_length_bits);
#endif
do_EOL();
				/*
				if(current_line_length_pixels!=1728 &&
				current_line_length_pixels!=0)
					exit(1);
				*/
				current_line_length_bits = 0;
				current_line_length_pixels = 0;
				shift_string(EOL_STRING_LEN);
				coding_method=match_string[0];
				fprintf(stderr,"coding method:%c\n", coding_method);
				/* shift_string(1); */

				current_mode = MODE_WHITE_LONG_OR_SHORT;
				goto again;
			}
			break;
		case MATCH_TYPES_WHITES_LESS_THAN_64:
			if (strncmp(match_string, whites_less_than_64[match_result.index].bits, whites_less_than_64[match_result.index].len) == 0) {
#if SEE_BLACK_AND_WHITE
				fprintf(stderr,"white:%d\n", match_result.index);
#endif
out_white(match_result.index);
				current_line_length_pixels += match_result.index;
				shift_string(whites_less_than_64[match_result.index].len);
				current_mode = MODE_BLACK_LONG_OR_SHORT;
				goto again;
			}
			break;
		case MATCH_TYPES_BLACKS_LESS_THAN_64:
			if (strncmp(match_string, blacks_less_than_64[match_result.index].bits, blacks_less_than_64[match_result.index].len) == 0) {
#if SEE_BLACK_AND_WHITE
				fprintf(stderr,"black:%d\n", match_result.index);
#endif
out_black(match_result.index);
				current_line_length_pixels += match_result.index;
				shift_string(blacks_less_than_64[match_result.index].len);
				current_mode = MODE_WHITE_LONG_OR_SHORT;
				goto again;
			}
			break;
		case MATCH_TYPES_WHITES_MULT_OF_64:
			if (strncmp(match_string, whites_mult_of_64[match_result.index].bits, whites_mult_of_64[match_result.index].len) == 0) {
#if SEE_BLACK_AND_WHITE
				fprintf(stderr,"white:(%d+1)*64=%d\n", match_result.index, (match_result.index+1)*64);
#endif
out_white((match_result.index+1)*64);
				current_line_length_pixels += (match_result.index + 1) * 64;
				shift_string(whites_mult_of_64[match_result.index].len);
				current_mode = MODE_WHITE_SHORT_ONLY;
				goto again;
			}
			break;
		case MATCH_TYPES_BLACKS_MULT_OF_64:
			if (strncmp(match_string, blacks_mult_of_64[match_result.index].bits, blacks_mult_of_64[match_result.index].len) == 0) {
#if SEE_BLACK_AND_WHITE
				fprintf(stderr,"black:(%d+1)*64=%d\n", match_result.index, (match_result.index+1)*64);
#endif
out_black((match_result.index+1)*64);
				current_line_length_pixels += (match_result.index + 1) * 64;
				shift_string(blacks_mult_of_64[match_result.index].len);
				current_mode = MODE_BLACK_SHORT_ONLY;
				goto again;
			}
			break;
		case MATCH_TYPES_EITHER_COLOR_MULT_OF_64:
			if (strncmp(match_string, either_color_mult_of_64[match_result.index].bits, either_color_mult_of_64[match_result.index].len) == 0) {

				switch(current_mode){
				case MODE_WHITE_LONG_OR_SHORT:
#if SEE_BLACK_AND_WHITE
					fprintf(stderr,"white, ");
					fprintf(stderr,"either:(%d+28)*64=%d\n", match_result.index, (match_result.index+28)*64);
#endif
out_white((match_result.index+28)*64);
				break;
				case MODE_BLACK_LONG_OR_SHORT:
#if SEE_BLACK_AND_WHITE
					fprintf(stderr,"black, ");
					fprintf(stderr,"either:(%d+28)*64=%d\n", match_result.index, (match_result.index+28)*64);
#endif
out_black((match_result.index+28)*64);
				break;
				}

				current_line_length_pixels += (match_result.index + 28) * 64;
				shift_string(either_color_mult_of_64[match_result.index].len);

				switch(current_mode){
				case MODE_WHITE_LONG_OR_SHORT:
				current_mode = MODE_WHITE_SHORT_ONLY;
				break;
				case MODE_BLACK_LONG_OR_SHORT:
				current_mode = MODE_BLACK_SHORT_ONLY;
				break;
				}

				goto again;
			}
			break;
		}
	}
}


/* Printer reset */
void prtreset()
{
	/* graphic off */
	fprintf(flp, "\033\\\n");
}




main(argc, argv)
int	argc;
char	**argv;
{
	int	bit;
	int	first = 1;
	int	c;
	FILE	*input_file;
	window_init();


page.xsize=1728;

	memset(match_string, 0, 31);
	flp=wlpopen("rle_dec");
	if(flp!= NULL){

	/* set for 144h x 72v dpi */
	fprintf(flp, "%s", GENICOM_HDPIS[hdpi_index].start_string);
	/* start graphics */
	fprintf(flp, "\033P");
	fflush(flp);

	max_out_bufs = GENICOM_VDPIS[vdpi_index].num_out_bufs;

	out_buf = (char * *)malloc(sizeof(char *) * max_out_bufs );
	if (out_buf == NULL) {
		fprintf(stderr, "prtdump:malloc failed\n");
		perror("rle_dec");
		exit(1);
	}

	for (buf_num = 0; buf_num < max_out_bufs; buf_num++) {
		out_buf[buf_num] = malloc((unsigned) page.xsize + 1);
		if (out_buf[buf_num] == NULL) {
			fprintf(stderr, "prtdump:malloc failed\n");
			perror("rle_dec");
			exit(1);
		}
	}



		input_file=fopen("bajko.out","r");
		if(input_file != NULL){

	buf_num = max_out_bufs;
	bit_row = 99;
	in_fill_dropping_mode=0;

			while ( (c = getc(input_file)) != EOF && !feof(input_file)) {
				if (first) {
					if (c == 0)
						first = 0;
					else {
						c = getc(input_file);
						continue;
					}
				}
				for (bit = 0; bit < 8; bit++) {
					if (int_test_bit(c, bit)) {
						add_to_string('1');
					} else {
						add_to_string('0');
					}
					try_match();
				}
			}
			fclose(input_file);
			if (bit_row != 99)
				prtdumpline(out_buf);
		}

		prtreset();
		for (buf_num = 0; buf_num < max_out_bufs; buf_num++)
			free(out_buf[buf_num]);
		wlpclos(flp);
	}
	window_end();
	return(0);
}


