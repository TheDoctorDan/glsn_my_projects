#!/usr/local/bin/perl

print "Enter text to escape: ";
chop ($string = <STDIN>);
$string =~ s/([^\w\s])/sprintf ("&#%d;", ord ($1))/ge;

print "The escaped string is: ", "\n";
print $string, "\n";

exit (0);
				     
