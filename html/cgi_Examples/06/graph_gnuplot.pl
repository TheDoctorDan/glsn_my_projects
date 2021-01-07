#!/usr/local/bin/perl

require "common.pl";

$webmaster = "shishir\@bu\.edu";

$gnuplot = "/usr/local/bin/gnuplot";
$ppmtogif = "/usr/local/bin/pbmplus/ppmtogif";
$access_log = "/usr/local/bin/httpd_1.4.2/logs/access_log";

$process_id = $$;
$output_ppm = join ("", "/tmp/", $process_id, ".ppm");
$datafile = join ("", "/tmp/", $process_id, ".txt");

$x = 0.6;
$y = 0.6;
$color = 1; 

if ( open (FILE, "<" . $access_log) ) {
	for ($loop=0; $loop < 24; $loop++) {
	   $time[$loop] = 0;
	}

	while (<FILE>) {
		if (m|\[\d+/\w+/\d+:([^:]+)|) {
			$time[$1]++;
		}
	}

	close (FILE);
	&create_output_file();
} else {
	&return_error (500, "Server Log File Error", "Cannot open NCSA server access log!");
}

exit(0);

sub create_output_file 
{
	local ($loop);
	
	if ( (open (FILE, ">" . $datafile)) ) {
		for ($loop=0; $loop < 24; $loop++) {
			print FILE $loop, " ", $time[$loop], "\n";
		}
	
		close (FILE);
	
		&send_data_to_gnuplot();
	} else {
		&return_error (500, "Server Log File Error", "Cannot write to data file!");
	}
}

sub send_data_to_gnuplot
{
	open (GNUPLOT, "|$gnuplot");
	print GNUPLOT <<gnuplot_Commands_Done;
	
		set term pbm color small
		set output "$output_ppm"
		set size $x, $y
		set title "WWW Server Usage"
		set xlabel "Time (Hours)"
		set ylabel "No. of Requests" 
		set xrange [-1:24]
		set xtics 0, 2, 23
		set noxzeroaxis
		set noyzeroaxis
		set border
		set nogrid
		set nokey
		plot "$datafile" w boxes $color

gnuplot_Commands_Done

	close (GNUPLOT);
	
	&print_gif_file_and_cleanup();
}

sub print_gif_file_and_cleanup
{
	$| = 1;
	print "Content-type: image/gif", "\n\n";
	system ("$ppmtogif $output_ppm 2> /dev/null");

	unlink $output_ppm, $datafile;
}

