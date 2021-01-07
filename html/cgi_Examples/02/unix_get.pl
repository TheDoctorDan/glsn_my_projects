#!/usr/local/bin/perl

print "Content-type: text/plain", "\n\n";

$query_string = $ENV{'QUERY_STRING'};
($field_name, $command) = split (/=/, $query_string);

if ($command eq "fortune") {
    print `/usr/local/bin/fortune`;
} elsif ($command eq "finger") {
    print `/usr/ucb/finger`;
} else {
    print `/usr/local/bin/date`;
}

exit (0);
