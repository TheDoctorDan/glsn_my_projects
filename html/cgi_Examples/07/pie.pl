#!/home/shishir/perl5.001m/perl5

require "common.pl";

use GD;

$webmaster = "shishir\@bu\.edu";
$document_root = "/home/shishir/httpd_1.4.2/public";
&read_data_file (*slices, *slices_color, *slices_message);
$no_slices = &remove_empty_slices();

if ($no_slices == -1) {
	&no_data ();
} else {
    $nongraphic_browsers = 'Lynx|CERN-LineMode';
	$client_browser = $ENV{'HTTP_USER_AGENT'};

	if ($client_browser =~ /$nongraphic_browsers/) {
	    &text_results ();
	} else {
	    &draw_pie ();
	}
}

exit(0);


sub no_data
{
	print "Content-type: text/html", "\n\n";
	
	print <<End_of_Message;
<HTML>
<HEAD><TITLE>Results</TITLE></HEAD>
<BODY>
<H1>No Results Available</H1>
<HR>
Sorry, no one has participated in this survey up to this point.
As a result, there is no data avilable. Try back later.
<HR>
</BODY></HTML>
End_of_Message
}

sub draw_pie 
{
	local ( $legend_rect_size, $legend_rect, $max_length, $max_height,
		$pie_indent, $pie_length, $pie_height, $radius, @origin,
		$legend_indent, $legend_rect_to_text, $deg_to_rad, $image,
		$white, $black, $red, $yellow, $green, $blue, $orange,
		$percent, $loop, $degrees, $x, $y, $legend_x, $legend_y,
		$legend_rect_y, $text, $message);

	$legend_rect_size = 10;
	$legend_rect = $legend_rect_size * 2;

	$max_length = 450;

	if ($no_slices > 8) {
	    $max_height = 200 + ( ($no_slices - 8) * $legend_rect );
	} else {
	    $max_height = 200;
	}

	$pie_indent = 10;
	$pie_length = $pie_height = 200;
	$radius = $pie_height / 2;

	@origin = ($radius + $pie_indent, $max_height / 2);
	$legend_indent = $pie_length + 40;
	$legend_rect_to_text = 25;
	$deg_to_rad = (atan2 (1, 1) * 4) / 180;

	$image = new GD::Image ($max_length, $max_height);

	$white = $image->colorAllocate (255, 255, 255);
	$black = $image->colorAllocate(0, 0, 0);
	$red = $image->colorAllocate (255, 0, 0);
	$yellow = $image->colorAllocate (255, 255, 0);
	$green = $image->colorAllocate(0, 255, 0);
	$blue = $image->colorAllocate(0, 0, 255);
	$orange = $image->colorAllocate(255, 165, 0);

	grep ($_ = eval("\$$_"), @slices_color);

	$image->arc (@origin, $pie_length, $pie_height, 0, 360, $black);

	$percent = 0;
	for ($loop=0; $loop <= $no_slices; $loop++) {
	    $percent += $slices[$loop];
	    $degrees = int ($percent * 360) * $deg_to_rad;

	    $image->line (  $origin[0],
		            $origin[1],
			    $origin[0] + ($radius * cos ($degrees)),
	                    $origin[1] + ($radius * sin ($degrees)),
	                    $slices_color[$loop] );
	}

	$percent = 0;
	for ($loop=0; $loop <= $no_slices; $loop++) {
	    $percent += $slices[$loop];
	    $degrees = int (($percent * 360) - 1) * $deg_to_rad;
	
	    $x = $origin[0] + ( ($radius - 10) * cos ($degrees) );
	    $y = $origin[1] + ( ($radius - 10) * sin ($degrees) );
	
	    $image->fill ($x, $y, $slices_color[$loop]);
	}

	$legend_x = $legend_indent;
	$legend_y = ( $max_height - ($no_slices * $legend_rect) - 
		    ($legend_rect * 0.75) ) / 2;

	for ($loop=0; $loop <= $no_slices; $loop++) {
	    $legend_rect_y = $legend_y + ($loop * $legend_rect);
	    $text = pack ("A18", $slices_message[$loop]);
	
	    $message = sprintf ("%s (%4.2f%%)", $text, $slices[$loop] * 100);
	
	    $image->filledRectangle (   $legend_x, 
		                        $legend_rect_y,
			                $legend_x + $legend_rect_size,
				        $legend_rect_y + $legend_rect_size, 
	                                $slices_color[$loop] );

	    $image->string ( gdSmallFont,
		             $legend_x + $legend_rect_to_text,
	                     $legend_rect_y,
		             $message,
			     $black );
	}	
	
	$image->transparent($white);

	$| = 1;
	print "Content-type: image/gif", "\n\n";
	print $image->gif;
}

sub text_results
{
	local ($text, $message, $loop);

	print "Content-type: text/html", "\n\n";
	
	print <<End_of_Results;
<HTML>
<HEAD><TITLE>Results</TITLE></HEAD>
<BODY>
<H1>Results</H1>
<HR>
<PRE>
End_of_Results

	for ($loop=0; $loop <= $no_slices; $loop++) {
		$text = pack ("A18", $slices_message[$loop]);
		$message = sprintf ("%s (%4.2f%%)", $text, 
						    $slices[$loop] * 100);

		print $message, "\n";
	}

	print "</PRE><HR>", "\n";
	print "</BODY></HTML>", "\n";
}

sub read_data_file
{
    local (*slices, *slices_color, *slices_message) = @_;
    local (@line, $total_votes, $poll_file, $loop, $exclusive_lock, $unlock);
    
    $exclusive_lock = 2;
    $unlock = 8;

    if ($ENV{'PATH_INFO'}) {
        $poll_file = $document_root . $ENV{'PATH_INFO'};
    } else {
        &return_error (500, "Poll Data File Error",
                 "A poll data file has to be specified.");
    }

    if ( open (POLL, "<" . $poll_file) ) {
        flock (POLL, $exclusive_lock);

	for ($loop=0; $loop < 3; $loop++) {
                $line[$loop] = <POLL>;
                $line[$loop] =~ s/\n$//;
        }

        @slices_message = split ("::", $line[0]);
        @slices         = split ("::", $line[1]);
        @slices_color   = split ("::", $line[2]);
       
        flock (POLL, $unlock);
        close (POLL);

        $total_votes = 0;
        for ($loop=0; $loop <= $#slices; $loop++) {
            $total_votes += $slices[$loop];
        }

	if ($total_votes > 0) {
	        grep ($_ = ($_ / $total_votes), @slices);
	}

    } else {
        &return_error (500, "Poll Data File Error",
                     "Cannot read from the poll data file [$poll_file].");
    }
}

sub remove_empty_slices
{
    local ($loop) = 0;

    while (defined ($slices[$loop])) {
        if ($slices[$loop] <= 0.0) {
            splice(@slices, $loop, 1);
            splice(@slices_color, $loop, 1);
            splice(@slices_message, $loop, 1);
        } else {
            $loop++;
        }
    }

    return ($#slices);
}
