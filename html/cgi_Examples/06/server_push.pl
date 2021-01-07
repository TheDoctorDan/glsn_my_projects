#!/usr/local/bin/perl

require "common.pl";

$| = 1;
$webmaster = "shishir\@bu\.edu";

$boundary_string = "\n" . "--End" . "\n";
$end_of_data = "\n" . "--End--" . "\n";

$delay_time = 1;

@image_list = ( "image_1.gif", 
		"image_2.gif",
		"image_3.gif",
		"image_4.gif",
		"image_5.gif");
	
$browser = $ENV{'HTTP_USER_AGENT'};

if ($browser =~ m#^Mozilla/(1\.[^0]|[2-9])#) {
	print "Content-type: multipart/x-mixed-replace;boundary=End", "\n";
	
	for ($loop=0; $loop < scalar (@image_list); $loop++) {
		print $boundary_string;
		&open_and_display_GIF ($image_list[$loop]);
		sleep ($delay_time);
	}
    
	print $end_of_data;
} else {
	&open_and_display_GIF ($image_list[0]);
}

exit(0);


sub open_and_display_GIF
{
    local ($file) = @_;
    local ($content_length) = (stat($file))[7];
	
	print "Content-type: image/gif", "\n";
	print "Content-length: ", $content_length, "\n\n";
	
	if (open (FILE, "<" . $file)) {
		while (<FILE>) {
			print;
		}

		close (FILE);
	} else {
	    &return_error (500, "File Access Error", "Cannot open graphic file $file");
	}
}
