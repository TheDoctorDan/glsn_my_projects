/* $Header: /hphome/gopal/fpfilter.wor/RCS/dp.h,v 1.2 1994/11/29 23:42:51 root Exp $ */
#ifndef DEEPEE_H
#define DEEPEE_H
/*
 * Copyright Milan Technology Inc., 1991, 1992
 */

/* @(#)dp.h	2.4 5/20/94 */
/* Modified to support MIL-3200 parameters */

#include <win.h>

#ifdef mips
#include <bsd/sys/types.h>
#include <bsd/sys/wait.h>
#else
#include <sys/types.h>
#endif
#include <sys/stat.h>
#include <stdio.h>
#include <unistd.h>

#include <signal.h>

#include <fcntl.h>
#ifndef mips
#include <sgtty.h>
#endif
#include <sys/file.h>
#include <sys/socket.h>

#ifdef LPD
#ifdef mips
#include <bsd/sys/un.h>
#else
#include <sys/un.h>
#endif
#endif

#ifdef ATT
#include <sys/in.h>

#else
#ifdef mips
#include <bsd/netinet/in.h>
#else
#include <netinet/in.h>
#endif
#endif

#include <netdb.h>

#ifdef INTERACTIVE
#include <net/errno.h>
#endif
#include <errno.h>

extern int errno;


#ifdef mips
#include <bsd/sys/time.h>
#include <bsd/syslog.h>
#else
#include <sys/time.h>
#include <syslog.h>
#endif


#ifdef SCO
#include <poll.h>
#endif

#ifdef AIX
#include <sys/select.h>
#endif

#if defined (SEQUENT) || defined (SOLARIS) 
#define bcopy(a, b, c) memcpy(b, a, c)
#define rindex(a, b)   strrchr(a, b)
#define bzero(a, c) memset(a, (char)0, c)
#endif

#ifdef HPOLD
#define strcasecmp strcmp
#endif

#ifdef INTERACTIVE
#define rindex(a, b)   strrchr(a, b)
#endif

#define SERIAL    2001
#define PARALLEL  2000

/* The following are additional ports defined for MIL-3200 */
#define SERIAL1   2020
#define SERIAL2   2021
#define PARALLEL1 2010
#define PARALLEL2 2011

#define PARALLEL_CHAN_TYPE  0
#define SERIAL_CHAN_TYPE    1


#ifndef STDIN_FILENO
#define STDIN_FILENO 0
#endif

#ifndef STDOUT_FILENO
#define STDOUT_FILENO 1
#endif

#define MAX_MAGIC_HEADER 100 
#define LINGERVAL 1000
#define ASCII 0
#define POSTSCRIPT 1
#define CAT 2
#define MAX_BUFFER 2000
#define MAXFILELEN 1024
#define MAXSTRNGLEN 800
#define MAXNAMELEN  256
#define MAXLINELEN  256
#define APPEND 0
#define PREPEND 1

#define TRUE  1
#define FALSE 0

#define readEnd 0
#define writeEnd 1

/* hsw == Host SoftWare */
typedef struct hsw_CONFIG {
   char printer_name[MAXNAMELEN];
   char hostname[MAXNAMELEN];
   int port_type;
   int port_index;
   int dataport; /* used for printer classes */
   struct hsw_CONFIG  *next_printer;
} hsw_PCONFIG;

typedef struct file_obj {
   char file_name[MAXNAMELEN];
   struct file_obj *next;
} file_obj_t, *file_obj_ptr;


/* Structure used for notification purposes */

typedef struct notification {
   int mail;                  /* Should I notify thru mail ? */
   int file;                  /* Should I notify thru file ? */
   int syslog;                /* Should I notify thru syslog file ? */
   int program;               /* notify thru execing a prog */
   char user[MAXNAMELEN];     /* User name to send mail to   */
   char filename[MAXNAMELEN]; /* Name of such a file */
   char prog_name[MAXNAMELEN];/* name of prog to exec */
} s_notification;

typedef struct Adobe {
   char *prog;
   char *pname;
   char *name;
   char *host;
   char *accountingfile;
   int banner_first, banner_last, verbose_log;
} s_Adobe;

