#!/usr/local/bin/perl

$remote_host = $ENV{'REMOTE_HOST'};

print "Content-type: text/plain", "\n";

if ($remote_host eq "bu.edu") {
    print "Status: 200 OK", "\n\n";
    print "Great! You are from Boston University!", "\n";
} else {
    print "Status: 400 Bad Request", "\n\n";
    print "Sorry! You need to access this from Boston University!", "\n";
}

exit (0);
