/*
 * Copyright Milan Technology Corp. 1991,1992,1993,1994,1995
 */

/*
 * fpfilter.c -- FastPort communications interface program. See man page for
 * complete set of options generates fpcomm for Adobe compatibility
 */
 /* Fix for SCO, use socket in non-blocking mode 2/93 
  * Support for generating banners 4/93
  * Added support for accounting on serial port 11/93
  * Added support for getting correct status for lpq 11/93
  * Fixed core dump crash during busy waits, 2/94 
  * Check for busy serial port (send a control-T) 2/94
  * Added support for 3200 ports, 5/94
  * Added support for using either hostname or ip address 9/94
  * Added support for 3200 multiple port status 
 */

char           *VERSION = "@(#)fpfilter.c	3.21 07/24/95";

#include "std.h"
#include "dp.h"
#include "errors.h"
#include "udp.h"
#if defined(LPD) || defined(SLOWSCO)
#ifndef ULTRIX
#include <poll.h>
#endif
#endif
char cmd_string[255];

#ifdef LPD
/* Structure for holding the accounting stuff */

struct acct {
	   char  *username ;
	   char  *hostname ;
	   char  *filename ;
	   } acctg ;


char *getdateforme() ;  /* a function prototype */
char *get_no_of_pages() ;
long strip();
#endif

char           *g_filter_name;	/* The invocation name */
s_options       g_opt;		/* the options struct */
FILE           *g_errorLog=0;	/* log file for errors */

long            fin_bytes;	/* Total number of bytes */
long            act_bytes_sent = 0;	/* Number of bytes actually sent */
long            int_bytes;	/* Number of par bytes initially */
int             supp_output = 0; /* Suppress display */


/* Strings for accounting information */

char            endbuffer[10] ; 
char            start[10] ;
char            *progname ;

/* The following is the string that needs to be sent to the PostScript printer
   to make it return the number of pages printed so far
*/

static char ps_indicator[] = "%! \n" ;
static char pagecount_string[]  =
"statusdict begin pagecount end 20 string cvs print flush \n" ;

/* UDP Status packet type */

extern udp_status_packet udp_status_pkt;
extern udpParStatusV2 *udpStatus;
extern udpStatusV2    *udpstatv2;

int             status_id = 0;

/* SIGALARM interrupt handler. */
int             g_alarmed = 0;
#ifdef ANSI
void
to_alarm(void)
#else
void
to_alarm()
#endif
{
#ifndef ESIX
	signal(SIGALRM, to_alarm);
#endif
	g_alarmed = 1;
}

#ifdef ANSI
char           *
find_ascii2ps(void)
#else
char           *
find_ascii2ps()
#endif
{
	struct stat     stat_buf;

	if (!stat("/usr/lib/ps/pstext", &stat_buf))
		return ("/usr/lib/ps/pstext");
	if (!stat("/usr/local/lib/ps/pstext", &stat_buf))
		return ("/usr/local/lib/ps/pstext");
	return ("a2ps");
}

#ifdef ANSI
int
get_file_type(int fd, unsigned char *first_chars, int *numchars)
#else
int
get_file_type(fd, first_chars, numchars)
	int             fd;
	unsigned char  *first_chars;
	int            *numchars;
#endif
{
	if ((*numchars = read(fd, first_chars, MAX_MAGIC_HEADER)) < 0)
		error_notify(ERR_MAGIC, "get_file_type: error reading magic number");
	if ( (!strncmp(first_chars, "%!", 2)) || (!strncmp(first_chars,"%!",3)))
		return (POSTSCRIPT);

	return (ASCII);
}

#ifdef ANSI
void
add_file(file_obj_ptr * flist, char *file_to_add)
#else
void
add_file(flist, file_to_add)
	file_obj_ptr   *flist;
	char           *file_to_add;
#endif
{
	file_obj_ptr    node, runner;
	node = (file_obj_ptr) malloc(sizeof(file_obj_t));
	node->next = 0;
	strncpy(node->file_name, file_to_add, MAXNAMELEN);

	if (!*flist)
		*flist = node;
	else {
		runner = *flist;
		while (runner->next)
			runner = runner->next;
		runner->next = node;
	}
}

/* =======Start gleeson code========================================================================= */

int	next_page(int sock, char *printer_name, int output_dest)
{
	register int amt;
	char	mess[200];

	/* increment to next page */
	if (isam_find_lock_read(flpdev, lpdev, &lpdev_rec) <= 0l) {
		systim(sys_time);
		fprintf(stderr, "fpfilter:%s:%s:error on read of lpdev\n", lpdev, sys_time);
		perror("fpfilter");
		return(1);
	}
	if (lpdev_rec.stop_printing) {
		sprintf(mess, "\n\n Printer stopped.\n\n");
		amt=strlen(mess);
		write_buffer(sock, mess, amt, printer_name, output_dest);
		return(1);
	}
	strcpy(lpdev_rec.printing_lpspool, lpspool);
	lpdev_rec.printing_lp_item = lp_item;
	lpdev_rec.pages_printed_current_copy++;
	time(&lpdev_rec.last_print_time);
	lp_line = 0;
	if (isam_insert_write_unlock(flpdev, lpdev, &lpdev_rec) <= 0l) {
		systim(sys_time);
		fprintf(stderr, "fpfilter:%s:%s:error on write of lpdev\n", lpdev, sys_time);
		perror("fpfilter");
		return(1);
	}
	if (isam_find_read(flpspool, lpspool, &lpspool_rec) <= 0l) {
		systim(sys_time);
		fprintf(stderr, "fpfilter:%s:%s:error on read of lpspool\n", lpdev, sys_time);
		perror("fpfilter");
		return(1);
	}
	if (lpspool_rec.hold) {
		sprintf(mess, "\n\n Job Put on Hold.\n\n");
		amt=strlen(mess);
		write_buffer(sock, mess, amt, printer_name, output_dest);
		return(1);
	}
	if (lpdev_rec.copies_printed >= lpspool_rec.copies_requested[lp_item]) {
		if (lpspool_rec.copies_requested[lp_item] == 0) {
			sprintf(mess, "\n\n Job Purged.\n\n");
			amt=strlen(mess);
			write_buffer(sock, mess, amt, printer_name, output_dest);
		}
		return(1);
	}
	return(0);
}


