#!/usr/local/bin/perl

print "Content-type: text/plain", "\n\n";

$query_string = $ENV{'QUERY_STRING'};

if ($query_string eq "fortune") {
    print `/usr/local/bin/fortune`;
} elsif ($query_string eq "finger") {
    print `/usr/ucb/finger`;
} else {
    print `/usr/local/bin/date`;
}

exit (0);
