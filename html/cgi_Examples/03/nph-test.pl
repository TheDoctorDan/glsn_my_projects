#!/usr/local/bin/perl

$server_protocol = $ENV{'SERVER_PROTOCOL'};
$server_software = $ENV{'SERVER_SOFTWARE'};

print "$server_protocol 200 OK", "\n";
print "Server: $server_software", "\n";
print "Content-type: text/plain", "\n\n";

print "OK, Here I go. I am going to count from 1 to 50!", "\n";

for ($loop=1; $loop <= 50; $loop++) {
    print $loop, "\n";
    sleep (2);
}

print "All Done!", "\n";

exit (0);
