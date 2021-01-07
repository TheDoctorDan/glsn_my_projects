/*
 * Copyright Milan Technology Inc., 1991, 1992
 */

/* @(#)printer.c	2.4 05/20/94 */
/*
 * Host software modified to include printer classes on 6/19/92 Host software
 * modified to include notifications on 6/22/92 Mods to use global options
 * struct and error enums 7/7/92
 */
 /* Added comment handling. Lines begining with a # sign are treated as comments
	and are discarded.
	Also fixed the bug where 'serial' had to the first entry in .fpconfig file.
	Now it can be anywhere in the .fpconfig file.
 */
 /* Modified to support .fpconfig options for multiple parallel ports
 for MIL-3200.
 serial/parallel -- as before
 parallel1       -- port 2010, same as port 2000
 parallel2       -- port 2011
 serial1         -- port 2020, same as port 2001
 serial2         -- port 2021
 */



#include <stdio.h>
#include <string.h>
#include "std.h"
#include "dp.h"
extern s_options g_opt;
#define ESC 27


/*
* Get the dataport number from the identifying string (p1, p2, s1, s2, p, s)
*/
#ifdef ANSI
int
get_dataport( char *name )
#else
int
get_dataport( name )
     char *name;
#endif
{
  if (!strcmp( name,"p1" )) return PARALLEL1;  else 
    if (!strcmp( name,"p2" )) return PARALLEL2;  else
	if (!strcmp( name,"s1" )) return SERIAL1; else
	  if (!strcmp( name,"s2" )) return SERIAL2; else
	    if (!strcmp( name,"p" )) return PARALLEL; else
	      if (!strcmp( name,"s" )) return SERIAL; else
		return 0;
}

/*
* Parse the input string (form: "host:port"), and add the printer and port it identifies 
* to the printer list identified by prt_ptr.
*/
#ifdef ANSI
hsw_PCONFIG    *
process_class_pair(char *string,
		  hsw_PCONFIG * prt_ptr,
		  int where)
#else
hsw_PCONFIG    *
process_class_pair(string, prt_ptr, where)
	char           *string;	/* String containing printer names */
	hsw_PCONFIG    *prt_ptr;/* Pointer to printer class list */
	int             where;	/* where=APPEND or PREPEND */
#endif
{
  char *host, *port;
  int dataport;

  host = strtok( string,":" );
  port = strtok( NULL," \r\n" );
  dataport = get_dataport( port );

  if (dataport) {
    return form_printer_list( host,prt_ptr,dataport,where );
  } else {
    return (hsw_PCONFIG *)NULL;
  }
}  

/*
 * These routines allow you to specify a configuration file to name the
 * printer different than the host.  To use this create the file
 * /usr/local/milan/fastport_config with the lines:
 * 
 * name <printer name used in lpr> hostname <hostname that you wish it to go to>
 */

