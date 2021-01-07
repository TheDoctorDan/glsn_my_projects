#!/usr/local/bin/perl

$host_address = "bu\.edu";
$ip_address = "128\.197";

$remote_address = $ENV{'REMOTE_ADDR'};
$remote_host = $ENV{'REMOTE_HOST'};

$local_users = "internal_info.html";
$outside_users = "general.html";

if (($remote_host =~ /\.$host_address$/) && ($remote_address =~ /^$ip_address/)) {
    $html_document = $local_users;
} else {
    $html_document = $outside_users;
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
