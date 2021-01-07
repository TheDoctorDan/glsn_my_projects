/*************************************************************************
*    INSTRUCTIONS FOR LINKING THESE FUNCTIONS INTO THE                   *
*                 RUN TIME LIBRARY ON THE TOWER.                         *
*                                                                        *
*    1) DOWNLOAD FILE AND MOVE IT TO /usr/lib/cobol/config/cprogram.c.   *
*    2) ADD ANY NEW FUNCTIONS OR CHANGES IN PARAMETERS TO THE 'CASE'     *
*       STATEMENT IN THE usercall.c FILE IN THE SAME config DIRECTORY.   *
*    3) COMPILE THE FUNCTIONS BY TYPING:                                 *
*                                                                        *
*            cc -n -o ../rts32 cprogram.c usercall.c rts32.o             *
*                                                                        *
*             WHILE IN THE /usr/lib/cobol/config  DIRECTORY              *
*                                                                        *
*  *** NOTE - THE RTS32 LIBRARY CANNOT BE IN USE WHILE COMPILING  ***    *
*             (NO COBOL APPLICATION CAN BE RUNNING AT THIS TIME)         *
*                                                                        *
*          USE:  'ps -e'  TO SEE IF rts32 IS IN USE.                     *
*                                                                        *
*          OTHERWISE TO COMPILE WHILE RTS32 IS IN USE, THE FOLLOWING     *
*          COMMANDS 'SEEM' TO WORK:                                      *
*                     cd ..                                              *
*                     mv rts32 rts32.old                                 *
*                     cd config                                          *
*                     DO STEP 3                                          *
*                                                                        *
*                                                                        *
*************************************************************************/

#include <stdio.h>
#include <fcntl.h>
#include <termio.h>
#include <string.h>
#include <malloc.h>
#include <errno.h>

#define CMAX 0777


extern int errno;
extern char *sys_errlist[];
extern int sys_nerr;


/******************************************************************
*               PORTOPEN()  -  CALL "1"
*       opens a port for reading and writing at a given baud.
*         line delimited with NL char. (ICANON) 
*
*  INPUT:
*    path = a COBOL PIC X(nn) terminated with nulls not spaces.
*    baud = a COBOL PIC 9(04) COMP field.
*  OUTPUT:
*          a COBOL PIC S9(04) COMP field with the file number.
******************************************************************/

int portopen(path, baud)

unsigned baud;
char *path;

{
   struct termio arg;
   int baud_rate, filedes, err;

   switch ( baud )
   {
                   case 300:  baud_rate = B300;
                              break;
                   case 600:  baud_rate = B600;
                              break;
                  case 1200:  baud_rate = B1200;
                              break;
                  case 1800:  baud_rate = B1800;
                              break;
                  case 2400:  baud_rate = B2400;
                              break;
                  case 4800:  baud_rate = B4800;
                              break;
                  case 9600:  baud_rate = B9600;
                              break;
                 case 19200:  baud_rate = B19200;
                              break;
                    default:  return(-3);  /* return -3 = illegal baud rate */
                              break;
   }

   filedes = open(path, O_RDWR);
   if (filedes < 0)
      return(-1);

   arg.c_iflag = BRKINT | IGNPAR | ISTRIP | ICRNL;
   arg.c_oflag = OPOST | OCRNL;
   arg.c_cflag = baud_rate | CS7 | CLOCAL | CREAD | PARENB;
   arg.c_lflag = ICANON | ISIG;

   arg.c_cc[VINTR] = CMAX;
   arg.c_cc[VQUIT] = CMAX;
   arg.c_cc[VERASE] = CMAX;
   arg.c_cc[VKILL] = CMAX;
   arg.c_cc[VEOF] = CEOF;
   arg.c_cc[VEOL] = 10;
   arg.c_cc[VSWTCH] = CSWTCH;

   err = ioctl(filedes, TCSETA, &arg);
   if (err >= 0)
      err = ioctl(filedes, TCFLSH, 2);

   if (err < 0)
      return(-2);
   else
      return(filedes);

}


/******************************************************************
*               PORTOPEN2()  - CALL "5"
*       opens a port for reading and writing at a given baud
*     for use with download programs - no canonical processing.
*                  *****************
*  INPUT:
*    path = a COBOL PIC X(nn) terminated with nulls not spaces.
*    baud = a COBOL PIC 9(04) COMP field.
*  OUTPUT:
*          a COBOL PIC S9(04) COMP field with the file number.
******************************************************************/

int portopen2(path, baud)

unsigned baud;
char *path;

