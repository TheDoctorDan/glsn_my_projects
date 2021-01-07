#!/usr/local/bin/perl

require "common.pl";

$webmaster = "shishir\@bu\.edu";
&parse_form_data (*simple_form);

print "Content-type: text/plain", "\n\n";
$user = $simple_form{'user'};

if ($user) {
    print "Nice to meet you ", $simple_form{'user'}, ".", "\n";
    print "Please visit this Web server again!", "\n";
} else {
    print "You did not enter a name. Are you shy?", "\n";
    print "But, you are welcome to visit this Web server again!", "\n";
}

exit(0);
