/******************************************************************************
 *
 *     COPYRIGHT 1984, 1986 by Micro Focus Ltd.
 *
 *    Micro Focus Ltd (0635) 32646                 
 *     26 West Street , Newbury , Berkshire             
 *
 *  Start of source module cbrun.c Ver .1
 *                            source revision 6
 *     
 * @(#) Run Time System Startup Program
 *
 * Last modified on 86/01/22 at 16:51:42
 *
 ******************************************************************************/

/*
 *    A helpful program to get us into the Level II cobol system...
 *
 *    This program is used to call the run-time systems for Micro Focus
 *    Level II (Enhanced Technology 1.1.5) COBOL.
 *
 *    The object file created from this source expects to be linked to
 *    other file names.
 *      cbrun   --- to run an int code or generated file.
 *      cobol   --- to compile a cobol program.
 *      animate --- to animate a program compiled for animation.
 *      cgen    --- to code generate an int code file.
 *      forms2  --- to run the forms2 screen generating program.
 *      cobprof --- to run the cobol profiler program
 *      upgrade3 -- to run the upgrade3 program
 *      convert3 -- to run the file converter
 *      convert5 -- to run another file converter
 *      reform5  -- to run another file converter
 *
 *    This program calls the appropriate rts with parameters, according to the 
 *    name which invoked it. Therefore, if the names are changed by copying,
 *    moving or linking, this source must be changed to recognize the new names.
 *
 *    The program is sensitive to several unix-type flags which are translated
 *    into legitimate rts flags. Thus:
 *
 *              -v      -- verbose: show the converted line before exec.
 *              -m***** -- memory size control flag.  
 *              -d***** -- dictionary size control flag.  
 *              -h      -- print help info.
 *
 *    All other flags, args starting with a + or - are passed directly to
 *    the RTS for interpretation.
 *
 *    All directives given in the long form must appear at the end
 *    of the command line.
 *
 *    The directory containing the compiler and rts may be specified by the
 *    environment variable COBDIR. If this is not set then a default value
 *    is used.
 */

# define NULL (char *)0

/* maximum number of arguments */
# define  ARGS  50 
char    *cmdbuff[ ARGS ];

char    **cmdargv = cmdbuff;

struct func {
        char *name;
        char *rts;
        char *dict;
        char *mem;
        char *prog;
};

struct func cbrun = { "cbrun", "rts32", NULL, "-m128000", NULL };
struct func cobol = { "cobol", "rts32", NULL, "-m200000", "COBOL" };
struct func anim  = { "animate", "rts32", "-d30000", "-m250000", "COBOL" };
struct func cgen  = { "cgen", "rts32", NULL, "-m150000", "ncg"};
struct func prof  = { "cobprof", "rts32", NULL, "-m50000", "PROFILER"};
struct func form  = { "forms2", "rts32", NULL, "-m70000", "FORMS2"};
struct func upgr  = { "upgrade3", "rts32", NULL, "-m80000", "upgrade3"};
struct func conv3 = { "convert3", "rts32", NULL, "-m80000", "FRMTOMF"};
struct func conv5 = { "convert5", "rts32", NULL, "-m80000", "FDGTOMF"};
struct func ref5  = { "reform5", "rts32", NULL, "-m80000", "RDGTOMF"};

struct func *list[] = { 
&cbrun, &cobol, &anim, &cgen, &prof, &form, &upgr, &conv3, &conv5, &ref5,
(struct func *)0 
};

main( argc, argv )
int     argc;
char    **argv ;

{       register char *s ;
        register char **avp;            /* argument vector pointer */
        register a ;
        struct func *cmd;
        int i;
        char    *cmd_name;
        char    *dictflag, *memflag;
        int     name_read = 0;
        short vflag =0 ;
        char *rindex();

        static char compbuf[128];
        static char rtsbuf[128];

/* get last element of path name */
        if(cmd_name = rindex(argv[0], '/'))
                cmd_name++;
        else
                cmd_name = argv[0];

/* set cmd to according to name */
        for(i = 0; cmd = list[i]; i++)
                if (strcmp(cmd->name, cmd_name) == 0)
                        break;

/* error if command name is not recognised */
        if(cmd == (struct func *)0 )
                errmsg("Invalid command name ", cmd_name);

/* set default dictionary and memory flags */
        dictflag = cmd->dict;
        memflag  = cmd->mem;

        avp = cmdargv;

        addarg(cmd->rts, avp++);        /* command name is first arg */
/* for each argument */
        a = 1;
        while (a < argc && ! name_read)
        {
                s = argv[a++];
                if (*s  == '-') 
                        switch (s[1])
                        {
                                default:  addarg(s, avp++); break;
                                case 'd': dictflag = s; break;
                                case 'm': memflag = s; break;
                                case 'v': vflag++ ; break;
                                case 'h': help(cmd); return;
                        }
                
                else if (*s == '+')
                        addarg(s, avp++);
                else    
                /* first non-flag character read */
                        name_read++;
        }
                                        /* copy rts control flags */
        if (dictflag)
            addarg(dictflag, avp++);

        if (memflag)
            addarg(memflag, avp++);

        if (cmd->prog)
        {
            mkcobdir(compbuf, cmd->prog);
            addarg(compbuf, avp++);
        }

        if (name_read)                  /* add name to command list */
        {                               /* s contains the name here */
            if (cmd == &cbrun)          /* if we are running comms... */
                if (strcmp(s, "COMMS") == 0 || strcmp(s, "comms") == 0)
                    s = "$comms";
            addarg(s, avp++);           /* add name to list */
        }

        while (a < argc)                /* copy rest of arguments */
            addarg(argv[a++], avp++);

        if(cmd == &anim)
        {
            if (!name_read)
                    errmsg("No file name given", NULL);
            addarg("NOANIM + RNIM",avp++);
        }

        addarg( NULL, avp ) ;   /* terminate the list of args for execv */

/* generate the full path name of the rts */
        mkcobdir(rtsbuf, cmd->rts);

/* if verbose flag is set, print out command line */
        if( vflag )
        {
                mesg(rtsbuf);
                for( avp = &cmdargv[1] ; *avp ; avp++ )
                {       mesg(" ");
                        mesg(*avp) ;
                }
                mesg("\n");
         }

        execv(rtsbuf,  cmdargv ) ;

        errmsg("cannot exec ", rtsbuf);
}


