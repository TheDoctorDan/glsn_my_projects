#!/usr/local/bin/perl

print "Please enter a string to encode: ";
$string = <STDIN>;
chop ($string);

$string =~ s/(\W)/sprintf("%%%x", ord($1))/eg;

print "The encoded string is: ", "\n";
print $string, "\n";
exit(0);
