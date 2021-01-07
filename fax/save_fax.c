/* fax.c */
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

#include <stdio.h>

/*define stdin f0 */
/*define stdout f1 */
/*define stderr f2 */

#include "atxy.h"
#include <string.h>
#include <memory.h>
#include <signal.h>
#include <termio.h>
#include <malloc.h>
#include <errno.h>
#include <varargs.h>

#include "systim.h"
#include "str_index.h"
#include "nointer.h"

#define GOT_IT 0
#define GOT_EOF -1
#define GOT_TIMEOUT -2
#define NO_ALLOWED_ANSWERS -3

static char	*getty = "/etc/getty";

char	sys_time[9];

FILE	*f0, *f1, *f2;

FILE	*fax_in;
FILE	*fax_out;
FILE	*file_out;
FILE	*file_in;

int	main_err;

extern void	exit();
extern void	perror();
extern int	ioctl();
extern unsigned	alarm();

int	fax_alarm_flag;

struct {
	int	vertical_resolution, bit_rate, page_width, page_length,
		data_compression_format, error_correction,
		binary_file_transfer, scan_time_per_line;
}session_subparameter_codes;


#define NUM_VERTICAL_RESOLUTIONS 2
static struct{
	char	*desc;
	int	dpi;
}VERTICAL_RESOLUTION_TABLE[NUM_VERTICAL_RESOLUTIONS]={
	{"Normal",	98 },
	{"Fine",	196 },
};

#define NUM_BIT_RATES 4
static struct{
	int	bits_per_second;
}BIT_RATE_TABLE[NUM_BIT_RATES]={
	{ 2400 },
	{ 4800 },
	{ 7200 },
	{ 9600 }
};

#define NUM_PAGE_WIDTHS 5
static struct{
	int	pixels, millimeters;
}PAGE_WIDTH_TABLE[NUM_PAGE_WIDTHS]={
	{ 1728, 215 },
	{ 2048, 255 },
	{ 2432, 303 },
	{ 1216, 151 },
	{ 864, 107 }
};

#define NUM_PAGE_LENGTHS 3
static struct{
	char	*desc;
	int	millimeters;
}PAGE_LENGTH_TABLE[NUM_PAGE_LENGTHS]={
	{ "A4",	297 },
	{ "B4",	364 },
	{ "Unlimited", -1 }
};

#define NUM_DATA_COMPRESSION_FORMATS 4
static struct{
	char	*desc;
	int	not_supported;
}DATA_COMPRESSION_FORMAT_TABLE[NUM_DATA_COMPRESSION_FORMATS]={
	{ "1 Dimensional modifed Huffman",		0 },
	{ "2 Dimensional Read",				1 },
	{ "2 Dimensional Uncompressed mode",		1 },
	{ "2 Dimensional modified modified Read",	1 }
};

#define NUM_ERROR_CORRECTIONS 3
static struct{
	char	*desc;
	int	not_supported;
}ERROR_CORRECTION_TABLE[NUM_ERROR_CORRECTIONS]={
	{ "Disable ECM",			0 },
	{ "Enable ECM,  64 bytes/frame",	1 },
	{ "Enable ECM, 256 bytes/frame",	1 }
};

#define NUM_BINARY_FILE_TRANSFERS 2
static struct{
	char	*desc;
	int	not_supported;
}BINARY_FILE_TRANSFER_TABLE[NUM_BINARY_FILE_TRANSFERS]={
	{ "Disabled",	0 },
	{ "Enabled",	1 }
};

#define NUM_SCAN_TIME_PER_LINES 8
static struct{
	int	milliseconds[NUM_VERTICAL_RESOLUTIONS];
}SCAN_TIME_PER_LINE_TABLE[NUM_SCAN_TIME_PER_LINES]={
	{ 0,	0 },
	{ 5,	5 },
	{ 10,	5 },
	{ 10,	10 },
	{ 20,	10 },
	{ 20,	20 },
	{ 40,	20 },
	{ 40,	40 }
};

void strip_string(s)
char	*s;
{
	if (s[strlen(s)-1] == '\n')
		s[strlen(s)-1] = 0;
	if (s[strlen(s)-1] == '\r')
		s[strlen(s)-1] = 0;
	if (s[0] == ':')
		strcpy(s, &s[1]);
	if (s[0] == '=')
		strcpy(s, &s[1]);
}

void	control_print(string)
char	*string;
{
	int	c;
	while (*string) {
		c = *string;
		if (c < ' ')
			fprintf(stderr, "^%c", c + '@');
		else if (c < '\177')
			fprintf(stderr, "%c", c);
		else
			fprintf(stderr, "\%3.3o", c);
		string++;
	}
	fprintf(stderr, "\n");
}