int	next_line(int sock, char *printer_name, int output_dest)
{
	register int amt;
	char	mess[200];

	if (isam_find_read(flpdev, lpdev, &lpdev_rec) <= 0l) {
		systim(sys_time);
		fprintf(stderr, "fpfilter:%s:%s:error on read of lpdev\n", lpdev, sys_time);
		perror("fpfilter");
		return(1);
	}
	if (lpdev_rec.stop_printing) {
		sprintf(mess, "\n\n Printer stopped.\n\n");
		amt=strlen(mess);
		write_buffer(sock, mess, amt, printer_name, output_dest);
		return(1);
	}
	lp_line++;
	if (lp_line >= lpdev_rec.lines_per_page) {
		return(next_page(sock, printer_name, output_dest));
	}
	return(0);
}


/* =======End gleeson code========================================================================= */


















#ifdef ANSI
int             sendfile
                (int sock, char *file_to_send, int output_dest, int check_postscript, char *printer_name, int from_lpspool)
#else
int
sendfile(sock, file_to_send, output_dest, check_postscript, printer_name, from_lpspool)
	int             sock;
	char           *file_to_send;
	int             output_dest;
	int             check_postscript;
	char           *printer_name;
	int		from_lpspool;
#endif
{
	int             fd;
	int             numchars, num_sent, total_sent;
	int             length, filetype;
	char            inputbuffer[MAX_BUFFER];
	char           *databuffer;
	char            ff = 12;
	char            first_chars[MAX_MAGIC_HEADER];
	char            error_string[MAXSTRNGLEN];
	int             write_error_sent = 0;
	int             i;

	if (strcmp(file_to_send, "STDIN")) {
		if ((fd = open(file_to_send, O_RDONLY, 0400)) < 0)
			return (1);
	} else
		fd = 0;

	filetype = get_file_type(fd, first_chars, &numchars);
#ifdef LPD
#ifndef ULTRIX

	    if (( g_opt.port_type == SERIAL_CHAN_TYPE ) && 
		(g_opt.use_control_d ) && (g_opt.acctg ))  {
	      /* We need to perform accounting. Therefore get the page count now 
		 To do this, we send a postscript string, which would return the number of
		 pages written so far. At the end of the print job, we send the same string
		 again and get the number of pages written then. The difference between the
		 two becomes the number of pages actually printed.
		 */
	      get_acctg_info(sock,printer_name,output_dest,start);
	    }
#endif
#endif
	if (check_postscript && filetype == ASCII) {
		/*
		 * check for PSTEXT added for transcript compatibility--
		 * transcript already looks for PSTEXT
		 */
		if (!g_opt.asciifilter)
			if (!(g_opt.asciifilter = (char *) getenv("PSTEXT")))
				if (!(g_opt.asciifilter = (char *) getenv("ASCII2PS")))
					g_opt.asciifilter = find_ascii2ps();
		fd = do_translate(fd, g_opt.asciifilter);
		filetype = POSTSCRIPT;
	} else {
		char           *databuffer = first_chars;
		if (g_opt.mapflg)
			databuffer = expand_buffer(first_chars, numchars, &numchars);
#ifndef SLOWSCO
		/* Perform normal write operation */
		write_buffer(sock, databuffer, numchars, printer_name, output_dest);
#ifdef SCO
		check_write(sock);
#endif

#else
		/*
		 * write only onr byte to avoid any flow control problem
		 * which SCO seems to handle pretty badly
		 */
		for (i = 0; i < numchars; i++) {
			check_write(sock);
			write_buffer(sock, &databuffer[i], 1, printer_name, output_dest);
		}
#endif
	      }
	if (filetype == ASCII && g_opt.use_control_d)
	  error_notify(ERR_ASCII, 0);

	while ((numchars = read(fd, inputbuffer, MAX_BUFFER)) > 0) {

/* ==========Start Gleeson Code ====================================================================== */
		if(from_lpspool){
		for (i = 0; i < numchars; i++) {
			if(inputbuffer[i]=='\n'){
				if (next_line(sock, printer_name, output_dest)){
					close(fd);
					return(-1);
				}
			}
			if(inputbuffer[i]=='\f'){
				if (next_page(sock, printer_name, output_dest)){
					close(fd);
					return(-1);
				}
			}
		}
		}
/* ==========End Gleeson Code ====================================================================== */
		total_sent = 0;

		databuffer = (g_opt.mapflg) ?
			expand_buffer(inputbuffer, numchars, &numchars) : inputbuffer;
#ifndef SLOWSCO
		/* Perform normal write operation */
		write_buffer(sock, databuffer, numchars, printer_name, output_dest);
		check_input(sock, output_dest, 0);
#ifdef SCO
		check_write(sock);
#endif

#else
		/*
		 * write only one byte to avoid any flow control problem
		 * which SCO seems to handle pretty badly
		 */
		for (i = 0; i < numchars; i++) {
			check_write(sock);
			write_buffer(sock, &databuffer[i], 1, printer_name, output_dest);
		}
#endif
	}
	check_input(sock, output_dest, 2);
	if (g_opt.ff_flag) {
#ifdef SCO
	  while ( 1 ) {
		  write(sock, &ff, 1);
		  if (errno == EWOULDBLOCK)
			 sleep(5);
		  else
			 break ;
	  }
#else
#ifdef ESIX
	  alarm(0);
#endif
      write(sock,&ff, 1);
#endif
      act_bytes_sent += 1;
     }
	close(fd);
	return (numchars);
}