/* Struct to hold options and other "global" values */
typedef struct options {
   int use_control_d;
   int hostname ;
   int mapflg;
   int ff_flag;
   int dobanner;
   int binary_type;
   int check_postscript;
   int dataport;         /* TCP/IP port number of the target */
   int port_type;        /* port type (parallel or serial) of the target */
   int port_index;       /* port index (0..NPPORT or NSPORT) of the target */
   char port_id_string[20];
   int send_startfile;
   int send_endfile;
   int real_filter;
   int closewait  ;
   int use_printer_classes;
   char *current_dir;
   char *start_string;
   char *end_string;
   char *asciifilter;
   char asciiname[MAXNAMELEN];
   char start_file[MAXFILELEN];
   char end_file[MAXFILELEN];
   char *printer_str;
   int checkserial ;
   int checkparallel ;
   file_obj_ptr file_list;

   s_Adobe adobe;
   s_notification notify_type;
   hsw_PCONFIG *prt_list;    /* List of serial and parallel prts*/
   int acctg;
   int paracctg ;
} s_options;

/* Function prototypes */

#if defined(__cplusplus)
#define EXTERNAL_FUNCTION( funName, params ) extern "C" { funName params; }
#else
#if defined(ANSI) || defined(__STDC__)
#define EXTERNAL_FUNCTION( funName, params ) funName params
#else
#define EXTERNAL_FUNCTION( funName, params ) funName()
#endif
#endif


EXTERNAL_FUNCTION(void systemNoWait,
		  (char* cmd));
EXTERNAL_FUNCTION(void sig_child,
		  ());
EXTERNAL_FUNCTION(int parseCommandLineArgs,
		  (int argc, char **argv));
EXTERNAL_FUNCTION(void expand_char,
		  ());
EXTERNAL_FUNCTION(void get_printername, 
		  (char *printer));
EXTERNAL_FUNCTION(void error_notify, 
		  (int err_no, char* message));
EXTERNAL_FUNCTION(void error_protect, 
		  (int err_no));
EXTERNAL_FUNCTION(hsw_PCONFIG* form_printer_list, 
		  (char* string, hsw_PCONFIG *ptr_ptr, int ptype, int where));
EXTERNAL_FUNCTION(hsw_PCONFIG* process_class_pair, 
		  (char* string, hsw_PCONFIG *ptr_ptr, int where));
EXTERNAL_FUNCTION(char* parse_string, 
		  (char* str));
EXTERNAL_FUNCTION(void add_file, 
		  (file_obj_ptr *flist, char *file_to_add));
EXTERNAL_FUNCTION(int sendfile, 
		  (int sock, char *file_to_send, int output_dest, 
		   int check_postscript, char *printer));
EXTERNAL_FUNCTION(void check_input, 
		  (int sock, int output_dest, int timeout));
EXTERNAL_FUNCTION(void send_control_d,
		  (int sock));
EXTERNAL_FUNCTION(void check_alarm, 
		  ());
EXTERNAL_FUNCTION(char *expand_buffer,
		  (char *, int, int *));
EXTERNAL_FUNCTION(void send_banner,
		  ());
EXTERNAL_FUNCTION(char *expand_binary,(char *,int,int));

EXTERNAL_FUNCTION(void update_status_file,(char *message,int status_num,int style));
EXTERNAL_FUNCTION(void write_buffer,(int sock, char *buffer, int length,char *printer_name,int output_dest));

EXTERNAL_FUNCTION(void check_write,(int sock));
EXTERNAL_FUNCTION(void do_acctg,());

#endif /* DEEPEE_H */

#define APPEND 0
#define CREATE 1

extern s_options g_opt;


#define ADOBE_BINARY 1
#define TEK_BINARY 1
#define HP_BINARY 2


#include "lpdev.h"
#include "lpspool.h"

int	flpdev;
char	lpdev[LPDEV_KEY_LEN+1];
t_lpdev_rec lpdev_rec;

int	flpspool;
char	lpspool[LPSPOOL_KEY_LEN+1];
t_lpspool_rec lpspool_rec;

int	lp_item;
int	lp_line;
int	have_lpspool;

char	sys_time[9];
