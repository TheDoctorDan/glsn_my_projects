/* Copyright Milan Technology Inc. 1991, 1992 */
/* DO NOT EDIT THIS FILE.  EDIT errors.init */
typedef enum {
  ERR_ZERO = 0		,
  ERR_READING = 1	,
  ERR_ILLFILE = 2	,
  ERR_UNIXSKT = 3	,
  ERR_SNDFILE = 4	,
  ERR_WRITE = 5		,
  ERR_MAGIC = 6		,
  ERR_PIPE = 7		,
  ERR_DOMAIN = 8	,
  ERR_HOSTNAME = 9	,
  ERR_CONFIG = 10	,
  ERR_MAIL = 11		,
  ERR_GENERIC = 12	,
  ERR_FORK = 13		,
  ERR_ASCII = 14	,
  ERR_NOPRINTER = 15	,
  ERR_NORESPONSE = 16	,
  ERR_BOGUS };
#ifdef G_ERRORS
char *g_errors[] = {
	"Unknown (bogus) error message 0.",
	"Error in reading file.",
	"File not found or access denied.",
	"Connecting of unix socket.",
	"Error in sending file.",
	"Error on write.",
	"Error on read of magic number.",
	"Cannot create a pipe for external program.",
	"Unix domain socket error.",
	"Hostname lookup error.",
	"Configuration file does not exist.",
	"Error in sending mail.",
	"Unknown (bogus) error message 12.",
	"Error on fork/exec.",
	"Non-postscript file as input.  Printer may not print.",
	"No printer specified.  Job not printed.",
	"No response from any printer. Job not printed.",
	"Bogus error message."};
#endif
