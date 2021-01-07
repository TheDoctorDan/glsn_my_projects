#!/usr/local/bin/perl

require "sockets.pl";

$service = "finger";
chop ($hostname = `/bin/hostname`);

$input = shift (@ARGV);
($username, $remote_host) = split (/@/, $input, 2);

unless ($remote_host) {
        $remote_host = $hostname;
}

&open_connection (FINGER, $remote_host, $service) 
	|| die "Cannot open connection to: $remote_host", "\n";

print FINGER $username, "\n";

while (<FINGER>) {
	print;
}

&close_connection (FINGER);
exit (0);
