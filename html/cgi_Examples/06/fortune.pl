#!/usr/local/bin/perl

$fortune = "/usr/local/bin/fortune";
$refresh_time = 10;

print "Refresh: ", $refresh_time, "\n";
print "Content-type: text/plain", "\n\n";

print "Here is another fortune...", "\n";
print `$fortune`;

exit(0);
