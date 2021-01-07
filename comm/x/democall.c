/******************************************************************************
 *
 *     COPYRIGHT 1984, 1986 by Micro Focus Ltd.
 *     All rights reserved. No part of this program or publication may be
 *   reproduced, transmitted, transcribed, stored in a retrieval system,
 *   or translated into any language or computer language, in any form or
 *   by any means, electronic, mechanical, magnetic, optical, chemical,
 *   manual or otherwise, without the prior written permission of 
 *    Micro Focus Ltd (0635) 32646                 
 *     26 West Street , Newbury , Berkshire             
 *
 *
 *  Start of source module democall.c Ver .1
 *                            source revision 1
 *     
 * @(#) Extended Addressing Version 1.0
 *
 * Last modified on 85/11/21 at 15:47:42
 *
 ******************************************************************************/
/*****************************************************************************
 *
 *   xequcall() ... execute a user's CALL'ed subroutine.
 *      The argument 'callnum' is the called routine number, in binary.
 *      The arguments to the routine, which appeared as USING names in the
 *        COBOL source, have been converted to absolute addresses and stored
 *        in the calargv[] array, in the same order they appeared on the
 *        source line. The number of USING parameters is held in the variable
 *        calargc. This format is similar to the 'argc,argv' convention
 *        used for command line arguments in C programs. Each pointer in 
 *        calargv[] is a pointer to a data area in the currently running
 *        COBOL program. This is referred to as "call by name".
 *
 *      NOTE: All accesses to Cobol data in Working Storage should go
 *        through the routines defined in "ugetput.h" for the following
 *        reasons:
 *        1) Convert between Cobol byte order and the processors byte order.
 *        2) Cope with alignment problems in Cobol Working Storage.
 *
 *        The routines are defined as follows:
 * 
 *        unsigned get1ws(address)    char *address;
 *        unsigned get2ws(address)    char *address;
 *        long     get3ws(address)    char *address;
 *        long     get4ws(address)    char *address;
 *
 *        put1ws(address, value)      char *address; unsigned value;
 *        put2ws(address, value)      char *address; unsigned value;
 *        put3ws(address, value)      char *address; long value;
 *        put4ws(address, value)      char *address; long value;
 *
 *      NOTE: Although get/put4ws use longs care must be taken with the sign.
 *        On systems where the type unsigned long is available it should
 *        be used instead.
 *
 *      The execution error message routine is also available, as shown 
 *        for the default case, this routine prints out the corresponding
 *        error message from "cblerrs". Caution should be exercised when using
 *        CALL'ed routines, since there is no run-time validation that the
 *        routine you wanted was the one you called.
 */

# define CPUgeneral     /* Change this for your processor */

# include "ugetput.h"   /* The data pickers and unpickers */

extern  char *calargv[] ;
extern  int  calargc    ;

#define ER_CALL  164    /* Specified call code not supplied */

xequcall( callnum )
{       
        long result;
        long fact();

        switch( callnum )
        {       
            default:    execerr( ER_CALL );
                        break;
        
            case 99:    result = fact( get2ws(calargv[ 0 ] ));
                        put4ws(calargv[ 1 ], result);
                        break;
        }
}


/* fact() - a simple function to find the factorial of a number
 *          very inefficently!!!!!
 */

long
fact(number)
unsigned number;

{       if (number == 0 || number == 1)
                return 1L;
        return (fact(number - 1) * number);
}
