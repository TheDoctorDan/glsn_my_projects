#!/usr/local/bin/perl

print "Content-type: text/plain", "\n\n";

$remote_address = $ENV{'REMOTE_ADDR'};
$referral_address = $ENV{'HTTP_REFERER'};

print "Hello user from $remote_address!", "\n";
print "The last site you visited was: $referral_address. Am I genius or what?", "\n";

exit (0);
