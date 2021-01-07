#!/usr/local/bin/perl

require "common.pl";

$webmaster = "shishir\@bu\.edu";
$gateway = "CGI Mail Gateway [v1.0]";

$request_method = $ENV{'REQUEST_METHOD'};
$sendmail = "/usr/lib/sendmail -t -n";

$address_file = "/home/shishir/httpd_1.4.2/cgi-bin/address.dat";

$exclusive_lock = 2;
$unlock = 8;

if ( defined ($address_file) && (-e $address_file) ) {
	&load_address (*address);
}

&parse_form_data (*MAIL);

if ($request_method eq "GET") {
	&display_form ();
	
} elsif ($request_method eq "POST") {
	
	if ( defined (%address) ) {
		$check_status = &check_to_address ();

		if (!$check_status) {
			&return_error (500, "$gateway Error",
				"The address you specified is not allowed.");
		}
	}

	if ( (!$MAIL{'from'}) || (!$MAIL{'email'}) ) {
		&return_error (500, "$gateway Error", "Who are you ?");
	} else {
		&send_mail ();		
		&return_thanks ();
	}

} else {
	&return_error (500, "Server Error",
		            "Server uses unsupported method");
}

exit(0);


sub load_address
{
	local (*ADDRESS_DATA) = @_;
	local ($name, $address);

	open (FILE, $address_file) || &return_error (500, "$gateway Error",
		    "Cannot open the address file [$address_file].");

	flock (FILE, $exclusive_lock);

	while (<FILE>) {
		chop if (/\n$/);
		($name, $address) = split (/,/, $_, 2);

		$ADDRESS_DATA{$name} = $address;
	}

	flock (FILE, $unlock);

	close (FILE);
}

sub display_form
{
	local ($address_to);

	print "Content-type: text/html", "\n\n";
	
	$address_to = &determine_to_field ();

	print <<End_of_Mail_Form;

<HTML>
<HEAD><TITLE>A WWW Gateway to Mail</TITLE></HEAD>
<BODY>
<H1>$gateway</H1>
This form can be used to send mail through the World Wide Web.
Please fill out all the necessary information.
<HR>
<FORM METHOD="POST">
<PRE>
Full Name:  <INPUT TYPE="text" NAME="from" VALUE="$MAIL{'from'}" SIZE=40>
E-Mail:     <INPUT TYPE="text" NAME="email" VALUE="$MAIL{'email'}" SIZE=40>
To:         $address_to
CC:         <INPUT TYPE="text" NAME="cc" VALUE="$MAIL{'cc'}" SIZE=40>

Subject:    <INPUT TYPE="text" NAME="subject" VALUE="$MAIL{'subject'}" SIZE=40>

<HR>

Please type the message below:

<TEXTAREA ROWS=10 COLS=60 NAME="message"></TEXTAREA>

</PRE>
<INPUT TYPE="hidden" NAME="url" VALUE="$MAIL{'url'}">
<INPUT TYPE="submit" VALUE="Send the Message">
<INPUT TYPE="reset"  VALUE="Clear the Message">
</FORM>
<HR>
</BODY></HTML>

End_of_Mail_Form

}

sub determine_to_field
{
	local ($to_field, $key, $selected);

	if (%address) {
		$to_field = '<SELECT NAME="to">';
		foreach $key (keys %address) {
			if ( ($MAIL{'to'} eq $key) ||
			     ($MAIL{'to'} eq $address{$key}) ) {
		
				$selected = "<OPTION SELECTED>";
			} else {
				$selected = "<OPTION>";
			}

			$to_field = join ("\n", $to_field,
						$selected, $key);
		}
		$to_field = join ("\n", $to_field, "</SELECT>");
	} else {
		$to_field = 
		qq/<INPUT TYPE="text" NAME="to" VALUE="$MAIL{'to'}" SIZE=40>/;
	}

	return ($to_field);
}

sub check_to_address
{	
	local ($status, $key);

	$status = 0;

	foreach $key (keys %address) {
	     if ( ($MAIL{'to'} eq $key) || ($MAIL{'to'} eq $address{$key}) ) {
			$status = 1;

			$MAIL{'to'} = $address{$key};
	     }
	}

	return ($status);
}

sub send_mail
{
	open (SENDMAIL, "| $sendmail");

	print SENDMAIL <<Mail_Headers;
From: $MAIL{'from'} <$MAIL{'email'}>
To: $MAIL{'to'}
Reply-To: $MAIL{'email'}
Subject: $MAIL{'subject'}
X-Mailer: $gateway
X-Remote-Host: $ENV{'REMOTE_ADDR'}
Mail_Headers

	if ($MAIL{'cc'}) {
		print SENDMAIL "Cc: ", $MAIL{'cc'}, "\n";
	}

	print SENDMAIL "\n", $MAIL{'message'}, "\n";	

	close (MAIL);
}

sub return_thanks
{
	if ($MAIL{'url'}) {
		print "Location: ", $MAIL{'url'}, "\n\n";
	} else {
		print "Content-type: text/html", "\n\n";

		print <<Thanks;
<HTML>
<HEAD><TITLE>$gateway</TITLE></HEAD>
<BODY>
<H1>Thank You!</H1>
<HR>
Thanks for using the mail gateway. Please feel free to use it again.
</BODY></HTML>

Thanks

	}
}

		