/*
 * write a buffer to the socket sock On blocks it checks to see what error
 * has occurred through both the data channel and udp status All alarms and
 * alarm logic are handled here. Note--the alarm handling routine re-arms the
 * timer alarm
 */

#ifdef ANSI
void
write_buffer(int sock, char *databuffer, int numchars, char *printer_name, int output_dest)
#else
void
write_buffer(sock, databuffer, numchars, printer_name, output_dest)
	int             sock;
	char           *databuffer;
	int             numchars;
	char           *printer_name;
	int             output_dest;
#endif
{
	char            error_string[MAXSTRNGLEN];
	int             total_sent = 0;
	int             num_sent;
	int             orig_total;

#ifndef ESIX
	signal(SIGALRM, to_alarm);
#endif

#ifdef BSD
	alarm(60);
#endif
	orig_total = numchars;
#ifdef ESIX
	alarm(0);
#endif
	while ((num_sent = write(sock, databuffer + total_sent, numchars)) != numchars) {
		if (errno != EINTR && num_sent < 0)
			error_notify(ERR_WRITE, 0);
#ifdef SCO
		if (errno == EWOULDBLOCK)
		   sleep(5);
#endif
		if (errno == EINTR) {
			error_string[0] = 0;
#ifdef LPD
			update_status_file(error_string, status_id, CREATE);
#endif
			error_notify(ERR_GENERIC, error_string);
			check_input(sock, output_dest, 0);
			continue;
		}
		check_input(sock, output_dest, 0);
		if (num_sent > 0) {
			numchars -= num_sent;
			total_sent += num_sent;
		}
#ifdef ESIX
		alarm(0);
#endif
#ifdef BSD
		if (numchars == 0) {
			break;
		} else
			alarm(10);
#endif
	}
#ifdef BSD
	alarm(0);		/* turn off the alarm after the write */
#endif

	/* Update # of characters sent so far */

	act_bytes_sent += orig_total;
}


/*
 * simply checks the read side of sock to see if there is any data to read
 * back and writes it to output_dest
 */

#ifdef ANSI
void
check_input(int sock, int output_dest, int time_out)
#else
void
check_input(sock, output_dest, time_out)
	int             sock, output_dest, time_out;
#endif
{
	struct timeval  timeout;
	fd_set          readfd;
	char            inchar;
	int             num_chars;
	int             sel_val;
	char            buf[MAX_BUFFER];
	char            error_string[MAXSTRNGLEN];
	int             count = 0;

	FD_ZERO(&readfd);

	timeout.tv_sec = time_out;
	timeout.tv_usec = 0;
	FD_SET(sock, &readfd);
	while (sel_val = select(sock + 1, &readfd, 0, 0, &timeout)) {
		if (sel_val < 0)
			break;
		if (FD_ISSET(sock, &readfd)) {
			if ((num_chars = read(sock, &inchar, 1)) >= 0) {
				if (count < MAX_BUFFER - 1)
					buf[count++] = inchar;
				else {
					buf[count] = (char) 0;
					error_notify(ERR_GENERIC, buf);
					count = 0;
				}
			} else
				break;
		}
	}
	if ( (count > 0) && (supp_output == 0 )) {
		buf[count] = (char) 0;
		error_notify(ERR_GENERIC, buf);
	}
}

#ifdef ANSI
void
send_control_d(int sock)
#else
void
send_control_d(sock)
	int             sock;
#endif
{
	char            dchar = 4;	/* control-d if a postscript printer */
#ifdef SCO
  while ( 1 ) {
	  write(sock, &dchar, 1);
	  if (errno == EWOULDBLOCK)
		 sleep(5);
	  else
		 break;
  }
#else
#ifdef ESIX
  alarm(0);
#endif
  write(sock, &dchar, 1);
#endif
  act_bytes_sent += 1;
}

#ifdef ANSI
char           *
expand_buffer(char *data, int length, int *retlength)
#else
char           *
expand_buffer(data, length, retlength)
	char           *data;
	int             length;
	int            *retlength;
#endif
{
	static char     return_buff[2 * MAX_BUFFER];
	int             temp, i;
	for (i = 0, temp = 0; i < length; i++) {
		if (data[i] == '\n') {
			return_buff[temp++] = '\r';
			return_buff[temp++] = '\n';
		} else {
			return_buff[temp++] = data[i];
		}
	}
	*retlength = temp;
	return (return_buff);
}

/*
 * send_banner() we need this since transcript writes a file .banner that we
 * need to write to the printer
 * 
 * ADOBE: assume that previous filters will handle BANNERPRO=file and write it
 * to .banner
 */

#ifdef ANSI
void
send_banner(int sock)
#else
void
send_banner(sock)
	int             sock;
