#!/usr/local/bin/perl

print "Content-type: text/html", "\n\n";

$webmaster = "shishir";

($seconds, $minutes, $hour) = localtime (time);

if ( ($hour >= 23) || ($hour <= 6) ) {
        $greeting = "Wow, you are up late";
} elsif ( ($hour > 6) && ($hour < 12) ) {
        $greeting = "Good Morning";
} elsif ( ($hour >= 12) && ($hour <= 18) ) {
        $greeting = "Good Afternoon";
} else {
        $greeting = "Good Evening";
}

if ($hour > 12) {
	$hour -= 12;
} elsif ($hour == 0) {
        $hour = 12;
}

$time = sprintf ("%02d:%02d:%02d", $hour, $minutes, $seconds);

open(CHECK, "/usr/bin/w -h -s $webmaster |");

if (<CHECK> =~ /$webmaster/) {
   $in_out = "I am currently logged in.";
} else {
   $in_out = "I just stepped out.";
}

close (CHECK);

print <<End_of_Homepage;

<HTML>
<HEAD><TITLE>Welcome to my homepage</TITLE></HEAD>

<BODY>
$greeting! It is $time. Here are some of my favorite links:
.
. (some information)
.
<ADDRESS>
Shishir Gundavaram ($in_out)
</ADDRESS>

</BODY></HTML>

End_of_Homepage

exit(0);




