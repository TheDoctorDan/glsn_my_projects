#!/usr/local/bin/perl

print "Content-type: text/plain", "\n";
print "Status: 204 No Response", "\n\n";

print "You should not see this message. If you do, your browser does", "\n";
print "not implement status codes correctly.", "\n";

exit (0);