#endif
{
	int             bfile;
	char            input_b[512];
	int             c_read, total = 0, num_sent = 0;

	if ((bfile = open(".banner", O_RDONLY, 0600)) < 0)
		return;
	while ((c_read = read(bfile, input_b, 512)) > 0) {
		act_bytes_sent += c_read;
#ifdef ESIX
        alarm(0);
#endif
		while ((num_sent = write(sock, input_b + total, c_read))
		       != c_read) {
			if (num_sent < 0)
				return;
			if (!num_sent)
				return;
			total += num_sent;
			c_read -= num_sent;
#ifdef ESIX
            alarm(0);
#endif
		}
	}
  unlink(".banner");
}

#ifdef ANSI
int
is_ascii_file(int fd, char *first_chars)
#else
int
is_ascii_file(fd, first_chars)
	int             fd;
	char           *first_chars;
#endif
{
	if (read(fd, first_chars, 2) < 0)
		error_notify(ERR_MAGIC, 0);
	return (strncmp(first_chars, "%!", 2));
}

/*
 * tries to execute the appropriate translation program: Any error causes the
 * whole job to abort
 */

#ifdef ANSI
int
do_translate(int fd, char *program)
#else
int
do_translate(fd, program)
	int             fd;
	char           *program;
#endif
{
	char           *psprogname;
	int             pipe_fd[2], pid;

	if (psprogname = (char *) strrchr(program, '/'))
		psprogname++;
	else
		psprogname = program;

	if (pipe(pipe_fd) < 0)
		error_notify(ERR_PIPE, 0);

	if ((pid = fork()) < 0)
		error_notify(ERR_FORK, 0);
	else if (pid > 0) {
		/* parent */
		close(pipe_fd[writeEnd]);
		return (pipe_fd[readEnd]);
	} else {
		/* child */
		/* make childs stdin come from fd */
		if (fd != STDIN_FILENO) {
			dup2(fd, STDIN_FILENO);
			close(fd);
		}
		/* make childs stdout go to the write end of the pipe */
		if (pipe_fd[writeEnd] != STDOUT_FILENO) {
			dup2(pipe_fd[writeEnd], STDOUT_FILENO);
			close(pipe_fd[writeEnd]);
		}
		rewind(stdin);

		execl("/bin/sh", "sh", "-c", program, (char *) 0);
		error_notify(ERR_FORK, 0);
	}
}

/*
 * Get the next printer from the linked list of printers and return the
 * printer in first argument. Returns a zero if all OK, else returns -1.
 */

#ifdef ANSI
int
getNextPrinter(hsw_PCONFIG ** current_ptr, struct sockaddr_in * server)
#else
int
getNextPrinter(current_ptr, server)
	hsw_PCONFIG   **current_ptr;
	struct sockaddr_in *server;
#endif
{
	static int      valid_host_name = 0;
	struct hostent *hp;
	unsigned long cmp;
	char error_string[MAXSTRNGLEN];

	if (!*current_ptr) 
	  *current_ptr = g_opt.prt_list;
	else {
	  *current_ptr = (*current_ptr)->next_printer;
	  if (!*current_ptr) 
	    *current_ptr = g_opt.prt_list;
	}
	while (*current_ptr) {
	  if (check_type((*current_ptr)->printer_name)) {
		if (hp = gethostbyname((*current_ptr)->printer_name)) {
			valid_host_name++;
			bcopy((char *) hp->h_addr, (char *) &server->sin_addr, hp->h_length);
			server->sin_port = htons((*current_ptr)->dataport);
			setPortOpt( (*current_ptr)->dataport );
			return 0;
		} else {
			sprintf(error_string, "host name lookup failed for %s",
				(*current_ptr)->printer_name);
			error_notify(ERR_GENERIC, error_string);
			if (!(*current_ptr)->next_printer) {
				if (!valid_host_name) {
					error_notify(ERR_GENERIC, "fpfilter: exit, no valid hosts specified");
					exit(1);
				}
			}
		 }
		}
		else {
		   /* It is an IP Address */
		   cmp = inet_addr((*current_ptr)->printer_name);
		   bcopy((char *)&cmp, (char *) &server->sin_addr,4);
		   server->sin_port = htons((*current_ptr)->dataport);
		   setPortOpt( (*current_ptr)->dataport );
		   return 0;
                }
		*current_ptr = (*current_ptr)->next_printer;
		if (!*current_ptr)
			*current_ptr = g_opt.prt_list ;
	}
	return -1;
}

#ifdef ANSI
main(int argc, char **argv)
#else
main(argc, argv)
	int             argc;
	char          **argv;