int decode_session_string(s)
char	*s;
{
	char	*cptr;
	int	field;
	int	ier=0;
	/* string has : on front and \r\j on end */
	/* ":0,1,0,0,0,0,0,0^M^J" */

	strip_string(s);
	control_print(s);
	cptr=strtok(s,",");
	field=0;
	while(cptr != NULL){
		if(strlen(cptr)==1 && (*cptr >='0' && *cptr <='9')){

			switch(field){

			case 0:
				session_subparameter_codes.vertical_resolution= *cptr - '0';
				if(session_subparameter_codes.vertical_resolution <0 || session_subparameter_codes.vertical_resolution >= NUM_VERTICAL_RESOLUTIONS){
					fprintf(stderr, "vertical resolution invalid\n");
					ier=1;
					goto end;
				}
				fprintf(stderr, "vertical resolution :%s, %d dpi\n", VERTICAL_RESOLUTION_TABLE[session_subparameter_codes.vertical_resolution].desc, VERTICAL_RESOLUTION_TABLE[session_subparameter_codes.vertical_resolution].dpi);
				break;

			case 1:
				session_subparameter_codes.bit_rate = *cptr -'0';
				if(session_subparameter_codes.bit_rate <0 || session_subparameter_codes.bit_rate >= NUM_BIT_RATES){
					fprintf(stderr, "bit rate invalid\n");
					ier=1;
					goto end;
				}
				fprintf(stderr, "bit rate :%d bits per second\n", BIT_RATE_TABLE[session_subparameter_codes.bit_rate].bits_per_second);
				break;

			case 2:
				session_subparameter_codes.page_width = *cptr -'0';
				if(session_subparameter_codes.page_width <0 || session_subparameter_codes.page_width >= NUM_PAGE_WIDTHS){
					fprintf(stderr, "page width invalid\n");
					ier=1;
					goto end;
				}
				fprintf(stderr, "page_width :%d pixels in %d mm\n", PAGE_WIDTH_TABLE[session_subparameter_codes.page_width].pixels, PAGE_WIDTH_TABLE[session_subparameter_codes.page_width].millimeters);
				break;

			case 3:
				session_subparameter_codes.page_length = *cptr -'0';
				if(session_subparameter_codes.page_length <0 || session_subparameter_codes.page_length >= NUM_PAGE_LENGTHS){
					fprintf(stderr, "page length invalid\n");
					ier=1;
					goto end;
				}
				fprintf(stderr, "page_length :%s, %d mm\n", PAGE_LENGTH_TABLE[session_subparameter_codes.page_length].desc, PAGE_LENGTH_TABLE[session_subparameter_codes.page_length].millimeters);
				break;

			case 4:
				session_subparameter_codes.data_compression_format = *cptr -'0';
				if(session_subparameter_codes.data_compression_format <0 || session_subparameter_codes.data_compression_format >= NUM_DATA_COMPRESSION_FORMATS){
					fprintf(stderr, "data compression format invalid\n");
					ier=1;
					goto end;
				}
				fprintf(stderr, "data_compression_format :%s\n", DATA_COMPRESSION_FORMAT_TABLE[session_subparameter_codes.data_compression_format].desc);
				break;

			case 5:
				session_subparameter_codes.error_correction = *cptr -'0';
				if(session_subparameter_codes.error_correction <0 || session_subparameter_codes.error_correction >= NUM_ERROR_CORRECTIONS){
					fprintf(stderr, "error correction invalid\n");
					ier=1;
					goto end;
				}
				fprintf(stderr, "error_correction :%s\n", ERROR_CORRECTION_TABLE[session_subparameter_codes.error_correction].desc);
				break;

			case 6:
				session_subparameter_codes.binary_file_transfer = *cptr -'0';
				if(session_subparameter_codes.binary_file_transfer <0 || session_subparameter_codes.binary_file_transfer >= NUM_BINARY_FILE_TRANSFERS){
					fprintf(stderr, "binary file transfer invalid\n");
					ier=1;
					goto end;
				}
				fprintf(stderr, "binary_file_transfer :%s\n", BINARY_FILE_TRANSFER_TABLE[session_subparameter_codes.binary_file_transfer].desc);
				break;

			case 7:
				session_subparameter_codes.scan_time_per_line = *cptr -'0';
				if(session_subparameter_codes.scan_time_per_line <0 || session_subparameter_codes.scan_time_per_line >= NUM_SCAN_TIME_PER_LINES){
					fprintf(stderr, "scan time per line invalid\n");
					ier=1;
					goto end;
				}
				fprintf(stderr, "scan_time_per_line :%d ms\n", SCAN_TIME_PER_LINE_TABLE[session_subparameter_codes.scan_time_per_line].milliseconds[session_subparameter_codes.vertical_resolution]);
				break;

			default:
				fprintf(stderr, "session subparamters codes error. too many commas.\n");
					ier=1;
					goto end;
			}
		}else if(strlen(cptr)!=0){
			fprintf(stderr, "session subparamters codes error. Not single digit.\n");
					ier=1;
					goto end;
		}
		cptr=strtok(NULL,",");
		field++;
	}
end:
	return(ier);
}

