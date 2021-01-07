#!/usr/local/bin/perl

$webmaster = "shishir\@bu\.edu";
$document_root = "/home/shishir/httpd_1.4.2/public";
$request_method = $ENV{'REQUEST_METHOD'};
$form_file = $ENV{'PATH_INFO'};
$full_path = $document_root . $form_file;

$exclusive_lock = 2;
$unlock = 8;

if ($request_method eq "GET") {
	if ($form_file) {
		&display_file ();
	} else {
		&return_error (500, "CGI Shopping Cart Error",
			"An initial form must be specified.");
	}
} elsif ($request_method eq "POST") {
	&parse_form_data (*STATE);

	if ($form_file) {
		&parse_file ();
	} else {
		&thank_you ();
	}
} else {
	&return_error (500, "Server Error",
                        "Server uses unsupported method");
}

exit (0);

sub display_file
{
	open (FILE, "<" . $full_path) || 
		&return_error (500, "CGI Shopping Cart Error",
			"Cannot read from the form file [$full_path].");

	flock (FILE, $exclusive_lock);

	print "Content-type: text/html", "\n\n";
	while (<FILE>) {
		print;
	}

	flock (FILE, $unlock);
	close (FILE);	
}

sub parse_file
{
	local ($key, $value);

	open (FILE, "<" . $full_path) ||
		&return_error (500, "CGI Shopping Cart Error",
			"Cannot read from the form file [$full_path].");

	flock (FILE, $exclusive_lock);

	print "Content-type: text/html", "\n\n";

	while (<FILE>) {

		if (/<\s*form\s*.*>/i) {
			print;
		
			foreach $key (sort (keys %STATE)) {
				$value = $STATE{$key};
				print <<End_of_Hidden;
<INPUT TYPE="hidden" NAME="$key" VALUE="$value">	
End_of_Hidden
			}

		} else {
			print;
		}

	}	

	flock (FILE, $unlock);
	close (FILE);

}

sub thank_you
{
	local ($key, $value);

	print <<Thanks;
Content-type: text/html

<HTML>
<HEAD><TITLE>Thank You!</TITLE></HEAD>
<BODY>
<H1>Thank You!</H1>
Thank you again for using our service. Here are the items
that you selected: 
<HR>
<P>

Thanks

	foreach $key (sort (keys %STATE)) {
		$value = $STATE{$key};

	        $key =~ s/^\d+\s//;

		if ($value =~ /\0/) {
			print "<B>", $key, "</B>", "<BR>", "\n";
			$value =~ s/\0/<BR>\n/g; 
			print $value, "<BR>", "\n"; 
		} else {
			print $key, ": ", $value, "<BR>", "\n";
		}
	}

	print "<HR>", "\n";
        print "</BODY></HTML>", "\n";
}

sub parse_form_data
{
    local (*FORM_DATA) = @_;
    
    local ($query_string, @key_value_pairs, $key_value, $key, $value);
    
    read (STDIN, $query_string, $ENV{'CONTENT_LENGTH'});

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


	