#endif
{
	struct sockaddr_in server;
	hsw_PCONFIG    *current_ptr;	/* Current printer */
	char            c, *name, *temp, *cp;
	char           *Autoconfig;
	char            error_string[MAXSTRNGLEN];
	int             val;
	int             counter = 0;
#ifdef LPD
#ifndef ULTRIX
	struct pollfd   fds[1] ;
	char            buffer[250] ;
#endif
#endif
	int             ret       ;
	FILE            *fpz      ;


	/*
	 * set to stdout or stderr depending on from where the program is
	 * called
	 */
	int             output_dest;

	int             sock, sockops, ser_sock, save ;
#if defined (SOLARIS)
        char            optval ;
#else
        int             optval ;
#endif
	int             was_waiting_for_printer = 0;
	int		got_status = 0;

#if defined (SOLARIS) || defined(HPOLD) || defined (ATT)
	char             lingerval = 1;
#else
	struct linger   linger_struct;
#endif

#ifdef DEBUG
	if (!access("/tmp/fpdebug", F_OK)) {
		syslog(LOG_ERR, "%s %d\n", argv[0], getpid());
		pause();
	}
#endif
	progname = argv[0] ;

#ifndef ESIX
#ifdef BSD
	signal(SIGALRM, to_alarm);
	signal(SIGCLD, sig_child);
#else
	signal(SIGCLD, SIG_IGN);
#endif
#endif

	setDefaults(); 

	checkForCurrentDir(argc, argv);

	hsw_Getprinterconfig();

	flpdev = open(LPDEV_FILENAME,2);
	if (flpdev < 0) {
		systim(sys_time);
		fprintf(stderr, "fpfilter:%s:%s: Can't open file %s!\n", lpdev, sys_time, LPDEV_FILENAME);
		perror("fpfilter");
		exit(1);
	}
	flpspool = open(LPSPOOL_FILENAME,2);
	if (flpspool < 0) {
		systim(sys_time);
		fprintf(stderr, "fpfilter:%s:%s Can't open file %s!\n", lpdev, sys_time, LPSPOOL_FILENAME);
		perror("fpfilter");
		exit(1);
	}


#ifdef LPD	

	if (g_opt.acctg ) {
	/* Accounting has been enabled. Hence parse command line arguments 
	   This would work only if the input filter is not a script, but is
	   a program by itself
        */
	parsecommandlineargs(argc,argv);
	}
#endif
	/* If an error file needs to be opened, do so */
	if (g_opt.notify_type.file)
		if (!(g_errorLog = fopen(g_opt.notify_type.filename, "a")))
			g_opt.notify_type.file = 0;

	/* override with command line options */
	output_dest = parseCommandLineArgs(argc, argv);

	if (!g_opt.prt_list) {
		char            printer_name[MAXNAMELEN];
		get_printername(printer_name);
		g_opt.prt_list =
			form_printer_list(printer_name,
				    g_opt.prt_list, g_opt.dataport, APPEND);
	}
	if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0)
		error_notify(ERR_DOMAIN, 0);
	server.sin_family = AF_INET;
	current_ptr = 0;
	if (getNextPrinter(&current_ptr, &server) == -1)
		error_notify(ERR_NORESPONSE, 0);
	g_alarmed = 0;
#ifdef LPD
	if (( fpz = fopen("status","r")) != NULL ) {
	  backupstatus("status", ".status");
	  fclose(fpz);
	}
#endif
	for (;;) {		/* loop as long as we don't get a fatal error
				 * this allows engine errors to be ridden
				 * through */
		char            status_message[MAXSTRNGLEN];
		char            temp_printer[MAXSTRNGLEN];
		int             connect_error_sent = 0;
#ifdef LPD
		sprintf(status_message, "printing to %s on %s\n",
			g_opt.port_id_string,
			current_ptr->printer_name);
		update_status_file(status_message, status_id++, CREATE);
		strcpy(temp_printer, current_ptr->printer_name);
		alarm(30);
#endif
#if 0
		error_string[0] = 0 ;
		get_printer_status(current_ptr->printer_name, error_string, g_opt.dataport);
#endif
#ifdef LPD
		/* Save the current status file as .status */
		if (got_printer_error(error_string)) {
		  if (( fpz = fopen("./status","w")) != NULL) {
		    fprintf(fpz,"%s", error_string);
		    fclose(fpz);
		  }
         fprintf(stderr,error_string);
		}
#endif
		if (connect(sock, (struct sockaddr *) & server, sizeof(server)) >= 0) {
		  break;  /* success - break out of for (;;) */
		}
#ifdef LPD
		alarm(0);
#endif
		close(sock);
		if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0)
		  error_notify(ERR_DOMAIN, 0);
		was_waiting_for_printer = 1;
#if 0
		sprintf(error_string, "Error on connect (errno = %d)\nPrinter Status from Fastport:\n", errno);
		error_string[0] = 0 ;
		get_printer_status(current_ptr->printer_name, error_string, g_opt.dataport);
#endif
#ifdef LPD
		update_status_file(error_string, status_id, APPEND);
		if (!connect_error_sent) {
		  connect_error_sent = 1;
		  error_notify(ERR_GENERIC, error_string);
		}
#else
		if (!connect_error_sent) {
		  if (got_printer_error(error_string)) {
		    connect_error_sent = 1;
		    sprintf(cmd_string,"Error on printer %s : %s ",
			    current_ptr->printer_name,error_string);
		    error_notify(ERR_GENERIC, cmd_string);
		  }
		}
#endif
		if (getNextPrinter(&current_ptr, &server) == -1) {
		  error_notify(ERR_NORESPONSE,0);
		  exit(1);
		}
	      }  /* matches for (;;) */
#ifdef SCO
	/* Set the socket to be Non-Blocking in case of SCO */
	if ((fcntl(sock, F_SETFL, FNDELAY )) < 0 )
	  fprintf(stderr,"Socket option setting error \n");
#endif

	/* Now we are connected.  If a PS printer is connected to the serial port,
	   then we can check to see whether anything is connected to the printer OR not.
	   */
