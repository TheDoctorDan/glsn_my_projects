#!/usr/local/bin/perl

require "common.pl";

$webmaster = "shishir\@bu\.edu";
$document_root = "/home/shishir/httpd_1.4.2/public";
$ice_cream_file = "/ice_cream.dat";
$full_path = $document_root . $ice_cream_file;

$exclusive_lock = 2;
$unlock = 8;

&parse_form_data(*poll);
$user_selection = $poll{'ice_cream'};

if ( open (POLL, "<" . $full_path) ) {
	flock (POLL, $exclusive_lock);

	for ($loop=0; $loop < 3; $loop++) {
		$line[$loop] = <POLL>;
		$line[$loop] =~ s/\n$//;
	}

        @options = split ("::", $line[0]);
        @data    = split ("::", $line[1]);
        $colors  = $line[2];

        flock (POLL, $unlock);
        close (POLL);

        $item_no = 3;
        for ($loop=0; $loop <= $#options; $loop++) {
            if ($options[$loop] eq $user_selection) {
                $item_no = $loop;
                last;
            }
        }

        $data[$item_no]++;


        if ( open (POLL, ">" . $full_path) ) {
		flock (POLL, $exclusive_lock);

		print POLL join ("::", @options), "\n";
		print POLL join ("::", @data), "\n";
		print POLL $colors, "\n";

		flock (POLL, $unlock);
		close (POLL);

		print "Content-type: text/html", "\n\n";
        
		print <<End_of_Thanks;
<HTML>
<HEAD><TITLE>Thank You!</TITLE></HEAD>
<BODY>
<H1>Thank You!</H1>
<HR>        
Thanks for participating in the Ice Cream survey. If you would like to see the
current results, click <A HREF="/cgi-bin/pie.pl${ice_cream_file}">here</A>.
</BODY></HTML>

End_of_Thanks

        } else {
            &return_error (500, "Ice Cream Poll File Error",
			  "Cannot write to the poll data file [$full_path].");
        }

} else {
        &return_error (500, "Ice Cream Poll File Error",
		    "Cannot read from the poll data file [$full_path].");
}

exit (0);



