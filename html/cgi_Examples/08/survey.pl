#!/usr/local/bin/perl

$exclusive_lock = 2;
$unlock = 8;
$request_method = $ENV{'REQUEST_METHOD'};
$webmaster = "shishir\@bu\.edu";

$document_root = "/home/shishir/httpd_1.4.2/public";
$survey_dir = "/tmp/";

@Television_files    = ( "/tv_1.html", "/tv_2.html" );
@Movie_files = ( "/movie_1.html", "/movie_2.html" );

if ($request_method eq "GET") {

	$form_num = 0;
	$type = "start";
	$form_file = $ENV{'PATH_INFO'};

	if ($form_file) {
		$cookie = join ("_", $ENV{'REMOTE_HOST'}, time);
		$cookie = &escape($cookie);
		&pseudo_ssi ($form_file, $cookie, $type, $form_num);
	} else {
		&return_error (500, "CGI Network Survey Error", 
		        "An initial survey form must be specified.");
	}

} elsif ($request_method eq "POST") {

	&parse_form_data(*STATE);
	$form_num = $STATE{'cgi_form_num'};
	$type = $STATE{'cgi_survey'};
	$cookie = $STATE{'cgi_cookie'};

	if ( ($type eq "Television") || ($type eq "Movie") ) {
		$limit = eval ("scalar (\@${type}_files)");
		
		if ( ($form_num >= 0) && ($form_num <= $limit) ) {
			&write_data_to_file();

			if ($form_num == $limit) {
				&survey_over();
			} else {
				$form_file =eval("\$${type}_files[$form_num]");
				$form_num++;
				$cookie = &escape($cookie);
				&pseudo_ssi ($form_file, $cookie, $type, 
				             $form_num);
			}
		} else {
		        &return_error (500, "CGI Network Survey Error",
		            "You have somehow selected an invalid form!");
		}

	} else {
		&return_error (500, "CGI Network Survey Error",
		        "You have selected an invalid survey type!");
	}
} else {
	&return_error (500, "Server Error",
                        "Server uses unsupported method");
}

exit(0);


sub pseudo_ssi
{
	local ($file, $id, $kind, $number) = @_;
	local ($command, $argument, $parameter, $line);

	$file = $document_root . $file;

	open (FILE, "<" . $file) ||
		&return_error (500, "CGI Network Survey Error",
			"Cannot open: form [$number], file [$file].");

	flock (FILE, $exclusive_lock);

	print "Content-type: text/html", "\n\n";

	while (<FILE>) {
	    while ( ($command, $argument, $parameter) = 
		    (/<!--\s*#\s*(\w+)\s+(\w+)\s*=\s*"?(\w+)"?\s*-->/io) ) {

	        if ($command eq "insert") {
		    if ($argument eq "var") {
	                if ($parameter eq "COOKIE") {
			    s//$id/;          
	                } elsif ($parameter eq "DATE_TIME") {
		            local ($time) = &get_date_time();
	                    s//$time/;          
			} elsif ($parameter eq "NUMBER") {
			    s//$number/;
			} elsif ($parameter eq "SURVEY") {
		            s//$kind/;
			} else {
			    s///;
			}
		    } else {
			s///;
		    }
		} else {
		    s///;
		}
	    }
	
	    print;

	}

	flock (FILE, $unlock);
	close (FILE);
}

sub write_data_to_file
{
	local ($key, $temp_key);

	open (FILE, ">>" . $survey_dir . $cookie) || 
                    &return_error (500, "CGI Network Survey Error",
                        "Cannot write to a data file to store your info.");

	if ($form_num == 0) {
		print FILE $STATE{'cgi_survey'}, " Survey Filled Out", "\n";
	}

	foreach $key (sort (keys %STATE)) {
		if ($key !~ /^cgi_/) {
			($temp_key = $key) =~ s/^\d+\s//;
			print FILE $temp_key, ": ", $STATE{$key}, "\n";
		}
	}

	close (FILE);
}


sub survey_over
{
    local ($file) = $survey_dir . $cookie;

    open (FILE, "<" . $file) || 
                &return_error (500, "CGI Network Survey Error",
                                 "Cannot read the survey data file [$file].");

    print <<Thanks;
Content-type: text/html

<HTML>
<HEAD><TITLE>Thank You!</TITLE></HEAD>
<BODY>
<H1>Thank You!</H1>
Thank you again for filling out our survey. Here is the information
that you selected: 
<HR>
<P>

Thanks

    while (<FILE>) {
        print $_, "<BR>";
    }

    print "<HR>";
    print "</BODY></HTML>", "\n";

    close (FILE);

    unlink ($file);
}

sub escape
{
    local ($string) = @_;

    $string =~ s/(\W)/sprintf("%%%x", ord($1))/eg;

    return($string);
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

sub get_date_time 
{
    local ($months, $weekdays, $ampm, $time_string);

    $months = "January/Febraury/March/April/May/June/July/" . 
              "August/September/October/November/December";
    $weekdays = "Sunday/Monday/Tuesday/Wednesday/Thursday/Friday/Saturday";

    local ($sec, $min, $hour, $day, $nmonth, $year, $wday, $yday, $isdst) 
	          = localtime(time);

    if ($hour > 12) {
        $hour -= 12;
        $ampm = "pm";
    } else {
        $ampm = "am";
    }

    if ($hour == 0) {
	       $hour = 12;
    }

    $year += 1900;

    $week  = (split("/", $weekdays))[$wday];
    $month = (split("/", $months))[$nmonth];

    $time_string = sprintf("%s, %s %s, %s - %02d:%02d:%02d %s", 
                                $week, $month, $day, $year, 
                                $hour, $min, $sec, $ampm);

    return ($time_string);
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



