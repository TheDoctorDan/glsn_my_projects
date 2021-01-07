#!/usr/local/bin/oraperl

require "common.pl";
require "oraperl.ph";

$| = 1;
$webmaster = "shishir\@bu\.edu";

$gnuplot = "/usr/local/bin/gnuplot";
$ppmtogif = "/usr/local/bin/pbmplus/ppmtogif";

&parse_form_data (*DB);
($company_id = $DB{'Company_ID'}) =~ s/^\s*(.*)\b\s*$/$1/;

if ($company_id =~ /^\w+$/) {

	$process_id = $$;
	$output_ppm = join ("", "/tmp/", $process_id, ".ppm");
	$data_file =  join ("", "/tmp/", $process_id, ".txt");
	$color_number = 1; 

	$system_id = "Miscellaneous";
	$username = "shishir";
	$password = "fnjop673e2nB";

	$lda = &ora_login ($system_id, $username, $password);
	$csr = &ora_open ($lda,
		" select * from Stocks where ID = '$company_id' ");

	if ( open (DATA, ">" . $data_file) ) {
		($company_id, $company, @stock_prices) = &ora_fetch ($csr);

		&ora_close ($csr);
		&ora_logoff ($lda);

		if ($company_id) {

			$stocks_start = 1980;
			$stocks_end = 1990;
			$stocks_duration = $stocks_end - $stocks_start;

			for ($loop=0; $loop <= $stocks_duration; $loop++) {
				$price = $stock_prices[$loop];
				$year  = $stocks_start + $loop;

				print DATA $year, " ", $price, "\n";
			}
			close (DATA);

			&graph_data ("Stock History for $company", $data_file,
				     "Year", "Price", $color_number,
				     $output_ppm);

			&create_gif ($output_ppm);
		} else {
			&return_error (500, "Oracle Gateway CGI Error",
				"The specified company could not be found.");
		}
	} else {
		&return_error (500, "Oracle Gateway CGI Error",
				    "Could not create output file.");
	}
} else {
	&return_error (500, "Oracle Gateway CGI Error",
			    "Invalid characters in company field.");
}

exit (0);

sub graph_data
{
	local ($title, $file, $x_label, $y_label, $color, $output) = @_;

	open (GNUPLOT, "| $gnuplot");
	print GNUPLOT <<gnuplot_Commands_Done;
	
		set term pbm color small
		set output "$output"
		set title "$title"
		set xlabel "$x_label"
		set ylabel "$y_label"
		set noxzeroaxis
		set noyzeroaxis
		set border
		set nokey
		plot "$file" w lines $color

gnuplot_Commands_Done

	close (GNUPLOT);
}

sub create_gif
{
	local ($output) = @_;

	print "Content-type: image/gif", "\n\n";
	system ("$ppmtogif $output 2> /dev/null");

	unlink $output_ppm, $data_file;
}
