/* Copyright Milan Technology, 1991 1992 */

/* @(#)udpstat.c	2.6 09/15/94 */
/* Fixed inet_ntoa() typecast problem which was causing
   core dumps at a customer location, 6/29/94
   Added support for using either hostname or IP address 9/15/94
   Added support for multiport print server, 3200 10/08/94
*/

#include "udp.h"
#include <stdio.h>

#include <sys/types.h>

/* #include <sys/socket.h> */
#include <sys/errno.h>
#ifndef HPOLD
#ifdef mips
#include <bsd/netinet/in.h>
#else
#include <netinet/in.h>
#endif
#endif
#include <signal.h>
#include "dp.h"

void            print_status_infov2();
void            print_status_infov1();
int             get_udpstatus();
void            print_active_connection();
void            get_port_name();
void            get_portstatus();

/* UDP Status packet */

udp_status_packet udp_status_pkt;
int               numparchan,numserchan;
udpParStatusV2    *udpStatus;
udpStatusV2       *udpstatv2;
unsigned char     msgbuf[800];

#ifdef ANSI
int
get_printer_status(char *printer, char *error_string, int port)
#else
int
get_printer_status(printer, error_string, port)
	char           *printer;
	char           *error_string;
	int             port;
#endif
{
	int             fastport;
	struct sockaddr_in fp_sock, my_sock;
	unsigned long   temp,cmp;
	struct hostent *hp;
	int status;

	bzero((char *) &fp_sock, sizeof(fp_sock));
	fp_sock.sin_family = AF_INET;

	if (check_type(printer)) {
	  if (!(hp = gethostbyname(printer))) {
		 return(0);
	  }
	  temp = *(unsigned long *) hp->h_addr;
	  fp_sock.sin_addr.s_addr = temp;
	}
	else {
          cmp = inet_addr(printer);
          bcopy((char *)&cmp, (char *) &fp_sock.sin_addr, 4);
	}
	fp_sock.sin_port = htons(35);
	if ((fastport = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
	  perror("error on socket\n");
	  return(0);
	}
    /*
     * need to be able to receive status so bind the socket
    */
    bzero((char *) &my_sock, sizeof(my_sock));
    my_sock.sin_family = AF_INET;
    my_sock.sin_addr.s_addr = htonl(INADDR_ANY);
    my_sock.sin_port = htons(0);
    if (bind(fastport, (struct sockaddr *) & my_sock, sizeof(my_sock)) < 0) {
		perror("can't bind:");
		return(0);
	}
    status = get_udpstatus(fastport, (struct sockaddr *) & fp_sock, 
			 sizeof(fp_sock),error_string,port,printer);
    close(fastport);
    return(status);

}

#ifdef ANSI
int
get_udpstatus(int fastport, struct sockaddr *server, int server_length,
              char *error_string, int port, char *fastport_name)
#else
int
get_udpstatus(fastport, server, server_length, error_string, port,
			  fastport_name)
    int             fastport;
    struct sockaddr *server;
    int             server_length;
    char           *error_string;
    int             port;
    char           *fastport_name;
#endif
{
    char            command[10];
    int             nbytes, sel_val;
    fd_set          readfds;
    struct timeval  timeout;
    struct sockaddr_in rec_address;
    int             reclen,ret;
    timeout.tv_sec = 5;
    timeout.tv_usec = 0;

    /* set up request message.  Request version 2; fp may with either v1 or v2 */
    command[0] = MILAN_OP_V2;
    command[1] = GET_STATUS; /* action required */
	
    /* send request to fastport */
    if ((nbytes = sendto(fastport, command, sizeof(command), 0, server,
			 server_length)) != sizeof(command)) {
        perror("Cannot send to server:");
        exit(1);
    }
    
    /* wait for response from fastport */
    while (1) {
      FD_ZERO(&readfds);
      FD_SET(fastport, &readfds);
      if ((sel_val = select(fastport + 1, &readfds, 0, 0, &timeout)) <= 0) {
	/* Timeout - no response */
	sprintf(error_string, "no response from server %s\n",fastport_name);
	return(0);
      }
      /* We did get a response */
      if (FD_ISSET(fastport, &readfds))
	break;
    }

    /* parse response */
    reclen = sizeof(rec_address);
    if ((nbytes = recvfrom(fastport,msgbuf,sizeof(msgbuf),0,&rec_address,
                  &reclen)) < 0) {
       sprintf(error_string, "udp recvfrom returned error");
       return(0);
    }
    udpstatv2 = (udpStatusV2 *)msgbuf;
    numparchan = udpstatv2->numParChan;
    numserchan = udpstatv2->numSerChan;
    /* version 2 fastports return ff... in the first four bytes to distinguish them
       from version 1 */
    if (udpstatv2->compat != 0x0ffffffff) {
      get_v1status(fastport,server,server_length,
                    error_string,port,fastport_name);
    } else {
      get_v2status(error_string,port,fastport_name,msgbuf); 
    }
    return(1);
}


/*
* Append status information about port porttype/index to the string result_string
*/ 
#ifdef  ANSII
void
print_status_infov2(udpParStatusV2 *stat_buff, char *result_string,int porttype,int index)
#else
void
print_status_infov2(stat_buff, result_string, porttype,index)
udpParStatusV2 *stat_buff;
char           *result_string;
int             porttype;
int             index;
#endif
{
	char   temp_string[200];
	char port_id_string[20];
	struct hostent *hp;
	unsigned long   host_addr,value;
	char *type_string[2];
	type_string[PARALLEL_CHAN_TYPE] = "parallel";
	type_string[SERIAL_CHAN_TYPE] = "serial";

	/* port_id_string contains port identification, e.g. "parallel port 1" */
	sprintf( port_id_string, "%s port %d",type_string[porttype],index+1 );

	/* append the "active job from host name ..." to the result_string */
	if (stat_buff->addr) {
#ifndef SCO
	    host_addr = ntohl(stat_buff->addr);
#else
	    host_addr = stat_buff->addr;
#endif
	    print_active_connection(&host_addr,port_id_string,result_string);
	  }

	/* append the message received from the fastport to the result_string */
	sprintf(temp_string, "%s\n", stat_buff->message);
        strcat(result_string, temp_string);

	/* append the total bytes sent string to the result_string */
        value = ntohl(stat_buff->bytes); /* @@@ */
	sprintf(temp_string,"Total bytes sent on %s : %d\n\n",
		port_id_string, ntohl(stat_buff->bytes));
        strcat(result_string, temp_string);
}

#ifdef ANSI
void
print_active_connection(unsigned long *host_address, char *kind, char *error_string)
#else
void
print_active_connection(host_address, kind, error_string)
	unsigned long  *host_address;
	char           *kind;
	char           *error_string;
#endif
{
	struct hostent *hp;
	char            temp_string[100];
	if (hp = gethostbyaddr(host_address, 4, AF_INET)) {
	   unsigned long newaddr;
#if defined (SUN) || (SOLARIS)
	   newaddr = hp->h_addr;
#else
           newaddr = *(unsigned long *)hp->h_addr; 
#endif
	   sprintf(temp_string, "\nTCP/IP socket job active on %s from %s (%s)\n", kind, hp->h_name, inet_ntoa((struct in_addr *) (newaddr)));
	} else {
			sprintf(error_string, "\nActive %s data session from %s\n", kind,
				inet_ntoa((struct in_addr *) (*host_address)));
			return;
	}
	strcat(error_string, temp_string);
}


/* Check whether it is the name or the IP address */
/* We also have to make sure that it is a set of numbers before
   returning  type as "IP address" since we may run into a situation
   like navier.mae.cornell.edu, which has four dots !!
   (gopal 1/12/95)
*/

int 
check_type(name)
char *name;
{
  unsigned int i;
  int count = 0 ;

  if (name[0] > 0x39)             /* 0x39 is ASCII 9 */
	  return(1);
  for (i= 0; i <strlen(name); i++) {
      if (name[i] == '.') count++;
  }
  if (count < 3 ) 
     return(1);    /* It is not an IP address */
  else
     return(0);    /* Yes, it is the IP address */
}


/* Get status for MIL-3200 */
int
#ifdef ANSI
get_v2status(char *error_string,int port,
               char *fastport_name,unsigned char *status)
#else
get_v2status(error_string,port,fastport_name,status)
char   *error_string;
int    port;
char   *fastport_name;
unsigned char *status;
#endif
{
 int i;

 udpStatus = (udpParStatusV2 *)(status + sizeof(udpStatusV2));

 if (port==ALL) {
  for (i=0; i<numparchan; i++) {
      print_status_infov2(udpStatus,error_string,PARALLEL_CHAN_TYPE,i);
      udpStatus++;        /* Goto next instance of structure */
  }
  for (i=0; i<numserchan; i++) {
      print_status_infov2(udpStatus,error_string,SERIAL_CHAN_TYPE,i);
      udpStatus++;        /* Goto next instance of structure */
      
    }
 }
 else {
    if (port == PARALLEL || port == PARALLEL1) {
       print_status_infov2(udpStatus,error_string,PARALLEL_CHAN_TYPE,0);
    }
    if (port == PARALLEL2) {
       udpStatus++;
       print_status_infov2(udpStatus,error_string,PARALLEL_CHAN_TYPE,1);
    }
    if (port == SERIAL || port == SERIAL1) {
       udpStatus += 2;  /* Move two instances away */
       print_status_infov2(udpStatus,error_string,SERIAL_CHAN_TYPE,0);
    }
    if (port == SERIAL2) {
       udpStatus += 3;
       print_status_infov2(udpStatus,error_string,SERIAL_CHAN_TYPE,1);
    }
  }
}

void
#ifdef ANSI
get_port_name(int port_num,char *error_string,char *fastport_name)
#else
get_port_name(port_num,error_string,fastport_name)
int port_num;
char *error_string;
char *fastport_name;
#endif
{

 if (port_num == PARALLEL || port_num == PARALLEL1)
    sprintf(error_string,
    "no response from server %s for first parallel port\n");
 if (port_num == PARALLEL2)
     sprintf(error_string,
    "no response from server %s for second parallel port\n");
 if (port_num == SERIAL || port_num == SERIAL1)
    sprintf(error_string,
    "no response from server %s for first serial port\n");
 if (port_num == SERIAL2)
    sprintf(error_string,
    "no response from server %s for second serial port\n");
}

/* Get status from old (version 1) FastPorts */
int 
#ifdef ANSI
get_v1status(int fastport,struct sockaddr *server,int server_length,
char *error_string,int port,char *fastport_name)
#else
get_v1status(fastport,server,server_length,error_string,
port,fastport_name)

int    fastport;
struct sockaddr *server;
int    server_length;
char   *error_string;
int    port;
char   *fastport_name;
#endif
{
	char            command[10];

	int             nbytes, sel_val;
	fd_set          readfds;
	struct timeval  timeout;
	struct sockaddr_in rec_address;
	int             reclen;
	timeout.tv_sec = 5;
	timeout.tv_usec = 0;
	
	command[0] = MILAN_OP; /* a milan query */
	command[1] = GET_STATUS; /* action required */
	
	if ((nbytes = sendto(fastport, command, sizeof(command), 0, server, server_length)) !=
	    sizeof(command)) {
		perror("Cannot send to server:");
		exit(1);
	}
    while (1) {
        FD_ZERO(&readfds);
        FD_SET(fastport, &readfds);
		if ((sel_val = select(fastport + 1, &readfds, 0, 0, &timeout)) < 0) {
           sprintf(error_string, "no response from server \n");
           return(0);
		}
		if (!sel_val) {
           sprintf(error_string, "no response from server %s\n",fastport_name);
           return(0);;
		}
        if (FD_ISSET(fastport, &readfds))
           break;
	}
    reclen = sizeof(rec_address);
    if ((nbytes = recvfrom(fastport, &udp_status_pkt, sizeof(udp_status_pkt), 0, &rec_address, &reclen)) < 0) {
       sprintf(error_string, "udp recvfrom returned error");
       return(0);
    }
    print_status_infov1(&udp_status_pkt, error_string, port);
    return(1);
}

/* Prints status information for Version 1 (older) FastPorts */

#ifdef  ANSII
void print_status_infov1(udp_status_packet *stat_buff, char *error_string, int port)
#else
void
print_status_infov1(stat_buff, error_string, port)
    udp_status_packet *stat_buff;
    char           *error_string;
    int             port;
#endif
{
    char            temp_string[100];
    struct hostent *hp;
    unsigned long   host_addr;
    if (port == PARALLEL || port == ALL) {
       if (stat_buff->parallel_addr) {
#ifndef SCO
          host_addr = ntohl(stat_buff->parallel_addr);
#else
          host_addr = stat_buff->parallel_addr;
#endif
          print_active_connection(&host_addr, "parallel ", error_string);
          sprintf(temp_string, "%s\n", stat_buff->message);
          strcat(error_string, temp_string);
          sprintf(temp_string, "Total parallel bytes sent: %d\n\n", ntohl(stat_buff->parallel_bytes));
          strcat(error_string, temp_string);
       } else {
          strcat(error_string, "No active parallel job\n");
          sprintf(temp_string, "%s\n", stat_buff->message);
          strcat(error_string, temp_string);
          sprintf(temp_string, "Total parallel bytes sent: %d\n\n", ntohl(stat_buff->parallel_bytes));
          strcat(error_string, temp_string);
       }
    }
    if (port == SERIAL || port == ALL) {
       if (stat_buff->serial_addr) {
#ifndef SCO
          host_addr = ntohl(stat_buff->serial_addr);
#else
          host_addr = stat_buff->serial_addr;
#endif
          print_active_connection(&host_addr, "serial ", error_string);
          sprintf(temp_string, "Total serial bytes sent: %d\n\n", ntohl(stat_buff->serial_bytes));
          strcat(error_string, temp_string);
		} else {
          strcat(error_string, "No active serial job\n");
          sprintf(temp_string, "Total serial bytes sent: %d\n\n", ntohl(stat_buff->serial_bytes));
          strcat(error_string, temp_string);
        }
    }
}


