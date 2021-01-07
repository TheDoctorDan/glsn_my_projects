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
 *  Start of source module ugetput.h Ver .1
 *                            source revision 1
 *     
 * @(#) Extended Addressing Version 1.0
 *
 * Last modified on 85/11/21 at 15:49:06
 *
 ******************************************************************************/
/*
 *    the data pickers and unpickers.
 *      the structures are very machine dependent.
 *      the get macros must be of type unsigned, and prevent extension.
 *    the macros could be better tuned to your target machine...
 *      these represent a compromise for several machines and compilers.
 * 
 *    see usercall.c for full details.
 */

# ifdef  CPUPDP11
extern struct worder { char  lobyte, hibyte ; }  ;   /* verbose one. */
extern struct w2b    { char  wlb, whb ; }        ;   /* smaller for macros */
extern struct l4b    { char hwlb, hwhb, lwlb, lwhb ; } ;  /* longs */
extern struct l2w    { unsigned short hiword, loword ; } ;/* longs */
# endif

# ifdef  CPUZ8000                               /*  same as COBOL order */
extern struct worder { char  hibyte, lobyte ; }  ;
extern struct w2b    { char  whb, wlb ; }        ;
extern struct l4b    { char hwhb, hwlb, lwhb, lwlb ; } ;
extern struct l2w    { unsigned short hiword, loword ; } ;/* longs */
# endif

# ifdef  CPU68000       /* same as COBOL order, hi lo. */
extern struct worder { char  dummy1, dummy2, hibyte, lobyte ; }  ;
extern struct w2b    { char  dummy3, dummy4, whb, wlb ; }        ;
extern struct l4b    { char hwhb, hwlb, lwhb, lwlb ; } ;
extern struct l2w    { unsigned short hiword, loword ; } ;/* longs */
# endif

# ifdef  CPU3B20        /*  same as COBOL order, 32 bit integer */
                        /* Also covers 3B2 and 3B5 processors   */
extern struct worder { char  dummy1, dummy2, hibyte, lobyte ; }  ;
extern struct w2b    { char  dummy3, dummy4, whb, wlb ; }        ;
extern struct l4b    { char hwhb, hwlb, lwhb, lwlb ; } ;
extern struct l2w    { unsigned short hiword, loword ; } ;/* longs */
# endif

# ifdef CPUPYRAMID
extern struct worder { char  dummy1, dummy2, hibyte, lobyte ; }  ;
extern struct w2b    { char  dummy3, dummy4, whb, wlb ; }        ;
extern struct l4b    { char hwhb, hwlb, lwhb, lwlb ; } ;
extern struct l2w    { unsigned short hiword, loword ; } ;/* longs */
# endif

# ifdef CPUNS16000
extern struct worder { char  lobyte, hibyte, dummy1, dummy2 ; }  ;
extern struct w2b    { char  wlb, whb, dummy3, dummy4 ; }        ;
extern struct l4b    { char lwlb, lwhb, hwlb, hwhb ; } ;
extern struct l2w    { unsigned short loword, hiword ; } ;/* longs */
# endif

# ifdef CPUVAX
extern struct worder { char  lobyte, hibyte, dummy1, dummy2 ; }  ;
extern struct w2b    { char  wlb, whb, dummy3, dummy4 ; }        ;
extern struct l4b    { char lwlb, lwhb, hwlb, hwhb ; } ;
extern struct l2w    { unsigned short loword, hiword ; } ;/* longs */
# endif

extern char *zp;                /* zp and zu are defined in rts??.o */
extern union wrd2b {
        unsigned u;
        struct w2b s2;
} zu;

# define get1ws(a) ((unsigned)(*(char *)(a) & 0xFF))
# define get2ws(a) (zu.u = 0, zu.s2.whb = *((char *)(a)), zu.s2.wlb = *((char *)(a)+1), zu.u )
extern long  get3ws() ; /*  routine defined in rts??.o */
extern long  get4ws() ; /*  routine defined in rts??.o */

# define put1ws(a,v) (*((char *)(a)) =  (v))
# define put2ws(a,v) (zp=(char *)(a),zu.u=(unsigned)(v),*zp=zu.s2.whb,*(zp+1)=zu.s2.wlb)