#ifndef ULTRIX
#ifdef LPD
	if ((g_opt.port_type == SERIAL_CHAN_TYPE ) && 
	    (g_opt.use_control_d) && (g_opt.checkserial)) {
	  /* send a control-T and wait for something to come back from the serial
	     port.  If nothing comes back, keep waiting and keep sending messages
	     that the printer is not present.
	     */
	  char            dchar = 20;	/* control-t if a postscript printer */
	  int             not_ready = 1 ;
	  while (not_ready) {
	    write(sock,ps_indicator,sizeof(ps_indicator));
	    write(sock, &dchar, 1);
	    fds[0].fd = sock ;   /* socket to wait on */
	    fds[0].events = POLLIN ; /* wait for the message for 5 seconds */
	    if ( ( ret = poll(fds,1,5000) ) >= 0 ) {
	      /* The call timed out after 30 seconds or there was an error */
	      if (ret == 0) 
		     printf("Printer may not be connected or may need attention...... \n");
	      else {
		    if ((ret = read(sock, buffer,200)) > 0) {
		        not_ready = 0 ;
		        supp_output = 1; 
		    }
		else
		    error_notify(ERR_GENERIC,"error on read.... ");
	    }
	    }
	    else
	      printf("Printer may not be connected or may need attention...... \n");
	    act_bytes_sent += 1;
	  }
	}
#endif
#endif
	if ((g_opt.port_type == PARALLEL_CHAN_TYPE) && (g_opt.checkparallel)) {
	  while (TRUE) {
	    /* Check the parallel port status. If it is OFFLINE or FAULT etc, keep
	       waiting, continuously displaying message that printer has an error.
	    */
	    error_string[0] = 0 ;
	    get_printer_status(current_ptr->printer_name, error_string, g_opt.dataport);
	    if (got_printer_error(error_string)) {
	      printf("Printer may need attention...., please check printer \n");
	      sleep(60);
	    }
	    else
	      break;
	  }
	}

#ifdef LPD
	backupstatus(".status","status");
	alarm(0);
#endif

	status_id++;
#ifdef LPD
	if (was_waiting_for_printer) {
	  sprintf(error_string, "printing to %s on %s\n",
		  g_opt.port_id_string,
		  current_ptr->printer_name);
	  update_status_file(error_string, status_id, CREATE);
	}
#endif

	if (g_opt.closewait) {
	  /*
	   * Now that you have connected to the printer, check the printer
	   * status
	   */
	  error_string[0] = 0;
	  if (!get_printer_status(current_ptr->printer_name, error_string, g_opt.dataport))
	    got_status = 0;
	  else {
	    got_status = 1;
	    /* Check the number of bytes written so far on the serial and
	     * parallel port */
	    
	    if (g_opt.port_type == PARALLEL_CHAN_TYPE) {
	      if (udpstatv2->compat != 0x0ffffffff) 
		/* Initial # of parallel bytes */
		int_bytes = ntohl(udp_status_pkt.parallel_bytes);	
	      else
		int_bytes = ntohl(udpStatus->bytes);
	    }
	    if (g_opt.port_type == SERIAL_CHAN_TYPE) {
	      /* Initial # of serial bytes */
	      if (udpstatv2->compat != 0x0ffffffff) 
		int_bytes = ntohl(udp_status_pkt.serial_bytes);
	      else
		int_bytes = ntohl(udpStatus->bytes);
	    }
	  }
	}

#if defined (SOLARIS) || defined (ATT) || (HPOLD)
	sockops = setsockopt(sock, SOL_SOCKET, SO_KEEPALIVE, &lingerval,
			     sizeof(lingerval));
#ifndef SOLARIS
	if (sockops < 0)
	  fprintf(stderr, "setsockopt returned %d for keepalive\n", sockops);
#endif
#else
	linger_struct.l_onoff = 1;
	linger_struct.l_linger = LINGERVAL;
	sockops = setsockopt(sock, SOL_SOCKET, SO_LINGER, &linger_struct,
			     sizeof(linger_struct));
#ifndef SOLARIS
	if (sockops < 0)
	  fprintf(stderr, "setsockopt returned %d \n", sockops);
#endif
#endif
	optval = 1;
	sockops = setsockopt(sock, SOL_SOCKET, SO_KEEPALIVE, &optval,
			     sizeof(optval));
#ifndef SOLARIS
	if (sockops < 0)
	  fprintf(stderr, "setsockopt returned %d for keepalive\n", sockops);
