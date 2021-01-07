#!/usr/local/bin/perl

$plaintext_file = $ENV{'PATH_TRANSLATED'};

print "Content-type: text/plain", "\n\n";

if ($plaintext_file =~ /\.\./) {
    print "Sorry! You have entered invalid characters in the filename.", "\n";
    print "Please check your specification and try again.", "\n";
} else {
    if (open (FILE, "<" . $plaintext_file)) {
        while (<FILE>) {
	    print;
        }

        close (FILE);
    } else {
        print "Sorry! The file you specified cannot be read!", "\n";
    }
}

exit (0);