#ifdef ANSI
int 
hsw_Getprinterconfig(void)
#else
int 
hsw_Getprinterconfig()
#endif
{
	char           *config_file;
	char           *env_config;
	FILE           *c_file;
	char            e_mssg[MAXSTRNGLEN], data_buffer[MAX_BUFFER];
	i32             num_args;
	char            arg1[MAXSTRNGLEN];
	char            tempstring[MAXSTRNGLEN];
	char           *tempstring2;
	struct stat     stat_buff;
	char            fpconfig_file[MAXFILELEN];
	char            ch ;

	if (g_opt.current_dir)
		sprintf(fpconfig_file, "%s/.fpconfig", g_opt.current_dir);
	else
		strcpy(fpconfig_file, "./.fpconfig");

	if (!stat(fpconfig_file, &stat_buff))
		config_file = fpconfig_file;
	else if ((env_config = (char *) getenv("PRINTER_CONFIG")))
		config_file = env_config;
	else
		config_file = "/usr/local/milan/fastport_config";

	/*
	 * open it and get the parameters
	 */
	
	if (!(c_file = fopen(config_file, "r")))
	  return (-1);	/* NO ONE LOOKS AT THIS RETURN VALUE */
	
	arg1[0] = (char) 0;
	while ((num_args = fscanf(c_file, "%s", arg1)) != EOF) {
	  if (arg1[0] == '#' )  {
	    ch = (char) 0;
	    while (ch != '\n') ch = fgetc(c_file);
	    continue;
	  }
	  if (!strcasecmp(arg1, "hostname")) {
	    fscanf(c_file, "%s", tempstring);
	    /*
	     * Include this hostname also into the printer list
	     */
	    g_opt.hostname = 1 ;   /* a hostname has been specified */
	    g_opt.prt_list =
	      form_printer_list(tempstring, g_opt.prt_list, g_opt.dataport, APPEND);
	    continue;
	  }
	  if (!strcasecmp(arg1, "serial")) {
	    setPortOpt( SERIAL );
	    if (g_opt.prt_list) {
	      g_opt.prt_list->port_type = SERIAL_CHAN_TYPE;
	      g_opt.prt_list->port_index = 0;
	      g_opt.prt_list->dataport = SERIAL ;
	    }
	    continue;
	  }
	  if (!strcasecmp(arg1, "serial1")) {
	    setPortOpt( SERIAL1 );
	    if (g_opt.prt_list) {
	      g_opt.prt_list->port_type = SERIAL_CHAN_TYPE;
	      g_opt.prt_list->port_index = 0;
	      g_opt.prt_list->dataport = SERIAL1 ;
	    }
	    continue;
	  }
	  if (!strcasecmp(arg1, "serial2")) {
	    setPortOpt( SERIAL2 );
	    if (g_opt.prt_list) {
	      g_opt.prt_list->port_type = SERIAL_CHAN_TYPE;
	      g_opt.prt_list->port_index = 1;
	      g_opt.prt_list->dataport = SERIAL2 ;
	    }
	    continue;
	  }
	  if (!strcasecmp(arg1, "parallel")) {
	    setPortOpt( PARALLEL );
	    if (g_opt.prt_list) {
	      g_opt.prt_list->port_type = PARALLEL_CHAN_TYPE;
	      g_opt.prt_list->port_index = 0;
	      g_opt.prt_list->dataport = PARALLEL ;
	    }
	    continue;
	  }
	  if (!strcasecmp(arg1, "parallel1")) {
	    setPortOpt( PARALLEL1 );
	    if (g_opt.prt_list) {
	      g_opt.prt_list->port_type = PARALLEL_CHAN_TYPE;
	      g_opt.prt_list->port_index = 0;
	      g_opt.prt_list->dataport = PARALLEL1 ;
	    }
	    continue;
	  }
	  if (!strcasecmp(arg1, "parallel2")) {
	    setPortOpt( PARALLEL2 );
	    if (g_opt.prt_list) {
	      g_opt.prt_list->port_type = PARALLEL_CHAN_TYPE;
	      g_opt.prt_list->port_index = 1;
	      g_opt.prt_list->dataport = PARALLEL2 ;
	    }
	    continue;
	  }
	  if (!strcasecmp(arg1, "ctrld")) {
	    g_opt.use_control_d = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "formfeed")) {
	    g_opt.ff_flag = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "closewait")) {
	    g_opt.closewait = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "mapcrlf")) {
	    g_opt.mapflg = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "checkserial")) {
	    g_opt.checkserial = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "checkparallel")) {
	    g_opt.checkparallel = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "asciifilter")) {
	    g_opt.check_postscript = 1;
	    fscanf(c_file, "%s", g_opt.asciiname);
	    g_opt.asciifilter = g_opt.asciiname;
	    continue;
	  }
#ifdef DATAGEN
	  if (!strcasecmp(arg1, "dataport")) {
	    char temp_port[20];
	    fscanf(c_file, "%s",temp_port );
	    setPortOpt( atoi(temp_port) );
	    continue;
	  }