#endif

	if (g_opt.start_string) {
#ifdef ESIX
	  alarm(0);
#endif
	  write(sock, g_opt.start_string, strlen(g_opt.start_string));
	  act_bytes_sent += strlen(g_opt.start_string);
	}
	if (g_opt.send_startfile)
	  sendfile(sock, g_opt.start_file, output_dest, 0, current_ptr->printer_name,0);

	/*
	 * postscript printers really like control-d's.  So, if there was an
	 * engine problem this first control d will resync.  The last one
	 * will leave it that way.
	 */
	if (g_opt.use_control_d)
	  send_control_d(sock);
	/*
	 * check to see if it should send a banner page
	 */
	if ((g_opt.real_filter || g_opt.dobanner) && g_opt.adobe.banner_first)
	  send_banner(sock);
	/*
	 * if we have a list of files just send them one at a time
	 */
	if (g_opt.file_list) {
	  while (g_opt.file_list) {
	    if (sendfile(sock, g_opt.file_list->file_name,
			 output_dest, g_opt.check_postscript, current_ptr->printer_name,0))
	      error_notify(ERR_SNDFILE, 0);
	    g_opt.file_list = g_opt.file_list->next;
	  }
	  if ((g_opt.real_filter || g_opt.dobanner) && g_opt.adobe.banner_last)
	    send_banner(sock);
	  if (g_opt.send_endfile)
	    sendfile(sock, g_opt.end_file, output_dest, 0, current_ptr->printer_name,0);
	  if (g_opt.end_string) {
#ifdef ESIX
	     alarm(0);
#endif
	     write(sock, g_opt.end_string, strlen(g_opt.end_string));
	     act_bytes_sent += strlen(g_opt.end_string);
	  }
	  if (g_opt.use_control_d)
	    send_control_d(sock);
	} else {
	  /*
	   * Reading from standard input can either be through lpr or
	   * just piping into the filter from the shell
	   */
	  
	  sendfile(sock, "STDIN", output_dest, g_opt.check_postscript, current_ptr->printer_name,have_lpspool);
	  if ((g_opt.real_filter || g_opt.dobanner) && g_opt.adobe.banner_last)
	    send_banner(sock);
	  
	  if (g_opt.send_endfile)
	    sendfile(sock, g_opt.end_file, output_dest, 0, current_ptr->printer_name,0);
	  
	  
	  if (g_opt.end_string) {
#ifdef ESIX
		 alarm(0);
#endif
	    write(sock, g_opt.end_string, strlen(g_opt.end_string));
	    act_bytes_sent += strlen(g_opt.end_string);
	  }
	  if (g_opt.use_control_d)
	    send_control_d(sock);
	}
	
	/* Now that you have finished printing, check the status again */
	
	if (g_opt.closewait) {
	  int sent_status = 0;
	  if (! got_status ) {
	    sleep(10);
	  } else 
	    do {
	      error_string[0] = 0;
	      get_printer_status(current_ptr->printer_name, error_string, g_opt.dataport);
	      if (counter > 0) {
		if ((! sent_status)  && (g_opt.port_type == PARALLEL_CHAN_TYPE) ) {
		  sent_status = 1;
		  if (got_printer_error(error_string)) {
		    error_notify(ERR_GENERIC, error_string);
		  }
		}
		check_input(sock, output_dest, 0);
		sleep(1);
	      }
	      
	      /*
	       * Check the number of bytes written so far on the
	       * serial and parallel port
	       */
	      if (g_opt.port_type == PARALLEL_CHAN_TYPE) {
                if (udpstatv2->compat != 0x0ffffffff) 
		  /* Final # of parallel bytes */
		  fin_bytes = ntohl(udp_status_pkt.parallel_bytes);
                else
		  fin_bytes = ntohl(udpStatus->bytes);
	      }
	      if (g_opt.port_type == SERIAL_CHAN_TYPE) {
                if (udpstatv2->compat != 0x0ffffffff) 
		  fin_bytes = ntohl(udp_status_pkt.serial_bytes);
                else
		  fin_bytes = ntohl(udpStatus->bytes);
	      }
	      counter++;
	    }
	  while ((fin_bytes < (int_bytes + act_bytes_sent)) && (counter < 120));
	  if (counter >= 120 ) 
	    printf("Timed out on closewait !!!! \n");
	}
#ifdef LPD
#ifndef ULTRIX
	if ((g_opt.use_control_d ) && ( g_opt.port_type == SERIAL_CHAN_TYPE ) && (g_opt.acctg ))  {
	  get_acctg_info(sock,current_ptr->printer_name,output_dest,endbuffer);
	}
#endif
	if ((g_opt.acctg) && (acctg.filename !=NULL )) {
	    do_acctg();
    }
#endif
	close(sock);
	if (g_opt.use_printer_classes) {
		char            message[MAXSTRNGLEN];
		sprintf(message, "The file was printed on %s \n", current_ptr->printer_name);
		error_notify(ERR_GENERIC, message);
	}
	if (g_errorLog)
		fclose(g_errorLog);
	close(flpdev);
	close(flpspool);
	exit(0);
}

#ifdef LPD
void
#ifdef ANSI
update_status_file(char *message, int status_num, int style)
#else
update_status_file(message, status_num, style)
	char           *message;
	int             status_num;
	int             style;
#endif
{
	int             sf;
	static int      last_id = -1;

	if (style == APPEND)
		style = O_APPEND;
	else
		style = 0;

	if ((sf = open("./status", O_RDWR | style, 0600)) >= 0) {
		if (style == CREATE)
			ftruncate(sf, 0);
		write(sf, message, strlen(message));
		close(sf);
	} else {
		if (status_num != last_id) {
			error_notify(ERR_GENERIC, message);
			last_id = status_id;
		}
	}
}

#endif


#ifdef SCO

void
check_write(sock)
	int             sock;
{

	struct timeval  timeout;
	fd_set          writefd;
	int             sel_val;
	struct pollfd   pollfds[20];
	int             i;

	for (i = 0; i < 20; i++)
		pollfds[i].events = 0;

	pollfds[sock].events = POLLOUT;
	pollfds[sock].fd = sock;

	while (1) {
		if (poll(pollfds, 20, -1) < 0) {
			perror("Poll failed");
			exit(1);
		}
		if (pollfds[sock].revents == POLLOUT)
			return;
	}
}

#endif

int
#ifdef ANSI
got_printer_error(char *error_string)
#else
got_printer_error(error_string)
	char           *error_string;
#endif
{
	char           *c_ptr;
	if (is_substring("OFFLINE", error_string))
		return (1);
	if (is_substring("FAULT", error_string))
		return (1);
	if (is_substring("PAPER", error_string))
		return (1);
	return (0);
}


int
#ifdef ANSI
got_printer_idle(char *error_string)
#else
got_printer_idle(error_string)
	char           *error_string;
