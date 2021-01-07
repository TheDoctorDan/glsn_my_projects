#!/usr/local/bin/perl

$nongraphic_browsers = 'Lynx|CERN-LineMode';
$client_browser  = $ENV{'HTTP_USER_AGENT'};

$graphic_document = "full_graphics.html";
$text_document = "text_only.html";

if ($client_browser =~ /$nongraphic_browsers/) {
    $html_document = $text_document;
} else {
    $html_document = $graphic_document;
}

print "Content-type: text/html", "\n\n";

$document_root = $ENV{'DOCUMENT_ROOT'};
$html_document = join ("/", $document_root, $html_document);    

if (open (HTML, "<" . $html_document)) {
    while (<HTML>) {
	print;
    }

    close (HTML);
} else {
    print "Oops! There is a problem with the configuration on this system!", "\n";
    print "Please inform the Webmaster of the problem. Thanks!", "\n";
}

exit (0);