int	decode_fhng_string(s)
char	*s;
{
	int	value;
	int	ier=0;
	strip_string(s);
	if(strlen(s)==1){
		if(sscanf(s,"%1d",&value)!= 1){
			fprintf(stderr, "hng code not an integer\n");
			ier= -1;
			goto end;
		}
	}else if(strlen(s)==2){
		if(sscanf(s,"%2d",&value)!= 1){
			fprintf(stderr, "hng code not an integer\n");
			ier= -1;
			goto end;
		}
	}else if(strlen(s)==3){
		if(sscanf(s,"%3d",&value)!= 1){
			fprintf(stderr, "hng code not an integer\n");
			ier= -1;
			goto end;
		}
	}else{
		fprintf(stderr, "hng code too long\n");
		ier= -1;
		goto end;
	}

	switch(value) {
	case 0:
		fprintf(stderr, "Normal and proper end of connection\n");
	break;
	case 1:
		fprintf(stderr, "Ring Detect without successful handshake\n");
	break;
	case 2:
		fprintf(stderr, "Call Aborted, from +FK or <CAN>\n");
	break;
	case 3:
		fprintf(stderr, "No Loop Current\n");
	break;
	case 10:
		fprintf(stderr, "Unspecified Phase A error\n");
	break;
	case 11:
		fprintf(stderr, "No Answer (T.30 T1 timeout\n");
	break;
	case 20:
		fprintf(stderr, "Unspecified Transmit Phase B error\n");
	break;
	case 21:
		fprintf(stderr, "Remote cannot receive or send\n");
	break;
	case 22:
		fprintf(stderr, "COMREC error in transmit Phase B\n");
	break;
	case 23:
		fprintf(stderr, "COMREC invalid command received\n");
	break;
	case 24:
		fprintf(stderr, "RSPEC error\n");
	break;
	case 25:
		fprintf(stderr, "DCS sent three times without response\n");
	break;
	case 26:
		fprintf(stderr, "DIS/DTC received 3 time; DCS not recognized\n");
	break;
	case 27:
		fprintf(stderr, "Failure to train at 2400 bps or +FMINSP value\n");
	break;
	case 28:
		fprintf(stderr, "RSPREC invalid response received\n");
	break;
	case 40:
		fprintf(stderr, "Unspecified Transmit Phase C error\n");
	break;
	case 43:
		fprintf(stderr, "DTE to DCE data underflow\n");
	break;
	case 50:
		fprintf(stderr, "Unspecified Transmit Phase D error\n");
	break;
	case 51:
		fprintf(stderr, "RSPREC error\n");
	break;
	case 52:
		fprintf(stderr, "No response to MPS repeated 3 times\n");
	break;
	case 53:
		fprintf(stderr, "Invalid response to MPS\n");
	break;
	case 54:
		fprintf(stderr, "No response to EOP repeated 3 times\n");
	break;
	case 55:
		fprintf(stderr, "Invalid response to EOP\n");
	break;
	case 56:
		fprintf(stderr, "No response to EOM repeated 3 times\n");
	break;
	case 57:
		fprintf(stderr, "Invalid response to EOM\n");
	break;
	case 58:
		fprintf(stderr, "Unable to continue after PIN or PIP\n");
	break;
	case 70:
		fprintf(stderr, "Unspecified Receive Phase B error\n");
	break;
	case 71:
		fprintf(stderr, "RSPREC error\n");
	break;
	case 72:
		fprintf(stderr, "COMREC error\n");
	break;
	case 73:
		fprintf(stderr, "T.30 T2 timeout, expected page not received\n");
	break;
	case 74:
		fprintf(stderr, "T.30 T1 timeout after EOM received\n");
	break;
	case 90:
		fprintf(stderr, "Unspecified Receive Phase C error\n");
	break;
	case 91:
		fprintf(stderr, "Missing EOL after 5 seconds (section 3.2/T.4)\n");
	break;
	case 93:
		fprintf(stderr, "DCE to DTE buffer overflow\n");
	break;
	case 94:
		fprintf(stderr, "Bad CRC or frame (ECM or BFT modes)\n");
	break;
	case 100:
		fprintf(stderr, "Unspecified Receive Phase D error\n");
	break;
	case 101:
		fprintf(stderr, "RSPREC invalid response received\n");
	break;
	case 102:
		fprintf(stderr, "COMREC invalid response received\n");
	break;
	case 103:
		fprintf(stderr, "Unable to continue after PIN or PIP\n");
	break;
	default:
		fprintf(stderr, "\n");
	}
end:
	return(ier);
}




