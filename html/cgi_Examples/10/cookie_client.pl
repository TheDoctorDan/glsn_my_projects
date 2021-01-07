#!/usr/local/bin/perl

require "sockets.pl";

$webmaster = "Shishir Gundavaram (shishir\@bu\.edu)";
$remote_address = $ENV{'REMOTE_ADDR'};
$cookie_server = "cgi.bu.edu";
$cookie_port = 5000;

$document_root = "/home/shishir/httpd_1.4.2/public";
$error = "Cookie Client Error";

&parse_form_data (*FORM);
$start_form = $FORM{'start'};
$next_form = $FORM{'next'};
$cookie = $FORM{'Magic_Cookie'};

if ($start_form) {
    $cookie = &get_new_cookie ();
    &parse_form ($start_form, $cookie);

} elsif ($next_form) {
    &save_current_form ($cookie);
    &parse_form ($next_form, $cookie);

} else {
    if ($cookie) {
	&last_form ($cookie);
    } else {
	&return_error (500, $error,
		       "You have executed this script in an invalid manner.");
    }
}

exit (0);

sub open_and_check
{
    local ($first_line);

    &open_connection (COOKIE, $cookie_server, $cookie_port)
	|| &return_error (500, $error, "Could not connect to cookie server.");

    chop ($first_line = <COOKIE>);

    if ($first_line !~ /^200/) {
	&return_error (500, $error, "Cookie server returned an error.");
    }
}

sub get_new_cookie
{
    local ($cookie_line, $new_cookie);

    &open_and_check ();
    print COOKIE "new ", $remote_address, "\n";
    chop ($cookie_line = <COOKIE>);
    &close_connection (COOKIE);

    if ( ($new_cookie) = $cookie_line =~ /^200: (\S+)$/) {
	return ($new_cookie);
    } else {
	&return_error (500, $error, "New cookie was not created.");
    }
}

sub parse_form
{
    local ($form, $magic_cookie) = @_;
    local ($path_to_form);

    if ($form =~ /\.\./) {
        &return_error (500, $error, "What are you trying to do?");
    }

    $path_to_form = join ("/", $document_root, $form);

    open (FILE, "<" . $path_to_form)
	|| &return_error (500, $error, "Could not open form.");

    print "Content-type: text/html", "\n\n";

    while (<FILE>) {
	if (/-\*Cookie\*-/) {
	    s//$magic_cookie/g;
	}
	print;
    }

    close (FILE);
}

sub save_current_form
{
    local ($magic_cookie) = @_;
    local ($ignore_fields, $cookie_line, $key);

    $ignore_fields = '(start|next|Magic_Cookie)';

    &open_and_check ();
    print COOKIE "cookie $magic_cookie $remote_address", "\n";
    chop ($cookie_line = <COOKIE>);

    if ($cookie_line =~ /^200/) {
	foreach $key (keys %FORM) {
	    next if ($key =~ /\b$ignore_fields\b/o);
	    
	    print COOKIE $key, "=", $FORM{$key}, "\n";
	    chop ($cookie_line = <COOKIE>);

	    if ($cookie_line !~ /^200/) {
		&return_error (500, $error, "Form info. could not be stored.");
	    }
	}
    } else {
	&return_error (500, $error, "The cookie could not be set.");
    }

    &close_connection (COOKIE);
}

sub last_form
{
    local ($magic_cookie) = @_;
    local ($cookie_line, $key_value, $key, $value);

    &open_and_check ();
    print COOKIE "cookie $magic_cookie $remote_address", "\n";
    chop ($cookie_line = <COOKIE>);

    if ($cookie_line =~ /^200/) {
	print COOKIE "list", "\n";
	&display_all_items ();

	print COOKIE "delete", "\n";

    } else {
	&return_error (500, $error, "The cookie could not be set.");
    }

    &close_connection (COOKIE);
}

sub display_all_items
{
    local ($key_value, $key, $value);

    print "Content-type: text/html", "\n\n";
    print "<HTML>", "\n";
    print "<HEAD><TITLE>Summary</TITLE></HEAD>", "\n";
    print "<BODY>", "\n";
    print "<H1>Summary and Results</H1>", "\n";
    print "Here are the items/options that you selected:", "<HR>", "\n";

    while (<COOKIE>) {
	chop;
	last if (/^\.$/);

	$key_value = (split (/\s/, $_, 2))[1];
	($key, $value) = split (/=/, $key_value), "\n";
	    
	print "<B>", $key, " = ", $value, "</B>", "<BR>", "\n";
    }

    foreach $key (keys %FORM) {
	next if ($key =~ /^Magic_Cookie$/);

	print "<B>", $key, " = ", $FORM{$key}, "</B>", "<BR>", "\n";
    }
    
    print "</BODY></HTML>", "\n";
}

sub parse_form_data
{
    local (*FORM_DATA) = @_;

    local ($query_string, @key_value_pairs, $key_value, $key, $value);

    read (STDIN, $query_string, $ENV{'CONTENT_LENGTH'});

    if ($ENV{'QUERY_STRING'}) {
            $query_string = join("&", $query_string, $ENV{'QUERY_STRING'});
    }

    @key_value_pairs = split (/&/, $query_string);

    foreach $key_value (@key_value_pairs) {
        ($key, $value) = split (/=/, $key_value);
        $key   =~ tr/+/ /;
        $value =~ tr/+/ /;

        $key   =~ s/%([\dA-Fa-f][\dA-Fa-f])/pack ("C", hex ($1))/eg;
        $value =~ s/%([\dA-Fa-f][\dA-Fa-f])/pack ("C", hex ($1))/eg;

        if (defined($FORM_DATA{$key})) {
            $FORM_DATA{$key} = join ("\0", $FORM_DATA{$key}, $value);
        } else {
            $FORM_DATA{$key} = $value;
        }
    }
}

sub return_error
{
    local ($status, $keyword, $message) = @_;

    print "Content-type: text/html", "\n";
    print "Status: ", $status, " ", $keyword, "\n\n";

    print <<End_of_Error;

<title>CGI Program - Unexpected Error</title>
<h1>$keyword</h1>
<hr>$message</hr>
Please contact $webmaster for more information.

End_of_Error

    exit(1);
}