{
   struct termio arg;
   int baud_rate, filedes, err;

   switch ( baud )
   {
                   case 300:  baud_rate = B300;
                              break;
                   case 600:  baud_rate = B600;
                              break;
                  case 1200:  baud_rate = B1200;
                              break;
                  case 1800:  baud_rate = B1800;
                              break;
                  case 2400:  baud_rate = B2400;
                              break;
                  case 4800:  baud_rate = B4800;
                              break;
                  case 9600:  baud_rate = B9600;
                              break;
                 case 19200:  baud_rate = B19200;
                              break;
                    default:  return(-3);  /* return -3 = illegal baud rate */
                              break;
   }

   filedes = open(path, O_RDWR);
   if (filedes < 0)
      return(-1);
   if(ioctl(filedes,TCGETA, &arg)){
                fprintf(stderr,"dgsend: Error on call to ioctl\n");
                perror("dgsend");
                exit(1);
   }

   arg.c_oflag = CRDLY | CR0;
   arg.c_cflag = baud_rate | CS7 | CLOCAL | CREAD | PARENB;
   arg.c_lflag = 0;

   arg.c_cc[VINTR] = CDEL;
   arg.c_cc[VQUIT] = CDEL;
   arg.c_cc[VERASE] = CDEL;
   arg.c_cc[VKILL] = CDEL;
   arg.c_cc[VMIN] = 1;
   arg.c_cc[VTIME] = CNUL;
   arg.c_cc[VSWTCH] = CDEL;

   err = ioctl(filedes, TCSETA, &arg);
   if (err >= 0)
      err = ioctl(filedes, TCFLSH, 2);


   if (err < 0)
       {
         perror("ERROR FROM IOCTL");
         printf("error no %d\n", errno );
      return(-2);
       }
   else
      return(filedes);

}



/******************************************************************
*               PORTOPEN3()  - CALL "6"
*       opens a port for reading and writing at a given baud
*     for use with download programs - no canonical processing.
*                  *****************
*  INPUT:
*    path = a COBOL PIC X(nn) terminated with nulls not spaces.
*    baud = a COBOL PIC 9(04) COMP field.
*  OUTPUT:
*          a COBOL PIC S9(04) COMP field with the file number.
******************************************************************/

int portopen3(path, baud)

unsigned baud;
char *path;

{
   struct termio arg;
   int baud_rate, filedes, err;

   switch ( baud )
   {
                   case 300:  baud_rate = B300;
                              break;
                   case 600:  baud_rate = B600;
                              break;
                  case 1200:  baud_rate = B1200;
                              break;
                  case 1800:  baud_rate = B1800;
                              break;
                  case 2400:  baud_rate = B2400;
                              break;
                  case 4800:  baud_rate = B4800;
                              break;
                  case 9600:  baud_rate = B9600;
                              break;
                 case 19200:  baud_rate = B19200;
                              break;
                    default:  return(-3);  /* return -3 = illegal baud rate */
                              break;
   }

   filedes = open(path, O_RDWR);
   if (filedes < 0)
      return(-1);

   arg.c_iflag = IGNPAR | IGNBRK;
   arg.c_oflag = CRDLY | CR0;
   arg.c_cflag = baud_rate | CS7 | CLOCAL | CREAD | PARENB | HUPCL;
   arg.c_lflag = 0;

   arg.c_cc[VINTR] = CMAX;
   arg.c_cc[VQUIT] = CMAX;
   arg.c_cc[VERASE] = CMAX;
   arg.c_cc[VKILL] = CMAX;
   arg.c_cc[ 4  ] = 255;    /* 6 FOR OLD HDPR990 - 255 FOR BLOCKING */
   arg.c_cc[ 5  ] = 20;     /* 1 FOR OLD HDPR990 - 20 FOR BLOCKING */
   arg.c_cc[VSWTCH] = CSWTCH;

   err = ioctl(filedes, TCSETA, &arg);
   if (err >= 0)
      err = ioctl(filedes, TCFLSH, 2);

   if (err < 0)
      return(-2);
   else
      return(filedes);

}




/*******************************************************************
*                 PORTWRITE() - CALL "2"
*      writes to a port using a character buffer.
*
* INPUT:
*  filedes = a COBOL PIC S9(04) field holding the file number
*            returned from portopen().
*  s = a COBOL PIC X(nn) holding the characters to write to a
*            port.
*  size = a COBOL PIC 9(04) field holding the number of
*            characters to write in the buffer not exceeding nn.
* OUTPUT:
*         a COBOL PIC S9(04) field with the number of characters
*            written (or -1 for an error).
********************************************************************/

portwrite(filedes, s, size)

unsigned filedes;
unsigned size;
char *s;

{
   int err;

   err = write((int)filedes, s, size);
   return(err);
}



