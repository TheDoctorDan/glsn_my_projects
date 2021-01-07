CFLAGS=-O -c

rts32:  usercall.o
        cc -n -s rts32.o usercall.o -o rts32

usercall.o: ugetput.h
        cc $(CFLAGS) usercall.c

cbrun:  cbrun.c
        cc -O -s cbrun.c -o cbrun

fact:   fact.INT

fact.INT: fact.cbl
        cobol fact.cbl
