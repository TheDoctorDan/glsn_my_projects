.SUFFIXES: .c .o
#	make file for STYLE
#

.c.o:
	$(CC) -c $< $(CFLAGS)

CFLAGS=	-O ${INCLUDE}
BIN=/usr/gleeson/bin
MODE=711

style:	style1 style2 style3

style1: ${BIN}/style1

style2: ${BIN}/style2

style3: ${BIN}/style3

${BIN}/style3: prtlex.o pscan.o outp.o
	${CC} -o $@ prtlex.o pscan.o outp.o -ll $(LC_S)
	chmod ${MODE} $@

prtlex.c: part.l
	lex part.l
	mv lex.yy.c prtlex.c

prtlex.o:	names.h prtlex.c conp.h style.h names.h
pscan.o:	names.h conp.h pscan.c
outp.o:		names.h conp.h style.h outp.c
wdslex.o:	wdslex.c nhash.c dict.c ydict.c names.h abbrev.c
endlex.o:	names.h endlex.c ehash.c edict.c

${BIN}/style1: wdslex.o
	${CC} -o $@ wdslex.o -ll $(LC_S)
	chmod ${MODE} $@

wdslex.c: nwords.l
	lex nwords.l
	mv lex.yy.c wdslex.c

${BIN}/style2: endlex.o
	${CC} -o $@ endlex.o -ll $(LC_S)
	chmod ${MODE} $@

endlex.c: end.l
	lex end.l
	mv lex.yy.c endlex.c

