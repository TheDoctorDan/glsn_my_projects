/*
 * Copyright Milan Technology 1991,92,93,94
*/

/* @(#)udp.h	2.1 10/08/94 */

typedef struct {
        unsigned long parallel_addr;
        unsigned long serial_addr;
        unsigned long parallel_bytes;
        unsigned long serial_bytes;
        unsigned long  p_status;
        char message[250];
} udp_status_packet;


/* version 2 structures.  Request the Fastport to reply with this structure
* by sending it a status query with byte [0] == MILAN_OP_V2
*/

/* status reply header.
* The full status reply is as follows:
*
*	+-------------------+
*	| numParChan		|
*	+-------------------+
*	| numSerChan		|
*	+-------------------+
*	| struct parStat	|
*	|	[numParChan]	|
*	+-------------------+
*	| struct serStat	|
*	|	[numSerChan]	|
*	+-------------------+
*/
typedef struct {
    unsigned long compat;       /* Field used to distinguish
between 3200 and 3100 responses */
	unsigned char majorVersion;
	unsigned char minorVersion;
	unsigned char numParChan;
	unsigned char numSerChan;
} udpStatusV2;

/* parallel element of status struct.  Repeated in msg numParChan times */
typedef struct {
		unsigned long addr;
		unsigned long bytes;
		unsigned long status;
		char message[120];
} udpParStatusV2;

	
/* serial element of status struct.  Repeated in msg numSerChan times */
typedef struct {
	unsigned long addr;	
	unsigned long bytes;
	unsigned long status;
	char message[120];
} udpSerStatusV2;


#define MILAN_OP		0x40
#define MILAN_OP_V2		0x42	/* version 2 operation */

#define GET_STATUS	 	0x01
#define NETWARE_STATUS	0x41
#define PROTOCOL_STATUS	0x42
#define M3200           0x04  /* Model 3200 FastPort */
#define M3100           0x01  /* Model 3100/3000 FastPort */
#define ALL             0x05 
