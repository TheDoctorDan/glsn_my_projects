#!/usr/local/bin/perl

print "Content-type: text/plain","\n\n";

print "Welcome to Shishir's WWW Server!", "\n";

$remote_host = $ENV{'REMOTE_HOST'};
print "You are visiting from ", $remote_host, ". ";

$uptime = `/usr/ucb/uptime`;
($load_average) = ($uptime =~ /average: ([^,]*)/);
print "The load average on this machine is: ", $load_average, ".", "\n";
print "Happy navigating!", "\n";

exit (0);