t_signal alarm_sub()
{
	systim(sys_time);
	fprintf(stderr, "lpfax:%s: Failed to open device after 15 seconds\n", sys_time);
	exit(1);
}


t_signal fax_alarm_sub()
{
	fax_alarm_flag = 1;
}



void send_to(string)
char	*string;
{
	fprintf(stderr, "Send to fax :");
	control_print(string);
	fprintf(fax_out, "%s", string);
}


/* Gets chars from fax and checks against ok string */

int	wait_for(string_to_compare, delay, save_flag)
char	*string_to_compare;
int	delay, save_flag;
{
	char	*string_from_fax;
	int	len, ier, max_len, c;

	max_len = strlen(string_to_compare);
	string_from_fax = malloc((unsigned)max_len + 1);
	if (string_from_fax == NULL) {
		fprintf(stderr, "malloc failed\n");
		exit(1);
	}
	memset((char *)string_from_fax, 0, max_len + 1);

	ier = len = 0;

	fax_alarm_flag = 0;
	signal(SIGALRM, fax_alarm_sub);
	fprintf(stderr, "expecting :");
	control_print(string_to_compare);
	fprintf(stderr, "Got:");

	while (1) {
		alarm((unsigned)delay);
		c = getc(fax_in);
		alarm(0);
		if (c < ' ')
			fprintf(stderr, "^%c", c + '@');
		else if (c < '\177')
			fprintf(stderr, "%c", c);
		else
			fprintf(stderr, "\%3.3o", c);
		if (c == '\n')
			fprintf(stderr, "\n");
		if (fax_alarm_flag) {
			fprintf(stderr, "\nTIME OUT after %d sec\n", delay);
			fprintf(stderr, "Current string_from_fax:\n");
			control_print(string_from_fax);
			ier = GOT_TIMEOUT;

			break;
		}
		if (feof(fax_in)) {
			fprintf(stderr, "\nUnexpected EOF\n");
			ier = GOT_EOF;
			break;
		}
		if (len == max_len) {
			if (save_flag) {
				putc(string_from_fax[0], file_out);
			}
			strcpy(string_from_fax, &string_from_fax[1]);
			string_from_fax[max_len-1] = c;
		} else {
			string_from_fax[len++] = c;
		}
		if (str_index(string_from_fax, string_to_compare) != -1) {
			fprintf(stderr, "\nGOT IT\n");
			ier = GOT_IT;
			break;
		}
	}
	free(string_from_fax);
	return(ier);
}


/* Gets chars from fax and checks against many strings */

int	wait_for_many(delay, save_flag, va_alist)
int	delay, save_flag;
va_dcl
{
	char	*string_from_fax;
	int	len, ier, max_len, length, c, i;
	va_list	args;
	char	*allowed_answers[100];
	int	num_allowed_answers;

	max_len = 0;
	va_start(args);
	for (num_allowed_answers = 0; num_allowed_answers < 100; num_allowed_answers++) {
		allowed_answers[num_allowed_answers] = va_arg(args, char * );
		if (allowed_answers[num_allowed_answers] == NULL)
			break;
		length = strlen(allowed_answers[num_allowed_answers]);
		max_len = max_len > length ? max_len : length;
		fprintf(stderr, "expecting %d:", num_allowed_answers);
		control_print(allowed_answers[num_allowed_answers]);

	}
	va_end(args);
	if (num_allowed_answers == 0) {
		fprintf(stderr, "zero allowed answers\n");
		ier = NO_ALLOWED_ANSWERS;
		goto do_free;
	}


	string_from_fax = malloc((unsigned)max_len + 1);
	if (string_from_fax == NULL) {
		fprintf(stderr, "malloc failed\n");
		exit(1);
	}
	memset(string_from_fax, 0, max_len + 1);

	ier = len = 0;
	fax_alarm_flag = 0;
	signal(SIGALRM, fax_alarm_sub);

	fprintf(stderr, "Got:");
	while (1) {
		alarm((unsigned)delay);
		c = getc(fax_in);
		alarm(0);
		if (c < ' ')
			fprintf(stderr, "^%c", c + '@');
		else if (c < '\177')
			fprintf(stderr, "%c", c);
		else
			fprintf(stderr, "\%3.3o", c);
		if (c == '\n')
			fprintf(stderr, "\n");
		if (fax_alarm_flag) {
			fprintf(stderr, "\nTIME OUT after %d sec\n", delay);
			fprintf(stderr, "Current string_from_fax:%s:\n", string_from_fax);
			ier = GOT_TIMEOUT;
			break;
		}
		if (feof(fax_in)) {
			fprintf(stderr, "\nUnexpected EOF\n");
			ier = GOT_EOF;
			break;
		}
		if (len == max_len) {
			if (save_flag) {
				putc(string_from_fax[0], file_out);
			}
			strcpy(string_from_fax, &string_from_fax[1]);
			string_from_fax[max_len-1] = c;
		} else {
			string_from_fax[len++] = c;
		}
		for (i = 0; i < num_allowed_answers; i++) {
			if (str_index(string_from_fax, allowed_answers[i]) != -1) {
				fprintf(stderr, "\nGOT IT\n");
				ier = GOT_IT + i;
				goto do_free;
			}
		}
	}
do_free:
	free(string_from_fax);
	return(ier);
}


