#!/usr/local/bin/perl5

use GD;
use Sprite;

$webmaster = "Shishir Gundavaram (shishir\@bu\.edu)";

$cal = "/usr/bin/cal";
$database = "/home/shishir/calendar.db";
$delimiter = "::";

($current_month, $current_year) = (localtime(time))[4,5];
$current_month += 1;
$current_year += 1900;

$action_types = '^(add|delete|modify|search)$';
$delete_password = "CGI Super Source";

&check_database ();
&parse_query_and_form_data (*CALENDAR);

$action = $CALENDAR{'action'};
$month = $CALENDAR{'month'};
($temp_month, $temp_year) = split ("/", $month, 2);

if ( ($temp_month =~ /^\d{1,2}$/) && ($temp_year =~ /^\d{4}$/) ) {
	if ( ($temp_month >= 1) && ($temp_month <= 12) ) {
		$current_month = $temp_month;
		$current_year = $temp_year;
	}
}

@month_names =    ('January', 'Febraury', 'March', 'April', 'May', 'June',
                   'July', 'August', 'September', 'October', 'November', 
                   'December');

$weekday_names = "Sun,Mon,Tue,Wed,Thu,Fri,Sat";

$current_month_name = $month_names[$current_month - 1];
$current_month_year = join ("/", $current_month, $current_year);

if ($action eq "full") {
	&display_year_calendar ();

} elsif ($action eq "view") {
	$date = $CALENDAR{'date'};
	&display_all_appointments ($date);

} elsif ($action =~ /$action_types/) {
	$type = $CALENDAR{'type'};

	if ($type eq "form") {
		$dynamic_sub = "display_${action}_form";
		&$dynamic_sub ();

	} elsif ($type eq "execute") {
		$dynamic_sub = "${action}_appointment";
		&$dynamic_sub ();

	} else {
		&return_error (500, "Calendar Manager",
				    "An invalid query was passed!");
	}

} else {
	&display_month_calendar ();
}

exit (0);

#-------------------------------< End of Main >--------------------------------

sub check_database
{
        local ($exclusive_lock, $unlock, $header);

        $exclusive_lock = 2;
        $unlock = 8;

        if (! (-e $database) ) {
                if ( open (DATABASE, ">" . $database) ) {
                        flock (DATABASE, $exclusive_lock);

                        $header = join ($delimiter, "ID", "Month", "Day", 
					"Year", "Keywords", "Description");

                        print DATABASE $header, "\n";

                        flock (DATABASE, $unlock);
                        close (DATABASE);
                } else {
                        &return_error (500, "Calendar Manager",
                                "Cannot create new calendar database.");
                }
        }
}

sub Sprite_error
{
	&return_error (500, "Calendar Manager",
		  "Sprite Database Error. Check the server log file.");
}

sub open_database
{
	local (*INFO, $command, $rdb_query) = @_;
	local ($rdb, $status, $no_matches);

	$rdb = new Sprite ();
	$rdb->set_delimiter ("Read", $delimiter);
	$rdb->set_delimiter ("Write", $delimiter);

	if ($command eq "select") {
		@INFO = $rdb->sql ($rdb_query);
		$status = shift (@INFO);
		$no_matches = scalar (@INFO);
		$rdb->close ();

		if (!$status) {
			&Sprite_error ();
		} else {
			return ($no_matches);
		}
	} else {
		$rdb->sql ($rdb_query) || &Sprite_error ();
		$rdb->close ($database);
	}
}

sub print_header
{
	local ($title, $header) = @_;

	print "Content-type: text/html", "\n\n";
	print "<HTML>", "\n";
	print "<HEAD><TITLE>", $title, "</TITLE></HEAD>", "\n";
	print "<BODY>", "\n";
	
	$header = $title unless ($header);

	print "<H1>", $header, "</H1>", "\n";
	print "<HR>", "\n";
}

