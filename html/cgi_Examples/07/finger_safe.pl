#!/usr/local/bin/perl

require "common.pl";

&parse_form_data(*simple);
$user = $simple{'user'};

if ($user =~ /[;><&\*`\|]/) {
    &return_error (500, "CGI Program Alert", "What are you trying to do?");
} else {
    print "Content-type: text/plain", "\n\n";
    print "Here are the results of your query: ", "\n";
    print `/usr/local/bin/finger $user`;
    print "\n";
}

exit (0);
