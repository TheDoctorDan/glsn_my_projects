#!/usr/local/bin/perl5

require "common.pl";

use Sprite;

$webmaster = "shishir\@bu\.edu";
$query = undef;

&parse_form_data(*FORM);
$fields = '(Last|First|Job_Title|Department|EMail|Phone)';

foreach $key (keys %FORM) {
	if ( ($key !~ /\b$fields\b/o) || ($FORM{$key} =~ /[^\w\-\(\) ]/) ) {
		&return_error (500, "CGI Corporation Employee Database Error",
				"Invalid Information in Form.");
	} else {
		if ($FORM{$key}) {
   		        $FORM{$key} =~ s/(\W)/\\$1/g;

			$query = join (" and ", $query, 
			    "($key =~ /$FORM{$key}/i)");
		}
	}
}

if ($query) {
	$query =~ s/^ and //;
} else {
	&return_error (500, "CGI Corporation Employee Database Error",
			    "No query was entered.");
}

$rdb = new Sprite ();
$rdb->set_delimiter ('Read', ',');

@data = $rdb->sql (<<End_of_Query);
	select * from phone.db
	where $query
End_of_Query

$status = shift (@data);
$no_elements = scalar (@data);

if (!$status) {
	&return_error (500, "CGI Corporation Employee Database Error",
			    "Sprite Database Error!");
} elsif (!$no_elements) {
	&return_error (500, "CGI Corporation Employee Database Error",
                            "The record you specified does not exist.");
} else {
	print <<End_of_HTML;
Content-type: text/html

<HTML>
<HEAD><TITLE>CGI Corporation Employee Directory</TITLE></HEAD>
<BODY>
<H1>CGI Corporation Employee Directory</H1>
<HR><PRE>

End_of_HTML

	$~ = "HEADING";
	write;

	$~ = "EACH_ENTRY";
	
	foreach (@data) {
	        s/([^\w\s\0])/sprintf ("&#%d;", ord ($1))/ge;

		($last, $first, $job, $department, $email, $phone) =
			split (/\0/, $_, 6);
		write;
	}

	print "</PRE>", "\n";
	print "<HR>";
        print "</BODY></HTML>", "\n";
}

$rdb->close ();
exit (0);




format HEADING = 
Last       First      Job Title      Department   EMail       Phone
----       -----      ---------      ----------   -----       -----
.

format EACH_ENTRY = 
@<<<<<<<<  @<<<<<<<<  @<<<<<<<<<<<<  @<<<<<<<<<<  @<<<<<<<<<  @<<<<<<<<<<<<<
$last,     $first,    $job,          $department, $email,     $phone
.



