#!/usr/local/bin/perl

$GS = "/usr/local/bin/gs";

$| = 1;
print "Content-type: image/gif", "\n\n";

($seconds, $minutes, $hour) = localtime (time);

if ($hour > 12) {
	$hour -= 12;
	$ampm = "pm";
} else {
	$ampm = "am";
}

if ($hour == 0) {
        $hour = 12;
}

$time = sprintf ("%02d:%02d:%02d %s", $hour, $minutes, $seconds, $ampm);

$x = 80;
$y = 15;
open (GS, "|$GS -sDEVICE=gif8 -sOutputFile=- -q -g${x}x${y} - 2> /dev/null");

print GS <<End_of_PostScript_Code;

	%!PS-Adobe-3.0 EPSF-3.0
	%%BoundingBox: 0 0 $x $y
	%%EndComments

	/Times-Roman findfont 14 scalefont setfont

	/red   {1 0 0 setrgbcolor} def
	/black {0 0 0 setrgbcolor} def

	black clippath fill
	0 0 moveto
	($time) red show

	showpage

End_of_PostScript_Code

close (GS);
exit(0);
