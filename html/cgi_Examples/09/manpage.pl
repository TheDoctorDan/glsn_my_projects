#!/usr/local/bin/perl

require "common.pl";

$webmaster = "Shishir Gundavaram (shishir\@bu\.edu)";
$script = $ENV{'SCRIPT_NAME'};
$man_path = "/usr/local/man";
$nroff = "/usr/bin/nroff -man";
$last_line = "Last change:";

&parse_form_data (*FORM);

($manpage = $FORM{'manpage'}) =~ s/^\s*(.*)\b\s*$/$1/;
$section = $FORM{'section'};

if ( (!$manpage) || ($manpage !~ /^[\w\+\-]+$/) ) {
	&return_error (500, "UNIX Manual Page Gateway Error",
			    "Invalid manual page specification.");
} else {
	if ($section !~ /^\d+$/) {
		$section = &find_section ();
	} else {
		$section = &check_section ();
	}

	if ( ($section >= 1) && ($section <= 8) ) {
		&display_manpage ();
	} else {
		&return_error (500, "UNIX Manual Page Gateway Error",
				    "Could not find the requested document.");
	}
}

exit (0);

sub find_section
{
	local ($temp_section, $loop, $temp_dir, $temp_file);

	$temp_section = 0;

	for ($loop=1; $loop <= 8; $loop++) {
		$temp_dir = join("", $man_path, "/man", $loop);
		$temp_file = join("", $temp_dir, "/", $manpage, ".", $loop);

		if (-e $temp_file) {
			$temp_section = $loop;
		}
	}

	return ($temp_section);
}

sub check_section
{
	local ($temp_section, $temp_file);

	$temp_section = 0;
	$temp_file = join ("", $man_path, "/man", $section,
			       "/", $manpage, ".", $section);

	if (-e $temp_file) {
		$temp_section = $section;
	}

	return ($temp_section);
}

sub display_manpage
{
	local ($file, $blank, $heading);

	$file = join ("", $man_path, "/man", $section, 
			  "/", $manpage, ".", $section);

	print "Content-type: text/html", "\n\n";

        print "<HTML>", "\n";
	print "<HEAD><TITLE>UNIX Manual Page Gateway</TITLE></HEAD>", "\n";
        print "<BODY>", "\n";
	print "<H1>UNIX Manual Page Gateway</H1>", "\n";
	print "<HR><PRE>";

	open (MANUAL, "$nroff $file |");
	$blank = 0;

	while (<MANUAL>) {
		next if ( (/^$manpage\(\w+\)/i) || (/\b$last_line/o) );

		if (/^([A-Z0-9_ ]+)$/) {
			$heading = $1;
			print "<H2>", $heading, "</H2>", "\n";
		} elsif (/^\s*$/) {
			$blank++;

			if ($blank < 2) {
				print;
			}
		} else {
		
			$blank = 0;

			s//&amp;/g		if (/&/);
			s//&lt;/g		if (/</);
			s//&gt;/g		if (/>/);

			if (/((_\010\S)+)/) {
				s//<B>$1<\/B>/g;
				s/_\010//g;
			}

			if ($heading =~ /ALSO/) {
				if (/([\w\+\-]+)\((\w+)\)/) {
s//<A HREF="$script\?manpage=$1&section=$2">$1($2)<\/A>/g;
				}
			}

			print;
		}
	}

	print "</PRE><HR>", "\n";
        print "</BODY></HTML>", "\n";
	
	close (MANUAL);
}


