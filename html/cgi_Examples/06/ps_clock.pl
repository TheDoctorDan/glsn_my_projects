#!/usr/local/bin/perl

$GS = "/usr/local/bin/gs";

$| = 1;
print "Content-type: image/gif", "\n\n";

($seconds, $minutes, $hour) = localtime (time);

$x = $y = 150;
open (GS, "|$GS -sDEVICE=gif8 -sOutputFile=- -q -g${x}x${y} - 2> /dev/null");

print GS <<End_of_PostScript_Code;

	%!PS-Adobe-3.0 EPSF-3.0
	%%BoundingBox: 0 0 $x $y
	%%EndComments

	/max_length	$x def
	/line_size      1.5 def
	/marker         5 def
	/origin		{0 dup} def
	/center		{max_length 2 div} def
	/radius		center def
	/hour_segment	{0.50 radius mul} def
	/minute_segment	{0.80 radius mul} def

	/red		{1 0 0 setrgbcolor} def
	/green		{0 1 0 setrgbcolor} def
	/blue		{0 0 1 setrgbcolor} def
	/black		{0 0 0 setrgbcolor} def

	/hour_angle {
		$hour $minutes 60 div add 3 sub 30 mul
		neg
	} def

	/minute_angle {
		$minutes $seconds 60 div add 15 sub 6 mul
		neg
	} def

	center dup translate
	black clippath fill
	line_size setlinewidth
	origin radius 0 360 arc blue stroke

	gsave
	1 1 12 {
		pop
		radius marker sub 0 moveto 
		marker 0 rlineto red stroke
		30 rotate
	} for
	grestore

	origin moveto
	hour_segment hour_angle cos mul
	hour_segment hour_angle sin mul 
	lineto green stroke

	origin moveto
	minute_segment minute_angle cos mul
	minute_segment minute_angle sin mul
	lineto green stroke

	origin line_size 2 mul 0 360 arc red fill

	showpage

End_of_PostScript_Code
	
close (GS);
exit(0);