/**************************************************************
*                  PORTREAD() - CALL "3"
*      reads from a port using a character buffer.
*
* INPUT:
*  filedes = a COBOL PIC S9(04) field holding the file number
*            returned from portopen().
*  s = a COBOL PIC X(nn) the buffer to read characters into.
*  size = a COBOL PIC 9(04) field holding the size of the buffer.
* OUTPUT:
*         a COBOL PIC S9(04) field with the number of characters
*            read (or -1 for an error).
**************************************************************/

portread(filedes, s, size)

unsigned filedes, size;
char *s;

{
   int err;
   int i, j;
   char *b;

   b = malloc(size);
   if (b == NULL)
      printf("portread - memory size error\n");

   for (err = 0; err < size; err++) { /* clear the buffer before reading */
      s[err] = ' ';
      b[err] = ' ';
   }

   err = read((int)filedes, b, size);

   /* correct errors with leading linefeeds */
   j = 0;
   for (i = 0; i < err; i++) {
      if ((b[i] != 13) && (b[i] != 10) && (b[i] != '\n')) {
         s[j] = b[i];         /* remove the carraige return from input */
         j++;
      }
   }
   free(b);
   return(j);

}


/**************************************************************
*                 PORTCLOSE() - CALL "4"
*      closes a port using a character buffer.
*
* INPUT:
*  filedes = a COBOL PIC S9(04) field holding the file number
*            returned from portopen().
**************************************************************/

portclose(filedes)

unsigned filedes;

{
   close((int)filedes);
}



/******************************************************************
*               HDPR990OPEN()  - CALL "10"
*       opens a port for reading and writing at a given baud
*     for use with HDPR990 program   - no canonical processing.
*                  *******
*  INPUT:
*    path = a COBOL PIC X(nn) terminated with nulls not spaces.
*    baud = a COBOL PIC 9(04) COMP field.
*  OUTPUT:
*          a COBOL PIC S9(04) COMP field with the file number.
******************************************************************/

int hdpr990open(path, baud)

unsigned baud;
char *path;

{
   struct termio arg;
   int baud_rate, filedes, err;

   switch ( baud )
   {
                   case 300:  baud_rate = B300;
                              break;
                   case 600:  baud_rate = B600;
                              break;
                  case 1200:  baud_rate = B1200;
                              break;
                  case 1800:  baud_rate = B1800;
                              break;
                  case 2400:  baud_rate = B2400;
                              break;
                  case 4800:  baud_rate = B4800;
                              break;
                  case 9600:  baud_rate = B9600;
                              break;
                 case 19200:  baud_rate = B19200;
                              break;
                    default:  return(-3);  /* return -3 = illegal baud rate */
                              break;
   }

   filedes = open(path, O_RDWR);
   if (filedes < 0)
      return(-1);

   arg.c_iflag = IGNPAR | IGNBRK;
   arg.c_oflag = CRDLY | CR0;
   arg.c_cflag = baud_rate | CS7 | CLOCAL | CREAD | PARENB | HUPCL;
   arg.c_lflag = 0;

   arg.c_cc[VINTR] = CMAX;
   arg.c_cc[VQUIT] = CMAX;
   arg.c_cc[VERASE] = CMAX;
   arg.c_cc[VKILL] = CMAX;
   arg.c_cc[ 4  ] = 255;
   arg.c_cc[ 5  ] = 10;
   arg.c_cc[VSWTCH] = CSWTCH;

   errno = 0;
   err = ioctl(filedes, TCSETA, &arg);
   if (err >= 0)
      err = ioctl(filedes, TCFLSH, 2);

   perror("\nHDPR990 PORT CONFIGURATION");
   if (err < 0)
      return(-2);
   else
      return(filedes);

}


/*************************************************************
*          PORTTOGGLE() -  CALL "7"
*     This routine toggles a open port between
*   canonical line processing and timed i-o.
*
* INPUT:
*  filedes = a COBOL PIC S9(04) field holding the file number
*            returned from portopen().
**************************************************************/

int porttoggle(filedes)

int filedes;

{
   struct termio arg;
   int err;

   err = ioctl(filedes, TCGETA, &arg);
   if (err < 0)
      return(-1);


   if ((arg.c_lflag & ICANON) == 0)  { 
      arg.c_lflag = arg.c_lflag | ICANON;
      arg.c_cc[ 4  ] = 1;
      arg.c_cc[ 5  ] = 0;
   }
   else {
      arg.c_lflag = arg.c_lflag & ~ICANON;
      arg.c_cc[VEOF] = CEOF;
      arg.c_cc[VEOL] = 13;
   }

   err = ioctl(filedes, TCSETA, &arg);
   if (err < 0)
      return(-2);
   else 
      return(0);
}
