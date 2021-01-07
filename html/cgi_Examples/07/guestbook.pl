#!/usr/local/bin/perl

$webmaster = "shishir\@bu\.edu";

$method = $ENV{'REQUEST_METHOD'};
$script = $ENV{'SCRIPT_NAME'};
$query  = $ENV{'QUERY_STRING'};

$document_root = "/home/shishir/httpd_1.4.2/public";
$guest_file = "/guestbook.html";
$full_path = $document_root . $guest_file;

$exclusive_lock = 2;
$unlock = 8;

if ($method eq "GET") {
    if ($query eq "add") {
	$date_time = &get_date_time();

        &MIME_header ("text/html", "Shishir Gundavaram's Guestbook");

        print <<End_Of_Guestbook_Form;

This is a guestbook CGI script that allows people to leave some
information for others to see. Please enter all requested
information, <B>and</B> if you have a WWW server, enter the address
so a hypertext link can be created. 
<P>
The current time is: $date_time
<HR>

<FORM METHOD="POST">
<PRE>
<EM>Full Name</EM>:      <INPUT TYPE="text" NAME="name" SIZE=40>
<EM>Email Address</EM>:  <INPUT TYPE="text" NAME="from" SIZE=40>
<EM>WWW Server</EM>:     <INPUT TYPE="text" NAME="www"  SIZE=40>
</PRE>
<P>
<EM>Please enter the information that you'd like to add:</EM><BR>
<TEXTAREA ROWS=3 COLS=60 NAME="comments"></TEXTAREA><P>
<INPUT TYPE="submit" VALUE="Add to Guestbook">
<INPUT TYPE="reset"  VALUE="Clear Information"><BR>
<P>
</FORM>
<HR>

End_Of_Guestbook_Form

    } else {

	if ( open(GUESTBOOK, "<" . $full_path) ) {
                flock (GUESTBOOK, $exclusive_lock);

		&MIME_header ("text/html", "Here is my guestbook!");
                while (<GUESTBOOK>) {
                    print;
                }
                flock (GUESTBOOK, $unlock);
                close(GUESTBOOK);
		
	} else {
                &return_error (500, "Guestbook File Error",
			"Cannot read from the guestbook file [$full_path].");
        }
   }
} elsif ($method eq "POST") {
    
    if ( open (GUESTBOOK, ">>" . $full_path) ) {
        flock (GUESTBOOK, $exclusive_lock);

	$date_time = &get_date_time();
	&parse_form_data (*FORM);

        $FORM{'name'}  = "Anonymous User"       if !$FORM{'name'};
        $FORM{'from'}  = $ENV{'REMOTE_HOST'}    if !$FORM{'from'};
        
        $FORM{'comments'} =~ s/\n/<BR>/g;

        print GUESTBOOK <<End_Of_Write;

<P>
<B>$date_time:</B><BR>
Message from <EM>$FORM{'name'}</EM> at <EM>$FORM{'from'}</EM>:
<P>
$FORM{'comments'}

End_Of_Write


        if ($FORM{'www'}) {
            print GUESTBOOK <<End_of_Web_Address;
			
<P>
$FORM{'name'} can also be reached at: 
<A HREF="$FORM{'www'}">$FORM{'www'}</A>

End_of_Web_Address

        }
		
        print GUESTBOOK "<P><HR>";

        flock (GUESTBOOK, $unlock);
        close(GUESTBOOK);

        &MIME_header ("text/html", "Thank You!");
        
        print <<End_of_Thanks;
        
Thanks for visiting my guestbook. If you would like to see the guestbook,
click <A HREF="$guest_file">here</A> (actual guestbook HTML file), or <A HREF="$script">here</A> (guestbook script without a query).

End_of_Thanks

    } else {
        &return_error (500, "Guestbook File Error",
               "Cannot write to the guestbook file [$full_path].");
    }
		
} else {
    &return_error (500, "Server Error",
                        "Server uses unsupported method");
}

exit(0);

sub MIME_header
{
    local ($mime_type, $title_string, $header) = @_;

    if (!$header) {
        $header = $title_string;
    }

    print "Content-type: ", $mime_type, "\n\n";
    print "<HTML>", "\n";
    print "<HEAD><TITLE>", $title_string, "</TITLE></HEAD>", "\n";
    print "<BODY>", "\n";
    print "<H1>", $header, "</H1>";
    print "<HR>";
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

sub parse_form_data
{
    local (*FORM_DATA) = @_;
	
    local ( $request_method, $post_info, @key_value_pairs,
		          $key_value, $key, $value);

    read (STDIN, $post_info, $ENV{'CONTENT_LENGTH'});

    @key_value_pairs = split (/&/, $post_info);

    foreach $key_value (@key_value_pairs) {
        ($key, $value) = split (/=/, $key_value);
        $value =~ tr/+/ /;
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
