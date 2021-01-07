#!/usr/local/bin/perl

$uptime = `/usr/ucb/uptime`;
($load_average) = ($uptime =~ /average: ([^,]*)/);

$load_limit = 10.0;
$simple_document = "/simple.html";
$complex_document = "/complex.html";

if ($load_average >= $load_limit) {
    print "Location: $simple_document", "\n\n";
} else {
    print "Location: $complex_document", "\n\n";
}

exit (0);