#endif
	  if (!strcasecmp(arg1, "checkpostscript")) {
	    g_opt.check_postscript = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "dobanner")) {
	    g_opt.dobanner = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "startfile")) {
	    g_opt.send_startfile = 1;
	    fscanf(c_file, "%s", g_opt.start_file);
	    continue;
	  }
	  if (!strcasecmp(arg1, "endfile")) {
	    g_opt.send_endfile = 1;
	    fscanf(c_file, "%s", g_opt.end_file);
	    continue;
	  }
	  if (!strcasecmp(arg1, "startstring")) {
	    fgets(tempstring, sizeof(tempstring), c_file);
	    if (strlen(tempstring))
	      g_opt.start_string = parse_string(tempstring);
	    continue;
	  }
	  if (!strcasecmp(arg1, "endstring")) {
	    fgets(tempstring, sizeof(tempstring), c_file);
	    if (strlen(tempstring))
	      g_opt.end_string = parse_string(tempstring);
	    continue;
	  }
	  /* Form a linked list of printers. */
	  if (!strcasecmp(arg1, "P_CLASS")) {
	    fgets(tempstring, sizeof(tempstring), c_file);
	    if (strlen(tempstring)) {
	      /* If the line is not null/blank then */
	      tempstring2 = parse_string(tempstring);
	      /*
	       * Form a linked list of printer names and
	       * return a pointer to the begining of the list
	       */
	      g_opt.use_printer_classes = 1;
	      g_opt.prt_list =
		form_printer_list(tempstring2, g_opt.prt_list, PARALLEL, APPEND);
	    }
	    continue;
	  }
	  /* Form a linked list of printers. */
	  if (!strcasecmp(arg1, "CLASS")) {
	    fgets(tempstring, sizeof(tempstring), c_file);
	    tempstring2 = strtok( tempstring," \t\n" );
	    while (tempstring2) {
	      g_opt.prt_list = 
		process_class_pair( tempstring2,g_opt.prt_list,APPEND );
	      g_opt.use_printer_classes = 1;
	      tempstring2 = strtok( NULL," \n" );
	    }
	    continue;
	  }
	  if (!strcasecmp(arg1,"acctg"))  {
		   g_opt.acctg = 1;
		   continue;
		 }
	  if (!strcasecmp(arg1, "S_CLASS")) {
	    fgets(tempstring, sizeof(tempstring), c_file);
	    if (strlen(tempstring)) {
	      /* If the line is not null/blank then */
	      tempstring2 = parse_string(tempstring);
	      /*
	       * Continue forming a list and put these
	       * printers together
	       */
	      g_opt.use_printer_classes = 1;
	      g_opt.prt_list =
		form_printer_list(tempstring2, g_opt.prt_list, SERIAL, APPEND);
	    }
	  }
	  if (!strcasecmp(arg1, "errorfile")) {
	    g_opt.notify_type.file = TRUE;
	    fscanf(c_file, "%s", g_opt.notify_type.filename);
	    continue;
	  }
	  if (!strcasecmp(arg1, "bannerfirst")) {
	    g_opt.adobe.banner_first = 1;
	    g_opt.dobanner = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "bannerlast")) {
	    g_opt.adobe.banner_last = 1;
	    g_opt.dobanner = 1;
	    continue;
	  }
	  if (!strcasecmp(arg1, "program")) {
	    g_opt.notify_type.program = TRUE;
	    fscanf(c_file, "%s", g_opt.notify_type.prog_name);
	    continue;
	  }
	  if (!strcasecmp(arg1, "syslog")) {
	    g_opt.notify_type.syslog = TRUE;
	    continue;
	  }
	  /* Notify by mail to the user */
	  if (!strcasecmp(arg1, "mail")) {
	    /* Notify thru mail */
	    g_opt.notify_type.mail = TRUE;
	    fscanf(c_file, "%s", g_opt.notify_type.user);
	  }
	  arg1[0] = (char) 0;
	}
	return (g_opt.prt_list ? 1 : 0);
      }

#ifdef ANSI
void 
get_printername(char *printer)
#else
void 
get_printername(printer)
	char           *printer;
