#!/usr/local/bin/perl

@URL = ("http://www.ora.com",
        "http://www.digital.com",
        "http://www.ibm.com",
        "http://www.radius.com");

srand (time | $$);

$number_of_URL = $#URL;
$random = int (rand ($number_of_URL));

$random_URL = $URL[$random];

print "Content-type: text/html", "\n\n";
print qq|<A HREF="$random_URL">Click here for a random Web site!</A>|, "\n";

exit (0);