/* add an argument to the argument list */
addarg( arg, avp )  char *arg ; char **avp ;
{       
        if( avp > &cmdargv[ ARGS ] ) 
            errmsg("too many args", NULL);
        *avp = arg ;
}

/* give help */
help(cmd)
register struct func *cmd;

{       int rtsflag = 0;

        mesg("Usage : To ");
        if  (cmd == &cbrun)
        {
                mesg("execute a compiled/code generated program\n\n");
                mesg(cmd->name);
                mesg(" [rts-param] [debug-param] prog [prog-params]\n");
                rtsflag = 1;
        } 
        else if (cmd == &cobol)
        {
                mesg("compile a Level II (Enhanced Technology ET1.1.5) program\n\n");
                mesg(cmd->name);
                mesg(" [rts-param] source-program [compiler directives]\n");
                rtsflag = 1;
        }
        else if (cmd == &anim)
        {
                mesg("animate a compiled program\n\n");
                mesg(cmd->name);
                mesg(" [rts-param] int-program\n");
                rtsflag = 1;
        }
        else if (cmd == &cgen)
        {
                mesg("code generate an intermediate code program\n\n");
                mesg(cmd->name);
                mesg(" [rts-param] int-program [code generator directives]\n");
                rtsflag = 1;
        }
        else if (cmd == &form)
        {       
                mesg("run the forms2 program\n\n");
                mesg(cmd->name);
                mesg("\n");
        }
        else if (cmd == &prof)
        {       
                mesg("produce profiler statistics after a run\n\n");
                mesg(cmd->name);
                mesg(" program [profiler directives]\n");
        }
        else if (cmd = &upgr)
        {
                mesg("upgrade an RM program to Level II (Enhanced Technology ET1.1.5)\n\n");
                mesg(cmd->name);
                mesg(" program [upgrade directives]\n");
        }
        if (rtsflag)
        {       mesg("\nWhere [rts-param] can be one or more of the following\n");
                mesg("\t-v       show the built command line\n");
                mesg("\t-dnnnnn  set the in core Dictionary size to nnnnn\n");
                mesg("\t-mnnnnn  set the Memory size for programs to nnnnn\n");
                mesg("\t-h       print this information\n");
        }
}



/* give and error message and exit */
errmsg( msg, name )
        char *msg ;
        char *name ;
{
        mesg("Level II (Enhanced Technology ET1.1.5) error: ");
        if (msg) mesg( msg ) ;
        if (name) mesg( name ) ;
        mesg("\n");
        exit( 1 ) ;
}


/* print out string on error output */
mesg( str )
        register char *str ;
{
        register char *e ;
        for( e = str ;  *e  ; e++ ) ;   /* find the end */
        write( 2, str, e-str );         /* to the standard ERROR output. */
}


/***************************************************************************
 * mkcobdir - given a basename add the appropriate COBDIR to it and return
 * a pointer to the full name.
 */


mkcobdir(cdbuf, name)
        char *cdbuf;
        char *name;

{       char *getenv();
        register char *cp;
        register char *tp = cdbuf;

        if ((cp = getenv("COBDIR")) == (char *)0)
                cp = "/usr/lib/cobol";

        while (*tp = *cp)                       /* copy directory */
        {       tp++;
                cp++;
        }
        if ( tp > cdbuf && *(tp - 1) != '/')    /* terminate */
                *tp++ = '/';
        while (*tp++ = *name++) ;               /* copy name */
}

/* returns the last occurence of ch in str, or zero if it does not occur */
char *rindex(str, ch)
register char *str;
register char ch;
{
        register char *r;

        for(r = 0; *str; str++)
                if (*str == ch)
                        r = str;

        return(r);
}

