#!/usr/local/bin/perl

require "common.pl";

$webmaster = "Shishir Gundavaram (shishir\@bu\.edu)";
$archie = "/usr/local/bin/archie";
$error = "CGI Archie Gateway Error";
$default_server = "archie.rutgers.edu";
$timeout_value = 180;

%servers = ( 'ANS Net (New York, USA)',         'archie.ans.net',
             'Australia',                       'archie.au',
             'Canada',                          'archie.mcgill.ca',
             'Finland/Mainland Europe',         'archie.funet.fi',
             'Germany',                         'archie.th-darmstadt.de',
             'Great Britain/Ireland',           'archie.doc.ac.ac.uk',
             'Internic Net (New York, USA)',    'ds.internic.net',
             'Israel',                          'archie.ac.il',
             'Japan',                           'archie.wide.ad.jp',
             'Korea',                           'archie.kr',
             'New Zealand',                     'archie.nz',
             'Rutgers University (NJ, USA)',    'archie.rutgers.edu',
             'Spain',                           'archie.rediris.es',
             'Sweden',                          'archie.luth.se',
             'SURANet (Maryland, USA)',         'archie.sura.net',
             'Switzerland',                     'archie.switch.ch',
             'Taiwan',                          'archie.ncu.edu.tw',
             'University of Nebrasksa (USA)',   'archie.unl.edu' );

$request_method = $ENV{'REQUEST_METHOD'};

if ($request_method eq "GET") {
	&display_form ();

} elsif ($request_method eq "POST") {
	&parse_form_data (*FORM);
	$command = &parse_archie_fields ();

	$SIG{'ALRM'} = "time_to_exit";
	alarm ($timeout_value);
	open (ARCHIE, "$archie $command |");

	$first_line = <ARCHIE>;

	if ($first_line =~ /(failed|Usage|WARNING|Timed)/) {
		&return_error (500, $error,
			"The archie client encountered a bad request.");
	} elsif ($first_line =~ /No [Mm]atches/) {
		&return_error (500, $error,
		"There were no matches for <B>$FORM{'query'}</B>.");
	}

	print "Content-type: text/html", "\n\n";
	print "<HTML>", "\n";
	print "<HEAD><TITLE>", "CGI Archie Gateway", "</TITLE></HEAD>", "\n";
	print "<BODY>", "\n";
	print "<H1>", "Archie search for: ", $FORM{'query'}, "</H1>", "\n";
	print "<HR>", "<PRE>", "\n";

	while (<ARCHIE>) {
		if ( ($host) = /^Host (\S+)$/ ) {
			$host_url = join ("", "ftp://", $host);
			s|$host|<A HREF="$host_url">$host</A>|;

			<ARCHIE>;

		} elsif (/^\s+Location:\s+(\S+)$/) {
			$location = $1;
			s|$location|<A HREF="${host_url}${location}">$location</A>|;
		} elsif ( ($type, $file) = /^\s+(DIRECTORY|FILE).*\s+(\S+)/) {
			s|$type|<I>$type</I>|;
			s|$file|<A HREF="${host_url}${location}/${file}">$file</A>|;
		} elsif (/^\s*$/) {
			print "<HR>";
		}

		print;
	}
    
	$SIG{'ALRM'} = "DEFAULT";
	close (ARCHIE);

	print "</PRE>";
	print "</BODY></HTML>", "\n";
} else {
	&return_error (500, $error, "Server uses unspecified method");
}

exit (0);

sub time_to_exit
{
	close (ARCHIE);

	&return_error (500, $error,
		"The search was terminated after $timeout_value seconds.");
}

sub parse_archie_fields
{
	local ($query, $server, $type, $address, $status, $options);

	$status = 1;

	$query = $FORM{'query'};
	$server = $FORM{'server'};
	$type = $FORM{'type'};

	if ($query !~ /^\w+$/) {
		&return_error (500, $error, 
			"Search query contains invalid characters.");
	} else {
		foreach $address (keys %servers) {
			if ($server eq $address) {
				$server = $servers{$address};
				$status = 0;
			}
		}

		if ($status) {
			&return_error (500, $error,
				"Please select a valid archie host.");
		} else {
			if ($type eq "cs_sub") {
				$type = "-c";
			} elsif ($type eq "ci_sub") {
				$type = "-s";
			} else {
				$type = "-e";
			}

			$options = "-h $server $type $query";

			return ($options);
		}
	}
}

sub display_form
{
	local ($archie);

	print <<End_of_Archie_One;
Content-type: text/html

<HTML>
<HEAD><TITLE>Gateway to Internet Information Servers</TITLE></HEAD>
<BODY>
<H1>CGI Archie Gateway</H1>
<HR>
<FORM ACTION="/cgi-bin/archie.pl" METHOD="POST">
Please enter a string to search from: <BR>
<INPUT TYPE="text" NAME="query" SIZE=40>
<P>
What archie server would you like to use (<B>please</B>, be considerate
and use the one that is closest to you): <BR>
<SELECT NAME="server" SIZE=1>

End_of_Archie_One

	foreach $archie (sort keys %servers) {
		if ($servers{$archie} eq $default_server) {
			print "<OPTION SELECTED>", $archie, "\n";
		} else {
			print "<OPTION>", $archie, "\n";
		}
	}		

	print <<End_of_Archie_Two;
</SELECT>
<P>
Please select a type of search to perform: <BR>
<INPUT TYPE="radio" NAME="type" VALUE="exact" CHECKED>Exact<BR>
<INPUT TYPE="radio" NAME="type" VALUE="ci_sub">Case Insensitive Substring<BR>
<INPUT TYPE="radio" NAME="type" VALUE="cs_sub">Case Sensitive Substring<BR>
<P>
<INPUT TYPE="submit" VALUE="Start Archie Search!">
<INPUT TYPE="reset"  VALUE="Clear the form">
</FORM>
<HR>
</BODY>
</HTML>

End_of_Archie_Two
}

