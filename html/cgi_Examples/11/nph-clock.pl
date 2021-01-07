#!/usr/local/bin/perl5

use GD;

$| = 1;

$font_length = 8;
$font_height = 16;

$boundary_string = "\n" . "--End" . "\n";
$end_of_data = "\n" . "--End--" . "\n";

$delay_time = 5;
$max_updates = 10;

print "HTTP/1.0 200 OK", "\n";

$browser = $ENV{'HTTP_USER_AGENT'};

if ($browser =~ m#^Mozilla/(1\.[^0]|[2-9])#) {
	print "Content-type: multipart/x-mixed-replace;boundary=End", "\n";
	print $boundary_string;
	
	for ($loop=0; $loop < $max_updates; $loop++) {
		&display_time ();
		print $boundary_string;
		sleep ($delay_time);
	}

	&display_time ("end");
	print $end_of_data;

} else {
	&display_time ("end");
}

exit(0);

sub display_time
{
	local ($status) = @_;
        local ($seconds, $minutes, $hour, $ampm, $time, $time_length,
	       $x, $y, $image, $black, $color);

	print "Content-type: image/gif", "\n\n";
	
	($seconds, $minutes, $hour) = localtime (time);

	if ($hour > 12) {
	        $hour -= 12;
	        $ampm = "pm";
	} else {
	        $ampm = "am";
	}

	if ($hour == 0) {
	        $hour = 12;
	}

	$time = sprintf ("%02d:%02d:%02d %s", $hour, $minutes, $seconds,$ampm);

	$time_length = length($time);
	$x = $font_length * $time_length;
	$y = $font_height;

	$image = new GD::Image ($x, $y);

	$black = $image->colorAllocate (0, 0, 0);

	if ($status eq "end") {
		$color = $image->colorAllocate (0, 0, 255);
		$image->transparent ($black);
	} else {
		$color = $image->colorAllocate (255, 0, 0);
	}

	$image->string (gdLargeFont, 0, 0, $time, $color);
	print $image->gif;
}