#endif
{
	char            buff[MAXFILELEN];
	char           *tprinter;

#ifdef MIPS
	tprinter = (char *) getwd(buff);
#else
	tprinter = (char *) getcwd(buff, 40);
#endif

	tprinter = (char *) rindex(buff, '/');
	strcpy(printer, tprinter + 1);
}

#ifdef ANSI
char           *
parse_string(char *src_string)
#else
char           *
parse_string(src_string)
	char           *src_string;
#endif
{
	char           *temp_string;
	char           *c_ptr;
	char           *temp_ptr;

	while (*src_string == ' ')
		src_string++;

	if (*src_string == '"')
		src_string++;

	temp_ptr = &src_string[strlen(src_string) - 1];
	while (temp_ptr != src_string) {
		if (*temp_ptr == '"') {
			*temp_ptr = 0;
			break;
		}
		temp_ptr--;
	}

	temp_ptr = temp_string = (char *) malloc(strlen(src_string) + 1);
	c_ptr = src_string;
	while (*c_ptr)
		expand_char(&c_ptr, &temp_ptr);

	return (temp_string);
}

#ifdef ANSI
void 
expand_char(char **src, char **dest)
#else
void 
expand_char(src, dest)
	char          **src;
	char          **dest;
#endif
{
	char           *t_src;
	char           *t_dest;
	t_src = *src;
	t_dest = *dest;

	if ((*t_src == 'M') && (*(t_src + 1) == '-')) {
		*t_dest++ = ESC;
		(*dest)++;
		*src += 2;
		return;
	}
	if ((*t_src == 'C') && (*(t_src + 1) == '^')) {
		*t_dest++ = *(t_src + 2) - ('a' - 1);
		*src += 3 ;
		(*dest)++;
		return;
	}
	*t_dest = *t_src;
	(*src)++;
	(*dest)++;
}


/*
 * This routine forms a linked list of printers. The string containing
 * printers is specified in the '.fpconfig' file. The printer names are
 * separated by commas. We look for these separators, collect them one by
 * one, form a linked list and return the pointer to the begining of such a
 * list to the calling program.
 */

#ifdef ANSI
hsw_PCONFIG    *
form_printer_list(char *string,
		  hsw_PCONFIG * prt_ptr,
		  int dataport, int where)
#else
hsw_PCONFIG    *
form_printer_list(string, prt_ptr, dataport, where)
	char           *string;	/* String containing printer names */
	hsw_PCONFIG    *prt_ptr;/* Pointer to printer class */
	int             dataport;	/* port number (2000 etc) */
	int             where;	/* where=APPEND or PREPEND */
#endif
{
	hsw_PCONFIG    *runner, *node;
	char            name1[50];
	int             i = 0;

	int             limit = strlen(string);

	while (i < limit) {
		sscanf(string, "%s", name1);
		i += strlen(name1) + 1;	/* One for blank space */

		/* Remove the part of the string that has already been read */

		string += strlen(name1) + 1;	/* One for blank space */

		/* Create an entry for the printer */
		node = (hsw_PCONFIG *) malloc(sizeof(hsw_PCONFIG));
		strcpy(node->printer_name, name1);
		node->dataport = dataport;
		switch( dataport ) {
		case PARALLEL:
		case PARALLEL1:
		  node->port_type = PARALLEL_CHAN_TYPE;
		  node->port_index = 0;
		  break;
		case SERIAL:
		case SERIAL1:
		  node->port_type = SERIAL_CHAN_TYPE;
		  node->port_index = 0;
		  break;
		case PARALLEL2:
		  node->port_type = PARALLEL_CHAN_TYPE;
		  node->port_index = 1;
		  break;
		case SERIAL2:
		  node->port_type = SERIAL_CHAN_TYPE;
		  node->port_index = 1;
		  break;
		}
		node->next_printer = 0;
		if (!prt_ptr)
			prt_ptr = node;
		else {
			if (where == PREPEND) {
				node->next_printer = prt_ptr;
				prt_ptr = node;
			} else {
				runner = prt_ptr;
				while (runner->next_printer)
					runner = runner->next_printer;
				runner->next_printer = node;
			}
		}
	}
	return (prt_ptr);
}





