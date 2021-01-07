#!/usr/local/bin/perl

require "common.pl";

$webmaster = "Shishir Gundavaram (shishir\@bu\.edu)";
$fgrep = "/usr/local/bin/fgrep";
$document_root = $ENV{'DOCUMENT_ROOT'};

&parse_form_data (*SEARCH);
$query = $SEARCH{'query'};

if ($query eq "") {
	&return_error (500, "Search Error", "Please enter a search query.");
} elsif ($query !~ /^(\w+)$/) {
	&return_error (500, "Search Error", "Invalid characters in query.");
} else {
	print "Content-type: text/html", "\n\n";
	print "<HTML>", "\n";
	print "<HEAD><TITLE>Search Results</TITLE></HEAD>";
	print "<BODY>", "\n";
	print "<H1>Results of searching for: ", $query, "</H1>";
	print "<HR>";

	open (SEARCH, "$fgrep -A2 -B2 -i -n -s $query $document_root/* |");

	$count = 0;
	$matches = 0;
	%accessed_files = ();

	while (<SEARCH>) {
		if ( ($file, $type, $line) = m|^(/\S+)([\-:])\d+\2(.*)| ) {

			unless ($count) {
				if ( defined ($accessed_files{$file}) ) {
					next;
				} else {
					$accessed_files{$file} = 1;
				}

				$file =~ s/^$document_root\/(.*)/$1/;
				$matches++;

				print qq|<A HREF="/$file">$file</A><BR><BR>|;
			}

			$count++;
			$line =~ s/<(([^>]|\n)*)>/&lt;$1&gt;/g;
	
			if ($line =~ /^[^A-Za-z0-9]*$/) {
				next;
			}

			if ($type eq ":") {
				$line =~ s/($query)/<B>$1<\/B>/ig;
			}

			print $line, "<BR>";

		} else {
			if ($count) {
				print "<HR>";
				$count = 0;
			}
		}
	}
	
	print "<P>", "<HR>";
	print "Total number of files searched: ", $matches, "<BR>";
	print "<HR>";
	print "</BODY></HTML>", "\n";

	close (SEARCH);
}

exit (0);