#endif
{
	char           *c_ptr;
	if (is_substring("NOT  BUSY", error_string))
		return (1);
    return(0);
}

int
#ifdef ANSI
is_substring(char *sub, char *src)
#else
is_substring(sub, src)
	char           *sub, *src;
#endif
{
	while (*src) {
		if (!strncmp(src, sub, strlen(sub)))
			return (1);
		src++;
	}
	return (0);
}

/*
 * This routine performs accounting. Here we look at the '.banner' file
 * generated by the output filter in the spool directory and append the
 * contents of this file to the accounting file whose name and path are
 * passed as the 8th argument to the filter program. This SHOULD be used only
 * when you are printing through 'lpr' command and only on BSD systems.
 */

#ifdef LPD
void
do_acctg()
{
  long count ;
  FILE *fp ;
  char *date ;
  long end,start1 ;

 if ((g_opt.dataport == SERIAL) || (g_opt.dataport == SERIAL1) ||
     (g_opt.dataport == SERIAL2)) {
	if (start[0] == 0x04)
	 start1 = strip(start);
    else 
    	  sscanf(start,"%ld",&start1);
    if (endbuffer[0] == 0x04)
	 end = strip(endbuffer);
    else
       sscanf(endbuffer,"%ld",&end);
  count = end - start1 ;   /* Get number of pages */
  }
  date = getdateforme();
  if (( fp = fopen(acctg.filename,"a")) == NULL ) {
	 error_notify(ERR_GENERIC,"daemon can not open accounting file .... access rights ??? ");
	 return ;
  }
 if ((g_opt.dataport == SERIAL) || (g_opt.dataport == SERIAL1) ||
     (g_opt.dataport == SERIAL2)) {
    fprintf(fp,"%s : %s	Date : %s	Pages: %ld  \n",acctg.username,acctg.hostname,date,count);
  }
 else {
    fprintf(fp,"%s:%s:	Date : %s  \n",acctg.username,acctg.hostname,date);
  }
  fclose(fp);
}

long strip(string)
char *string ;
{
  int page ;
  char beginpage[10] ;
  int i = 0;

  strncpy(beginpage,string,sizeof(beginpage));

  /* get rid of control-d in the begining of the string */

  while (beginpage[i] == 0x04) i++;
  sscanf(beginpage,"%ld",&page);
  return(page);

}


/* Function for getting date and time the job was printed */

char *getdateforme()
{
 static long timeval ;
 long time() ;

 timeval = time(( long *) 0);
 return(ctime(&timeval));
}

/* This routine parses command line arguments */

parsecommandlineargs(argc,array)
int argc ;
char **array ;
{

 int  i = 1 ;

 while (( i < argc ) && (array[i][0] == '-'))  {

  switch (array[i][1]) {

	 case 'n' : /* get the user name */
				acctg.username = array[i+1] ;
				i++ ;
				break ;
         case 'h' : /* get the host name */
				acctg.hostname = array[i+1] ;
				i++ ;
				break ;
    }
   i++ ;
 }
 acctg.filename = array[argc-1] ;    /* name of the accounting file */
}

#ifndef ULTRIX
get_acctg_info(sock,printer_name,output_dest,buffer)
int sock ;
char *printer_name ;
int output_dest ;
char buffer[10] ;
{

   struct pollfd poll_fds[1] ;
   int ret ;

   /* Write the string */
    write_buffer(sock,ps_indicator, sizeof(ps_indicator), printer_name, output_dest);
    write_buffer(sock,pagecount_string, sizeof(pagecount_string), printer_name, output_dest);
    poll_fds[0].fd = sock ;   /* socket to wait on */
    poll_fds[0].events = POLLIN ; /* wait for the message */
    if ( ( ret = poll(poll_fds,1,30000) ) >= 0 ) {
		/* The call timed out after 30 seconds or there was an error */
		if ( ret == 0 )
		    error_notify(ERR_GENERIC,"accounting string timed out... Is the printer PS ??");
        else {
			  sleep(1);
     	     /* Read the number of data returned by the printer. This is the "startpage" */
UP1:
      	      if (( ret = read(sock, buffer,8)) < 0) 
	            error_notify(ERR_GENERIC,"poll failed to get accounting information..... ");
              else {
				 if ( (ret == 1 ) || (ret == 0) ) {
					 sleep(1);
					 goto UP1 ;
                 }
                 else
				    return ;
             }
       }
    }
   else
	 error_notify(ERR_GENERIC,"poll failed due to some error ... \n");
}

#endif

/* This routine saves the status file into .status file and writes the info
   returned by get_printer_status routine into the status file, so that when
   the users executes lpq command, this new message will be available to 
   them.
*/

backupstatus(file1, file2)
char *file1, *file2;
{
    char buffer[4096];
    int fd1, fd2;
    int count ;

    umask(0);
    fd1 = open(file1, O_WRONLY|O_CREAT, 0664);
	if ((fd1 < 0) || (flock(fd1,LOCK_EX))) {
	unlink(file1);
	flock(fd1,LOCK_UN);
	close(fd1);
	fd1 = open(file1, O_WRONLY|O_CREAT, 0664);
    }
    if ((fd1 < 0) || (flock(fd1,LOCK_EX) <0)) {
	close(fd1);
	return;
    }
    ftruncate(fd1,0);
    if ((fd2 = open(file2, O_RDONLY,0)) < 0) {
	close(fd1);
	return;
    }
    count = read(fd2,buffer,4096);
    flock(fd1,LOCK_UN);
    close(fd1);
    close(fd2);
}

#endif
