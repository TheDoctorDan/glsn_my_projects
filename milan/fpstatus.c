/* Copyright Milan Technology, 1991 1992 */
/*  2.2 10/9/1994, Modified to support 4 port MIL-3200 */
/* @(#)fpstatus.c	3.0 11/1/94 */

#include "udp.h"
#include <stdio.h>

#ifndef SCO
#include <sys/file.h>
#endif

#include <sys/types.h>

#include "dp.h"

#ifdef ANSII
main(int argc,char **argv)
#else
main(argc,argv)
int argc;
char **argv;
#endif
{
  char status_string[MAXSTRNGLEN];
  if (argc != 2) {
    fprintf(stderr,"Usage: fpstatus <fastport name>\n");
    exit(1);
  }
  status_string[0] = 0;
  get_printer_status(argv[1],status_string,ALL);
  fprintf(stderr,status_string);
}
