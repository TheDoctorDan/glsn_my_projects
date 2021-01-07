#!/usr/local/bin/pgperl

require "common.pl";
require "pgplot.pl";

$webmaster = "shishir\@bu\.edu";
$access_log = "/usr/local/bin/httpd_1.4.2/logs/access_log";

$hours = 23;
$maximum = 0;

$process_id = $$;
$output_gif = join ("", "/tmp/", $process_id, ".gif");
	
if ( (open(FILE, "<" . $access_log)) ) {
	for ($loop=0; $loop <= $hours; $loop++) {
 		   $time[$loop] = 0;
 		   $counter[$loop] = $loop;
	}
	
	while (<FILE>){ 
		if (m|\[\d+/\w+/\d+:([^:]+)|) {
			 $time[$1]++;
		}
   	}

   	close (FILE);

	&find_maximum();
   	&prepare_graph();
} else {
	&return_error (500, "Server Log File Error", "Cannot open NCSA server access log!");
}

exit(0);


sub find_maximum
{
    for ($loop=0; $loop <= $hours; $loop++) {
	if ($time[$loop] > $maximum) {
	    $maximum = $time[$loop];
	}
    }

    $maximum += 10;
}

sub prepare_graph
{	
	&pgbegin(0, "${output_gif}/VGIF", 1, 1);
	&pgscr(0, 1, 1, 1);
	&pgpap(4.0, 1.0);

	&pgscf(2);
	&pgslw(3);
	&pgsch(1.6);

	&pgsci(4);
	&pgenv(0, $hours + 1, 0, $maximum, 2, 0);

	&pgsci(2);
	&pgbin($hours, *counter, *time, 0);
	&pglabel("Time (Hours)", "No. of Requests", "WWW Server Usage");

	&pgend;
	
	&print_gif();
}

sub print_gif
{
	local ($content_length) = (stat($output_gif))[7];
	
	$| = 1;
	print "Content-type: image/gif", "\n";
	print "Content-length: ", $content_length, "\n\n";
	
	if ( (open (GIF, "<" . $output_gif)) ) {
		while (<GIF>) {
			print;
		}
		close (GIF);
	
		unlink $output_gif;
	} else {
		&return_error (500, "Server Log File Error", "Cannot read from the GIF file!");
	}
}
		