void hangup()
{
	sleep(3);
	fflush(fax_out);
	send_to("+++");
	fflush(fax_out);
	sleep(3);
	send_to("ATH\r");
	fflush(fax_out);
	switch (wait_for("OK\r\n", 60, 0)) {
	case GOT_IT:
		break;
	case GOT_EOF:
		exit(1);
	case GOT_TIMEOUT:
		fprintf(stderr, "fax modem not responding to ATH with OK\n");
		exit(1);
	}
}


main(argc, argv, envp)
int	argc;
char	**argv, **envp;
{
	/* execve("fax", "ttyd9", "dx_19200", "none", "LDISC0", 0, envp); */

	struct termio tty_setting;
	int	fail_count;
	int	hng_code;

	char	cig_string[31];
	char	csi_string[31];
	char	dcs_string[31];
	char	dis_string[31];
	char	dcc_string[31];
	char	dtc_string[31];
	char	et_string[31];
	char	hng_string[31];
	char	nsc_string[31];
	char	nss_string[31];
	char	pts_string[31];
	char	tsi_string[31];

	int	have_some_to_send = 0;
	int	sending = 0;
	int	polling = -1;
	int	found_fax_to_go = 0;
	int	got_fcfr;
	int	sent_control_s=0;

	main_err = 0;
	/*
	stdin = fopen("/dev/console", "r");
	stdout = fopen("/dev/console", "a");
	stderr = fopen("/dev/console", "a");
	*/

	signal(SIGALRM, alarm_sub);
	alarm(15);

	fax_out = fopen("/dev/ttyd9", "w");
	alarm(0);
	if (fax_out == NULL) {
		systim(sys_time);
		fprintf(stderr, "lpfax:%s: Error on open of /dev file %s!\n", sys_time, "/dev/ttyd9");
		perror("lpfax");
		main_err++;
	} else {
		fax_in = fopen("/dev/ttyd9", "r");
		if (fax_in == NULL) {
			systim(sys_time);
			fprintf(stderr, "lpfax:%s: Error on open of /dev file %s!\n", sys_time, "/dev/ttyd9");
			perror("lpfax");
			main_err++;
		} else {
			memset(&tty_setting, 0, sizeof tty_setting);

			tty_setting.c_iflag = IGNBRK | IGNPAR;
			tty_setting.c_cflag = CLOCAL | CNEW_RTSCTS | B19200 | CS8 | CREAD | HUPCL;


			tty_setting.c_cc[VINTR] = CINTR;
			tty_setting.c_cc[VQUIT] = CQUIT;
			tty_setting.c_cc[VERASE] = '\010';
			tty_setting.c_cc[VKILL] = '\030';
			tty_setting.c_cc[VMIN] = 1;
			tty_setting.c_cc[VTIME] = 1;

			if (ioctl(fileno(fax_in), TCSETA, &tty_setting)) {
				systim(sys_time);
				fprintf(stderr, "lpfax:%s: Error on call to ioctl\n", sys_time);
				perror("lpfax");
				main_err++;
			} else {

				file_out = fopen("/usr/gleeson/src/fax/file.out", "w");
				if (file_out == NULL) {
					systim(sys_time);
					fprintf(stderr, "lpfax:%s: Error on open of out file %s!\n", sys_time, "fax.out");
					perror("lpfax");
					main_err++;
				} else {


begin_again:
					hangup();
					fail_count = 0;
try_atz_again:
					send_to("atz\r");
					switch (wait_for("OK\r\n", 15, 0)) {
						;
					case GOT_IT:
						break;
					case GOT_EOF:
						goto bad_end;
					case GOT_TIMEOUT:
						fail_count++;
						if (fail_count > 3) {
							fprintf(stderr, "fax modem not responding to ATZ with OK\n");
							goto bad_end;
						} else {
							goto try_atz_again;
						}
					}

					send_to("AT+FLID=\"367-8767 gleeson\"\r");
					switch (wait_for("OK\r\n", 5, 0)) {
					case GOT_IT:
						break;
					case GOT_EOF:
						goto bad_end;
					case GOT_TIMEOUT:
						fprintf(stderr, "fax modem not responding to AT+FLID=# with OK\n");
						goto bad_end;
					}

					memset(&session_subparameter_codes, 0, sizeof session_subparameter_codes);

					send_to("AT+FDCC?\r");

					switch (wait_for("+FDCC", 5, 0)) {
					case GOT_IT:
						break;
					case GOT_EOF:
						goto bad_end;
					case GOT_TIMEOUT:
						fprintf(stderr, "fax modem not responding to AT+FDCC? with OK\n");
						goto bad_end;
					}
						fgets(dcc_string, 30, fax_in);
						fprintf(stderr, "dcc_string :");
						control_print(dcc_string);
						/* dcc_string has : on front and \r\j on end */
					decode_session_string(dcc_string);

					switch (wait_for("OK\r\n", 5, 0)) {
					case GOT_IT:
						break;
					case GOT_EOF:
						goto bad_end;
					case GOT_TIMEOUT:
						fprintf(stderr, "fax modem not responding to AT+FDCC? with OK\n");
						goto bad_end;
					}


					if (sending) {

			tty_setting.c_iflag &= ~IXOFF;
			tty_setting.c_iflag |= IXON;
			if (ioctl(fileno(fax_in), TCSETA, &tty_setting)) {
				systim(sys_time);
				fprintf(stderr, "lpfax:%s: Error on call to ioctl\n", sys_time);
				perror("lpfax");
				main_err++;
				goto bad_end;
			}

						send_to("AT+FCLASS=2\r");
						switch (wait_for("OK\r\n", 5, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding to AT+FCLASS with OK\n");
							goto bad_end;
						}


						send_to("atdt3677784\r");
						switch (wait_for("+FCON\r\n", 90, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding to ATDT# with +FCON\n");
							goto bad_end;
						}

						switch (wait_for("+FCSI", 20, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with +FCSI\n");
							goto bad_end;
						}
						fgets(csi_string, 30, fax_in);
						fprintf(stderr, "csi_string :");
						control_print(csi_string);
						/* csi_string has :" on front and "\r\j on end */


						switch (wait_for("+FDIS", 20, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with +FDIS\n");
							goto bad_end;
						}
						fgets(dis_string, 30, fax_in);
						fprintf(stderr, "dis_string :");
						control_print(dis_string);
						/* dis_string has : on front and \r\j on end */


						switch (wait_for("OK\r\n", 20, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with OK\n");
							goto bad_end;
						}

send_fdt:
						send_to("AT+FDT\r");
						switch (wait_for("+FDCS", 90, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding to AT+FDT with +FDCS\n");
							goto bad_end;
						}
						fgets(dcs_string, 30, fax_in);
						fprintf(stderr, "dcs_string :");
						control_print(dcs_string);
						/* dcs_string has : on front and \r\j on end */

						switch (wait_for("CONNECT\r\n", 90, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with CONNECT\n");
							goto bad_end;
						}

						/* cntrl-q */
						switch (wait_for("\021", 20, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with cntrl-q\n");
							goto bad_end;
						}



						send_to("ATH\r");
						switch (wait_for("OK\r\n", 60, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding to ATH with OK\n");
							goto bad_end;
						}

					} else {

			tty_setting.c_iflag |= IXOFF;
			tty_setting.c_iflag &= ~IXON;
			if (ioctl(fileno(fax_in), TCSETA, &tty_setting)) {
				systim(sys_time);
				fprintf(stderr, "lpfax:%s: Error on call to ioctl\n", sys_time);
				perror("lpfax");
				main_err++;
				goto bad_end;
			}

						send_to("ATQ0\r");
						switch (wait_for("OK\r\n", 5, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding to ATQ0 with OK\n");
							goto bad_end;
						}


						send_to("AT+FCR=1;+FAA=0\r");
						switch (wait_for("OK\r\n", 5, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding to AT+FCR=1;+FAA=1 with OK\n");
							goto bad_end;
						}

						send_to("AT+FCLASS=2\r");
						switch (wait_for("OK\r\n", 5, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding to AT+FCLASS with OK\n");
							goto bad_end;
						}



wait_for_ring:
						switch (wait_for("RING\r\n", 600, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with RING\n");
							fprintf(stderr, "check for out gouing faxes\n");
							if (found_fax_to_go) {
								sending = 1;
								goto begin_again;
							} else
								goto wait_for_ring;
						}

wait_for_fcon:
						switch (wait_for_many(90, 0, "+FCON", "CONNECT", "NO CARRIER", "+FHNG", NULL)) {
						case GOT_IT:
							/* +fcon */
							break;
						case GOT_IT + 1:
							/* connect */
							if (freopen("/dev/ttyd9", "a", stdout) == NULL) {
								fprintf(stderr, "Can't reopen stdout as /dev/ttyd9.\n");
								exit(1);
							}
							if (freopen("/dev/ttyd9", "r", stdin) == NULL) {
								fprintf(stderr, "Can't reopen stdout as /dev/ttyd9.\n");
								exit(1);
							}
							if (freopen("/dev/ttyd9", "a", stderr) == NULL) {
								fprintf(file_out, "Can't reopen stderr as /dev/ttyd9\n");
								exit(1);
							}
							fclose(fax_in);
							fclose(fax_out);
							nointer();
							argv[0] = getty;
							execve("/etc/getty", argv, envp);
							fprintf(file_out, "errno:%d:\n", errno);
							fflush(file_out);
							fclose(file_out);
						case GOT_IT + 2:
							/* no carrier */
							goto begin_again;
						case GOT_IT + 3:
							/* +FHNG */
							fgets(hng_string, 30, fax_in);
							fprintf(stderr, "hng_string :");
							control_print(hng_string);
							hng_code = decode_fhng_string(hng_string);
							goto bad_end;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with +FCON or CONNECT\n");
							goto bad_end;
						}


						polling = -1;
						strcpy(dcs_string, "");
						strcpy(dtc_string, "");
wait_for_dcs_dtc:
						switch (wait_for_many(40, 0, "+FNSS", "+FTSI", "+FDCS", "+FNSC", "+FCIG", "+FDTC", "OK", "+FHNG", NULL)) {
						case GOT_IT:
							/* +FNSS */
							if (polling == 1) {
								fprintf(stderr, "Can't switch from polling to recving\n");
								goto bad_end;
							}
							polling = 0;
							fgets(nss_string, 30, fax_in);
							fprintf(stderr, "nss_string :");
							control_print(nss_string);
							goto wait_for_dcs_dtc;
						case GOT_IT + 1:
							/* +FTSI */
							if (polling == 1) {
								fprintf(stderr, "Can't switch from polling to recving\n");
								goto bad_end;
							}
							polling = 0;
							fgets(tsi_string, 30, fax_in);
							fprintf(stderr, "tsi_string :");
							control_print(tsi_string);
							/* tsi_string has :" on front and "\r\j on end */
							goto wait_for_dcs_dtc;
						case GOT_IT + 2:
							/* +FDCS */
							if (polling == 1) {
								fprintf(stderr, "Can't switch from polling to recving\n");
								goto bad_end;
							}
							polling = 0;
							fgets(dcs_string, 30, fax_in);
							fprintf(stderr, "dcs_string :");
							control_print(dcs_string);
							/* dcs_string has : on front and \r\j on end */
							decode_session_string(dcs_string);
							goto wait_for_dcs_dtc;
						case GOT_IT + 3:
							/* +FNSC */
							if (polling == 0) {
								fprintf(stderr, "Can't switch from recving to polling\n");
								goto bad_end;
							}
							polling = 1;
							fgets(nsc_string, 30, fax_in);
							fprintf(stderr, "nsc_string :");
							control_print(nsc_string);
							goto wait_for_dcs_dtc;
						case GOT_IT + 4:
							/* +FCIG */
							if (polling == 0) {
								fprintf(stderr, "Can't switch from recving to polling\n");
								goto bad_end;
							}
							polling = 1;
							fgets(cig_string, 30, fax_in);
							fprintf(stderr, "cig_string :");
							control_print(cig_string);
							goto wait_for_dcs_dtc;
						case GOT_IT + 5:
							/* +FDTC */
							if (polling == 0) {
								fprintf(stderr, "Can't switch from recving to polling\n");
								goto bad_end;
							}
							polling = 1;
							fgets(dtc_string, 30, fax_in);
							fprintf(stderr, "dtc_string :");
							control_print(dtc_string);
							goto wait_for_dcs_dtc;
						case GOT_IT + 6:
							/* OK */
							switch (polling) {
							case -1:
								fprintf(stderr, "Can't decide which : recving or polling\n");
								goto bad_end;
							case 0:
								if (strcmp(dcs_string, "") == 0) {
									fprintf(stderr, "recving must have FDCS\n");
									goto bad_end;
								}
								break;
							case 1:
								if (strcmp(dtc_string, "") == 0) {
									fprintf(stderr, "polling must have FDTC\n");
									goto bad_end;
								}
								/* POLLING */
								/* do we have any to send */
								have_some_to_send = 0;
								if (have_some_to_send) {
									goto send_fdt;
								} else {
									hangup();
									goto good_end;
								}
							}
							break;
						case GOT_IT + 7:
							/* +FHNG */
							fgets(hng_string, 30, fax_in);
							fprintf(stderr, "hng_string :");
							control_print(hng_string);
							hng_code = decode_fhng_string(hng_string);
							goto bad_end;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with +FTSI\n");
							goto bad_end;
						}


						fprintf(stderr, "begin recv page commands\n");

send_fdr:
						send_to("AT+FDR\r");

						got_fcfr = 0;
wait_for_fcfr_connect:
						switch (wait_for_many(120, 0, "+FCFR", "+FNSS", "+FTSI", "+FDCS", "CONNECT\r\n", "+FHNG", NULL)) {
						case GOT_IT:
							/* +FCFR */
							got_fcfr = 1;
							goto wait_for_fcfr_connect;
						case GOT_IT + 1:
							/* +FNSS */
							fgets(nss_string, 30, fax_in);
							fprintf(stderr, "nss_string :");
							control_print(nss_string);
							goto wait_for_fcfr_connect;
						case GOT_IT + 2:
							/* +FTSI */
							fgets(tsi_string, 30, fax_in);
							fprintf(stderr, "tsi_string :");
							control_print(tsi_string);
							/* tsi_string has :" on front and "\r\j on end */
							goto wait_for_fcfr_connect;
						case GOT_IT + 3:
							/* +FDCS */
							fgets(dcs_string, 30, fax_in);
							fprintf(stderr, "dcs_string :");
							control_print(dcs_string);
							goto wait_for_fcfr_connect;
						case GOT_IT + 4:
							/* CONNECT */
							if (!got_fcfr) {
								fprintf(stderr, "recving must have FCFR\n");
								goto bad_end;
							}
							break;
						case GOT_IT + 5:
							/* +FHNG */
							fgets(hng_string, 30, fax_in);
							fprintf(stderr, "hng_string :");
							control_print(hng_string);
							hng_code = decode_fhng_string(hng_string);
							if (hng_code== 72) {
								goto send_fdr;
							}
							goto bad_end;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding to AT+FDR with +FCFR\n");
							goto bad_end;
						}


						send_to("\022"); /* dc2 cntrl-r */
						fprintf(stderr, "read data now\n");

						/* del etx */
						sent_control_s=0;

wait_for_del_etx:
						switch (wait_for_many(600, 1, "\020\020", "\020\003", NULL)) {
						case GOT_IT:
							/* del del -> del */
							putc("\020", file_out);
							sent_control_s=1;
							send_to("\023"); /* dc3 cntrl-s */
							goto wait_for_del_etx;
						case GOT_IT+1:
							/* del etx */
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with CONNECT\n");
							goto bad_end;
						}

						switch (wait_for("+FPTS", 40, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with CONNECT\n");
							goto bad_end;
						}
						fgets(pts_string, 30, fax_in);
						fprintf(stderr, "pts_string :");
						control_print(pts_string);

						switch (wait_for("+FET", 20, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with CONNECT\n");
							goto bad_end;
						}
						fgets(et_string, 30, fax_in);
						fprintf(stderr, "et_string :");
						control_print(et_string);

						switch (wait_for("OK\r\n", 20, 0)) {
						case GOT_IT:
							break;
						case GOT_EOF:
							goto bad_end;
						case GOT_TIMEOUT:
							fprintf(stderr, "fax modem not responding with CONNECT\n");
							goto bad_end;
						}



					}




					goto good_end;
bad_end:
					main_err++;
good_end:
					fclose(file_out);
				}
			}
			fclose(fax_in);
		}
		fflush(fax_out);
		fclose(fax_out);
	}
	return(main_err);
}


