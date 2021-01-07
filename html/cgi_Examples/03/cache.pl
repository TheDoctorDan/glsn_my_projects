#!/usr/local/bin/perl

chop ($current_date = `/bin/date`);
$script_name = $ENV{'SCRIPT_NAME'};

print "Content-type: text/html", "\n\n";
print "<HTML>", "\n";
print "<HEAD><TITLE>Effects of Browser Caching</TITLE></HEAD>", "\n";
print "<BODY><H1>", $current_date, "</H1>", "\n";
print "<P>", qq|<A HREF="$script_name">Click here to run again!</A>|, "\n";
print "</BODY></HTML>", "\n";

exit (0);