sub print_footer
{
	print "<HR>", "\n";
	print "<ADDRESS>", $webmaster, "</ADDRESS>", "\n";
	print "</BODY></HTML>", "\n";
}

sub print_location
{
	local ($location_URL);

	$location_URL = join ("", $ENV{'SCRIPT_NAME'},                 "?",
				  "browser=", $ENV{'HTTP_USER_AGENT'}, "&",
				  "month=", $current_month_year);

	print "Location: ", $location_URL, "\n\n";
}

#----------------------------< Full Year Calendar >----------------------------

sub display_year_calendar
{
	local (@full_year);

	@full_year = `$cal $current_year`;
	@full_year = @full_year[5..$#full_year-3];

	grep (s|(\w{3})|<B>$1</B>|g, @full_year);

	&print_header ("Calendar for $current_year");
	print "<PRE>", @full_year, "</PRE>", "\n";
	&print_footer ();
}

#-------------------------------< Search Action >------------------------------

sub display_search_form
{
	local ($search_URL);

	$search_URL = join ("", $ENV{'SCRIPT_NAME'}, "?", 
				  "action=search",   "&",
				  "type=execute",    "&", 
				  "month=", $current_month_year);

	&print_header ("Calendar Search");

	print <<End_of_Search_Form;

This form allows you to search the calendar database for certain
information. The Keywords and Description fields are searched for the
string you enter.
<P>
<FORM ACTION="$search_URL" METHOD="POST">
Enter the string you would like to search for: 
<P>
<INPUT TYPE="text" NAME="search_string" SIZE=40 MAXLENGTH=40>
<P>
Please enter the <B>numerical</B> month and the year in which to search.
Leaving these fields empty will default to the current month and year:
<P>
<PRE>
Month: <INPUT TYPE="text" NAME="search_month" SIZE=4 MAXLENGTH=4><BR>
Year:  <INPUT TYPE="text" NAME="search_year"  SIZE=4 MAXLENGTH=4>
</PRE>
<P>
<INPUT TYPE="submit" VALUE="Search the Calendar!">
<INPUT TYPE="reset"  VALUE="Clear the form">
</FORM>

End_of_Search_Form

	&print_footer ();
}

sub search_appointment
{
	local ($search_string, $search_month, $search_year, @RESULTS,
	       $matches, $loop, $day, $month, $year, $keywords, 
	       $description, $search_URL, $month_name);

	$search_string = $CALENDAR{'search_string'};
	$search_month = $CALENDAR{'search_month'};
	$search_year = $CALENDAR{'search_year'};

	if ( ($search_month < 1) || ($search_month > 12) ) {
		$CALENDAR{'search_month'} = $search_month = $current_month;
	}

	if ($search_year !~ /^\d{2,4}$/) {
		$CALENDAR{'search_year'} = $search_year = $current_year;

	} elsif (length ($search_year) < 4) {
		$CALENDAR{'search_year'} = $search_year += 1900;
	}

	$search_string =~ s/(\W)/\\$1/g;

	$matches = &open_database (*RESULTS, "select", <<End_of_Select);

select Day, Month, Year, Keywords, Description from $database
where ( (Keywords    =~  /$search_string/i)      or
        (Description =~  /$search_string/i) )   and
        (Month       =   $search_month)         and
        (Year        =   $search_year)

End_of_Select

	unless ($matches) {
		&return_error (500, "Calendar Manager",
 		     "No appointments containing $search_string are found.");
	}

	&print_header ("Search Results for: $search_string");

	for ($loop=0; $loop < $matches; $loop++) {
	        $RESULTS[$loop] =~ s/([^\w\s\0])/sprintf ("&#%d;", ord ($1))/ge;

		($day, $month, $year, $keywords, $description) =
			split (/\0/, $RESULTS[$loop], 5);

		$search_URL = join ("", $ENV{'SCRIPT_NAME'}, "?", 
					"action=view",       "&",
					"date=", $day,       "&",
					"month=", $month, "/", $year);

		$keywords = "No Keywords Specified!"   unless ($keywords);
		$description = "-- No Description --"  unless ($description);

		$description =~ s/&#60;BR&#62;/<BR>/g;
		$month_name = $month_names[$month - 1];

		print <<End_of_Appointment;

<A HREF="$search_URL">$month_name $day, $year</A><BR>
<B>$keywords</B><BR>
$description

End_of_Appointment

		print "<HR>" if ($loop < $matches - 1);

	}

	&print_footer ();
}

#---------------------------------< Add Action >-------------------------------

sub display_add_form
{
	local ($add_URL, $date, $message);

	$date = $CALENDAR{'date'};
	$message = join ("", "Adding Appointment for ",
			 $current_month_name, " ", $date, ", ", $current_year);

	$add_URL = join ("", $ENV{'SCRIPT_NAME'},           "?",
			     "action=add",                  "&",
			     "type=execute",                "&", 
			     "month=", $current_month_year, "&",
			     "date=", $date);

	&print_header ("Add Appointment", $message);
		      
	print <<End_of_Add_Form;

This form allows you to enter an appointment to be stored in the calendar
database. To make it easier for you to search for specific appointments
later on, please use descriptive words to describe an appointment.
<P>
<FORM ACTION="$add_URL" METHOD="POST">
Enter a brief message (keywords) describing the appointment:
<P>
<INPUT TYPE="text" NAME="add_keywords" SIZE=40 MAXLENGTH=40>
<P>
Enter some comments about the appointment:
<TEXTAREA ROWS=4 COLS=60 NAME="add_description"></TEXTAREA><P>
<P>
<INPUT TYPE="submit" VALUE="Add Appointment!">
<INPUT TYPE="reset"  VALUE="Clear Form">
</FORM>

End_of_Add_Form

	&print_footer();
}

sub add_appointment
{
	local ($time, $date, $keywords, $description);

	$time = time;
	$date = $CALENDAR{'date'};
	($keywords = $CALENDAR{'add_keywords'}) =~ s/(['"])/\\$1/g;
	($description = $CALENDAR{'add_description'}) =~ s/\n/<BR>/g;
        $description =~ s/(['"])/\\$1/g;

	&open_database (undef, "insert", <<End_of_Insert);

insert into $database
   (ID, Day, Month, Year, Keywords, Description)
values
   ($time, $date, $current_month, $current_year, '$keywords', '$description')

End_of_Insert

	&print_location ();
}

#-------------------------------< Delete Action >------------------------------

sub display_delete_form
{
	local ($delete_URL, $id);

	$id = $CALENDAR{'id'};
	$delete_URL = join ("", $ENV{'SCRIPT_NAME'}, "?",
			        "action=delete",     "&",
			        "type=execute",      "&",
			        "id=", $id,          "&",
				"month=", $current_month_year);

	&print_header ("Deleting appointment");

	print <<End_of_Delete_Form;

In order to delete calendar entries, you need to enter a valid
identification code (or password):
<HR>
<FORM ACTION="$delete_URL" METHOD="POST">
<INPUT TYPE="password" NAME="code" SIZE=40>
<P>
<INPUT TYPE="submit" VALUE="Delete Entry!">
<INPUT TYPE="reset"  VALUE="Clear the form">
</FORM>

End_of_Delete_Form

	&print_footer ();
}

sub delete_appointment
{
	local ($password, $id);

	$password = $CALENDAR{'code'};
	$id = $CALENDAR{'id'};

	if ($password ne $delete_password) {
		&return_error (500, "Calendar Manager",
			"The password you entered is not valid!");
	} else {
		&open_database (undef, "delete", <<End_of_Delete);

delete from $database
where (ID = $id)

End_of_Delete
	}

	&print_location ();
}

#---------------------------------< Modify Form >------------------------------

sub display_modify_form
{
	local ($id, $matches, @RESULTS, $keywords, $description, $modify_URL);

	$id = $CALENDAR{'id'};

	$matches = &open_database (*RESULTS, "select", <<End_of_Select);

select Keywords, Description from $database
where (ID = $id)

End_of_Select

	unless ($matches) {
		&return_error (500, "Calendar Manager",
		  "Oops! The appointment that you selected no longer exists!");
	}

	($keywords, $description) = split (/\0/, shift (@RESULTS), 2);
	$keywords = &escape_html ($keywords);
	$description =~ s/<BR>/\n/g;

	$modify_URL = join ("", $ENV{'SCRIPT_NAME'}, "?",
				"action=modify",     "&",
			        "type=execute",      "&", 
				"id=", $id,          "&",
				"month=", $current_month_year);

	&print_header ("Modify Form");

	print <<End_of_Modify_Form;

This form allows you to modify the <B>description</B> field for an
existing appointment in the calendar database.
<P>
<FORM ACTION="$modify_URL" METHOD="POST">
Enter a brief message (keywords) describing the appointment:
<P>
<INPUT TYPE="text" NAME="modify_keywords" SIZE=40 
       VALUE="$keywords" MAXLENGTH=40>
<P>
Enter some comments about the appointment:
<TEXTAREA ROWS=4 COLS=60 NAME="modify_description">
$description
</TEXTAREA><P>
<P>
<INPUT TYPE="submit" VALUE="Modify Appointment!">
<INPUT TYPE="reset"  VALUE="Clear Form">
</FORM>

End_of_Modify_Form

	&print_footer ();
}

sub escape_html
{
	local ($string) = @_;
	local (%html_chars, $html_string);

        %html_chars = ('&', '&amp;',
                       '>', '&gt;',
                       '<', '&lt;',
                       '"', '&quot;');

	$html_string = join ("", keys %html_chars);

	$string =~ s/([$html_string])/$html_chars{$1}/go;

	return ($string);
}

sub modify_appointment
{
	local ($modify_description, $id);

	($modify_description = $CALENDAR{'modify_description'}) 
	    =~ s/(['"])/\\$1/g;

	$id = $CALENDAR{'id'};

	&open_database (undef, "update", <<End_of_Update);

update $database
set Description = ('$modify_description')
where (ID = $id)

End_of_Update

	&print_location ();
}

#------------------------< Display Appropriate Calendar >----------------------

sub display_month_calendar
{
	local ($nongraphic_browsers, $client_browser, $clicked_point,
	       $draw_imagemap, $image_date);

	$nongraphic_browsers = 'Lynx|CERN-LineMode';
	$client_browser = $ENV{'HTTP_USER_AGENT'} || $CALENDAR{'browser'};

	if ($client_browser =~ /$nongraphic_browsers/) {
		&draw_text_calendar ();
	} else {
		$clicked_point = $CALENDAR{'clicked_point'};
		$draw_imagemap = $CALENDAR{'draw_imagemap'};

		if ($clicked_point) {
			$image_date = &get_imagemap_date();
			&display_all_appointments ($image_date);	

		} elsif ($draw_imagemap) {
			&draw_graphic_calendar ();

		} else {
			&output_HTML ();
		}
	}
}

sub get_next_and_previous
{
	local ($next_month, $next_year, $previous_month, $previous_year,
	       $arrow_URL, $next_month_year, $previous_month_year);

	$next_month = $current_month + 1;
	$previous_month = $current_month - 1;

	if ($next_month > 12) {
		$next_month = 1;
		$next_year = $current_year + 1;
	} else {
		$next_year = $current_year;
	}

	if ($previous_month < 1) {
		$previous_month = 12;
		$previous_year = $current_year - 1;
	} else {
		$previous_year = $current_year;
	}

	$arrow_URL = join ("", $ENV{'SCRIPT_NAME'}, "?",
			       "action=change",     "&",
			       "month=");


	$next_month_year = join ("", $arrow_URL, $next_month, "/", $next_year);
	$previous_month_year = join ("", $arrow_URL,
					 $previous_month, "/", $previous_year);

	return ($next_month_year, $previous_month_year);
}

sub output_HTML
{
	local ($script, $arrow_URL, $next, $previous, $left, $right);

	$script = $ENV{'SCRIPT_NAME'};

	($next, $previous) = &get_next_and_previous ();

	$left  = qq|<A HREF="$previous"><IMG SRC="/icons/left.gif"></A>|;
	$right = qq|<A HREF="$next"><IMG SRC="/icons/right.gif"></A>|;

	&print_header 
	    ("Calendar for $current_month_name $current_year",
	     "$left Calendar for $current_month_name $current_year $right");

	print <<End_of_HTML;

<A HREF="$script/$current_month_year">
<IMG SRC="$script?month=$current_month_year&draw_imagemap" ISMAP></A>
<HR>
<A HREF="$script?action=full&year=$current_year">Full Year Calendar</A>
<BR>
<A HREF="$script?action=search&type=form&month=$current_month_year">Search</A>

End_of_HTML

	&print_footer ();
}

sub draw_text_calendar
{
	local (@calendar, $big_line, $matches, @RESULTS, $header, $first_line,
               $no_spaces, $spaces, $loop, $date, @status, $script, $date_URL,
	       $next, $previous);

	@calendar = `$cal $current_month $current_year`;
	shift (@calendar);
	$big_line = join ("", @calendar);
  
	$matches = &open_database (*RESULTS, "select", <<End_of_Select);

select Day from $database
where (Month = $current_month) and
      (Year  = $current_year)

End_of_Select

	&print_header ("Calendar for $current_month_name $current_year");

	$big_line =~ s/\b(\w{1,2})\b/$1    /g;
	$big_line =~ s/\n/\n\n/g;
	($header) = $big_line =~ /( S.*)/;
	$big_line =~ s/ *(1.*)/$1/;
	($first_line) = $big_line =~ //;

	$no_spaces = length ($header) - length ($first_line);
	$spaces = " " x $no_spaces; 
	$big_line =~ s/\b1\b/${spaces}1/;
 
	for ($loop=0; $loop < $matches; $loop++) {
		$date = $RESULTS[$loop];

	        unless ($status[$date]) {
		        $big_line =~ s|\b$date\b {0,1}|$date\*|g;
			$status[$date] = 1;
		}
	}

	$script = $ENV{'SCRIPT_NAME'};

	$date_URL = join ("", $script,       "?", 
			      "action=view", "&",
			      "month=", $current_month_year);

	$big_line =~ s|\b(\d{1,2})\b|<A HREF="$date_URL&date=$1">$1</A>|g;
 
	($next, $previous) = &get_next_and_previous ();

	print <<End_of_Output;
<UL>
<LI><A HREF="$previous">Previous Month!</A></LI>
<LI><A HREF="$next">Next Month!</A></LI>
</UL>
<PRE>
$big_line
</PRE>
<HR>
<A HREF="$script?action=full&year=$current_year">Full Year Calendar</A>
<BR>
<A HREF="$script?action=search&type=form&month=$current_month_year">Search</A>

End_of_Output

	&print_footer ();
}

sub display_all_appointments
{
	local ($date) = @_;
	local ($script, $matches, @RESULTS, $loop, $id, $keywords, 
	       $description, $display_URL);

	$matches = &open_database (*RESULTS, "select", <<End_of_Select);

select ID, Keywords, Description from $database
where (Month = $current_month) and
      (Year  = $current_year)  and
      (Day   = $date)          

End_of_Select

	&print_header ("Appointments",
	      "Appointments for $current_month_name $date, $current_year");

	$display_URL = join ("", $ENV{'SCRIPT_NAME'}, "?",
				 "type=form",         "&",
				 "month=", $current_month_year);

	if ($matches) {
		for ($loop=0; $loop < $matches; $loop++) {
		        $RESULTS[$loop] =~ s/([^\w\s\0])/sprintf ("&#%d;", ord ($1))/ge;

			($id, $keywords, $description) =
				split (/\0/, $RESULTS[$loop], 3);

			$description =~ s/&#60;BR&#62;/<BR>/g;

			print <<End_of_Each_Appointment;

Keywords: <B>$keywords</B>
<BR>
Description: 
$description
<P>
<A HREF="$display_URL&action=modify&id=$id">Modify!</A>
<A HREF="$display_URL&action=delete&id=$id">Delete!</A>

End_of_Each_Appointment

			print "<HR>", "\n"	if ($loop < $matches - 1);

		}
	} else {
		print "There are no appointments scheduled!", "\n";
	}


	print <<End_of_Footer;

<HR>
<A HREF="$display_URL&action=add&date=$date">Add New Appointment!</A>

End_of_Footer

	&print_footer ();
}

sub graphics_calculations
{
	local (*GIF) = @_;

	$GIF{'first_day'} = &get_first_day ($current_month, $current_year);
	$GIF{'last_day'}  = &get_last_day ($current_month, $current_year);

	$GIF{'no_rows'} = ($GIF{'first_day'} + $GIF{'last_day'}) / 7;

	if ($GIF{'no_rows'} != int ($GIF{'no_rows'})) {
		$GIF{'no_rows'} = int ($GIF{'no_rows'} + 1);
	}

	$GIF{'box_length'} = $GIF{'box_height'} = 100;
	$GIF{'x_offset'}   = $GIF{'y_offset'} = 10;

	$GIF{'large_font_length'} = 8;
	$GIF{'large_font_height'} = 16;

	$GIF{'small_font_length'} = 6;
	$GIF{'small_font_height'} = 12;

	$GIF{'x'} = ($GIF{'box_length'} * 7)   +
		    ($GIF{'x_offset'} * 2)     +
		    $GIF{'large_font_length'};

	$GIF{'y'} = ($GIF{'large_font_height'} * 2)         +
		    ($GIF{'no_rows'} * $GIF{'box_height'})  +
		    ($GIF{'no_rows'} + 1)                   + 
		    ($GIF{'y_offset'} * 2)                  +
		    $GIF{'large_font_height'};

	$GIF{'start_calendar'} = $GIF{'y_offset'}                +
	                         (3 * $GIF{'large_font_height'});

	$GIF{'date_x_offset'} = int ($GIF{'box_length'} * 0.80);
	$GIF{'date_y_offset'} = int ($GIF{'box_height'} * 0.05);

	$GIF{'appt_x_offset'} = $GIF{'appt_y_offset'} = 10;

	$GIF{'no_chars'} = int (($GIF{'box_length'}     -
				 $GIF{'appt_x_offset'}) /
				 $GIF{'small_font_length'}) - 1;

	$GIF{'no_appts'} = int (($GIF{'box_height'}           - 
				  $GIF{'large_font_height'}   - 
				  $GIF{'date_y_offset'}       - 
				  $GIF{'appt_y_offset'})      /
				  $GIF{'small_font_height'});
}

sub get_imagemap_date
{
	local (%DATA, $x_click, $y_click, $error_offset, $error, 
	       $start_y, $end_y, $start_x, $end_x, $horizontal, $vertical,
	       $box_number, $clicked_date);

	&graphics_calculations (*DATA);

	($x_click, $y_click) = split(/,/, $CALENDAR{'clicked_point'}, 2);

	$error_offset = 2;
	$error = $error_offset / 2;

	$start_y = $DATA{'start_calendar'} + $error_offset;
	$end_y = $DATA{'y'} - $DATA{'y_offset'} + $error_offset;

	$start_x = $DATA{'x_offset'} + $error_offset;
	$end_x = $DATA{'x'} - $DATA{'x_offset'} + $error_offset;

	if ( ($x_click >= $start_x) && ($x_click <= $end_x) &&
	     ($y_click >= $start_y) && ($y_click <= $end_y) ) {

		$horizontal = int (($x_click - $start_x) /
				  ($DATA{'box_length'} + $error));

		$vertical = int (($y_click - $start_y) / 
				($DATA{'box_height'} + $error));

		$box_number = ($vertical * 7) + $horizontal;
		$clicked_date = ($box_number - $DATA{'first_day'}) + 1;

		if (($clicked_date <= 0) ||
		    ($clicked_date > $DATA{'last_day'})) {

			&return_error (204, "No Response", 
					    "Browser doesn't support 204");
		} else {
			return ($clicked_date);
		}

	} else {
		&return_error (204, "No Response", 
				    "Browser doesn't support 204");
	}
}

sub draw_graphic_calendar
{
	local (%DATA, $image, $black, $cadet_blue, $red, $yellow, 
	       $month_title, $month_point, $day_point, $loop, $temp_day, 
	       $temp_x, $temp_y, $inner, $counter, $matches, %APPTS,
	       @appt_list);

	&graphics_calculations (*DATA);

	$image = new GD::Image ($DATA{'x'}, $DATA{'y'});

	$black      = $image->colorAllocate (0, 0, 0);
	$cadet_blue = $image->colorAllocate (95, 158, 160);
	$red        = $image->colorAllocate (255, 0, 0);
	$yellow     = $image->colorAllocate (255, 255, 0);

	$month_title = join (" ", $current_month_name, $current_year);

	$month_point = ($DATA{'x'} - 
		       (length ($month_title) * 
		       $DATA{'large_font_length'})) / 2;

	$image->string (gdLargeFont, $month_point, $DATA{'y_offset'},
			$month_title, $red);

	$day_point = (($DATA{'box_length'} + 2) -
		      ($DATA{'large_font_length'} * 3)) / 2;

	# Draw Weekday Names
	
	for ($loop=0; $loop < 7; $loop++) {
		$temp_day = (split(/,/, $weekday_names))[$loop];

		$temp_x = ($loop * $DATA{'box_length'}) +
		           $DATA{'x_offset'} +
			   $day_point + $loop; 
	
		$image->string ( gdLargeFont, 
				 $temp_x,
				 $DATA{'y_offset'} +
				 $DATA{'large_font_height'} + 10,
				 $temp_day,
				 $red );
	}

	# Draw Horizontal Lines

	for ($loop=0; $loop <= $DATA{'no_rows'}; $loop++) {
		$temp_y = $DATA{'start_calendar'} +
			  ($loop * $DATA{'box_height'}) + $loop;

		$image->line ( $DATA{'x_offset'},
			       $temp_y,
			       $DATA{'x'} - $DATA{'x_offset'} - 1,
			       $temp_y,
			       $yellow );
	}

	# Draw Vertical Lines

	for ($loop=0; $loop <= 7; $loop++) {
		$temp_x = $DATA{'x_offset'} +
			  ($loop * $DATA{'box_length'}) + $loop;

		$image->line ( $temp_x,
			       $DATA{'start_calendar'},
			       $temp_x,
			       $DATA{'y'} - $DATA{'y_offset'} - 1,
			       $yellow );
	}


	# Draw Actual Calendar Days

	$inner = $DATA{'first_day'};
	$counter = 1;

	$matches = &appointments_for_graphic (*APPTS);

	for ($outer=0; $outer <= $DATA{'no_rows'}; $outer++) {
		$temp_y = $DATA{'start_calendar'} + $outer + 
			  ($outer * $DATA{'box_height'}) + 
			  $DATA{'date_y_offset'};

		while (($inner < 7) && ($counter <= $DATA{'last_day'})) {
			$temp_x = $DATA{'x_offset'} +
				  ($inner * $DATA{'box_length'}) +
			          $inner + $DATA{'date_x_offset'};

			$image->string (gdLargeFont, $temp_x, $temp_y, 
					sprintf ("%2d", $counter), 
					$cadet_blue);

			if ($APPTS{$counter}) {
				@appt_list = split (/\0/, $APPTS{$counter});

				for ($loop=0; $loop < $matches; $loop++) {
					last if ($loop >= $DATA{'no_appts'});

					$image->string (gdSmallFont, 
						$DATA{'x_offset'} +
					        ($inner * $DATA{'box_length'} +
					        $inner + 
					        $DATA{'appt_x_offset'}),

					        $temp_y +
						$DATA{'large_font_height'}+
					        ($loop *
						$DATA{'small_font_height'}) +
						$DATA{'appt_y_offset'},

					        pack ("A$DATA{'no_chars'}",
						$appt_list[$loop]),
						$red);
				}
			}

			$inner++;
			$counter++;
		}

	        $inner = 0;
	}

	$| = 1;
	print "Content-type: image/gif", "\n";
	print "Pragma: no-cache", "\n\n";
	print $image->gif;
}

sub appointments_for_graphic
{
	local (*DATES) = @_;
	local ($matches, @RESULTS, $loop, $day, $keywords);

	$matches = &open_database (*RESULTS, "select", <<End_of_Select);

select Day, Keywords from $database
where (Month = $current_month) and
      (Year  = $current_year)

End_of_Select

	for ($loop=0; $loop < $matches; $loop++) {
		($day, $keywords) = split (/\0/, $RESULTS[$loop], 2);

		if ($DATES{$day}) {
			$DATES{$day} = join ("\0", $DATES{$day}, $keywords);
		} else {
			$DATES{$day} = $keywords;
		}
	}

	return ($matches);
}

sub return_error
{
	local ($status, $keyword, $message) = @_;
	
	print "Content-type: text/html", "\n";
	print "Status: ", $status, " ", $keyword, "\n\n";
	
	print <<End_of_Error;

<title>Unexpected Error</title>
<h1>$keyword</h1>
<hr>$message</hr>
Please contact $webmaster for more information.

End_of_Error

	exit (1);
}

sub parse_query_and_form_data
{
	local (*FORM_DATA) = @_;
	
	local ($request_method, $query_string, $path_info, 
	       @key_value_pairs, $key_value, $key, $value);
	      
	$request_method = $ENV{'REQUEST_METHOD'};
        $path_info = $ENV{'PATH_INFO'};

	if ($request_method eq "GET") {
		$query_string = $ENV{'QUERY_STRING'};
	} elsif ($request_method eq "POST") {
		read (STDIN, $query_string, $ENV{'CONTENT_LENGTH'});
	
		if ($ENV{'QUERY_STRING'}) {
			$query_string = join ("&", $query_string,
						   $ENV{'QUERY_STRING'});
		}
	} else {
		&return_error ("500", "Server Error",
				      "Server uses unsupported method");
	}

	if ($query_string =~ /^\d+,\d+$/) {
		$FORM_DATA{'clicked_point'} = $query_string;

		if ($path_info =~ m|^/(\d+/\d+)$|) {
		    $FORM_DATA{'month'} = $1;
		} 
	} else {
		if ($query_string =~ /draw_imagemap/) {
		    $FORM_DATA{'draw_imagemap'} = 1;
		}

		@key_value_pairs = split (/&/, $query_string);
		
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
}

sub get_last_day
{
	local ($month, $year) = @_;
	local ($last, @no_of_days);

	@no_of_days = (31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);

	if ($month == 2) {
		if ( !($year % 4) && ( ($year % 100) || !($year % 400) ) ) {
			$last = 29;
		} else {
			$last = 28;
		}
	} else {
		$last = $no_of_days[$month - 1];
	}
		
	return ($last);
}

sub get_first_day
{
	# Algorithm by Malcolm Beattie <mbeattie@black.ox.ac.uk>

	local ($month, $year) = @_;
	local ($day, $first, @day_constants);

	$day = 1;

	@day_constants = (0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4);

	if ($month < 3) {
		$year--;
	}

	$first = ($year + int ($year / 4) - int ($year / 100) +
		 int ($year/400) + $day_constants [$month - 1] + $day) % 7;

	return ($first);
}

