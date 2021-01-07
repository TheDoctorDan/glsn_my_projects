#!/usr/local/bin/perl

$GS = "/usr/local/bin/gs";

$| = 1;
print "Content-type: image/gif", "\n\n";

$uptime = `/usr/ucb/uptime`;
($load_averages) = ($uptime =~ /average: (.*)$/);
@loads[0..2] = split(/,\s/, $load_averages);

for ($loop=0; $loop < 2; $loop++) {
	if ($loads[$loop] > 10) {
		$loads[$loop] = 10;
	}
}

$x = $y = 175;
open (GS, "|$GS -sDEVICE=gif8 -sOutputFile=- -q -g${x}x${y} - 2> /dev/null");

print GS <<End_of_PostScript_Code;

	%!PS-Adobe-3.0 EPSF-3.0
	%%BoundingBox: 0 0 $x $y
	%%EndComments

	/black  {0 0 0 setrgbcolor} def
	/red	{1 0 0 setrgbcolor} def
	/blue   {0 0 1 setrgbcolor} def

	/origin {0 dup} def

	15 150 moveto
	/Times-Roman findfont 16 scalefont setfont
	(System Load Average) blue show

	30 30 translate
	
	1 setlinewidth
	origin moveto 105 0 rlineto black stroke
	origin moveto 0 105 rlineto black stroke

	origin moveto
	0 1 10 {	
		10 mul 5 neg exch moveto
		10 0 rlineto blue stroke
	} for

	origin moveto
	0 1 4 {
		25 mul 5 neg moveto
		0 10 rlineto blue stroke
	} for

	newpath
	origin moveto
	25 $loads[0] 10 mul lineto  
	50 $loads[1] 10 mul lineto 
	75 $loads[2] 10 mul lineto 
	100 0 lineto
	closepath
	red fill

	showpage

End_of_PostScript_Code

close (GS);
exit(0);

