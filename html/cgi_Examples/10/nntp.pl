#!/usr/local/bin/perl

require "common.pl";
require "sockets.pl";

$webmaster = "Shishir Gundavaram (shishir\@bu\.edu)";
$error = "CGI NNTP Gateway Error";

%groups = ('cgi',    'comp.infosystems.www.authoring.cgi',
	   'html',   'comp.infosystems.www.authoring.html',
	   'images', 'comp.infosystems.www.authoring.images',
	   'misc',   'comp.infosystems.www.authoring.misc',
	   'perl',   'comp.lang.perl.misc');

$all_groups = '(cgi|html|images|misc|perl)';
$nntp_server = "nntp.bu.edu";

&parse_form_data (*NEWS);
$group_name = $NEWS{'group'};
$article_number = $NEWS{'article'};

if ($group_name =~ /\b$all_groups\b/o) {
	$selected_group = $groups{$group_name};
	&open_connection (NNTP, $nntp_server, "nntp") ||
	      &return_error (500, $error, "Could not connect to NNTP server.");

	&check_nntp ();
	($first, $last) = &set_newsgroup ($selected_group);

	if ($article_number) {
		if (($article_number < $first) || ($article_number > $last)) {
			&return_error (500, $error,
			     "The article number you specified is not valid.");
		} else {
			&show_article ($selected_group, $article_number);
		}
	} else {
		&show_all_articles ($group_name, $selected_group, $first, $last);
	}

	print NNTP "quit", "\n";
	&close_connection (NNTP);
} else {
	&display_newsgroups ();
}

exit (0);

sub print_header
{
	local ($title) = @_;

	print "Content-type: text/html", "\n\n";
	print "<HTML>", "\n";
	print "<HEAD><TITLE>", $title, "</TITLE></HEAD>", "\n";
	print "<BODY>", "\n";
	print "<H1>", $title, "</H1>", "\n";
	print "<HR>", "<BR>", "\n";
}

sub print_footer
{
	print "<HR>", "\n";
	print "<ADDRESS>", $webmaster, "</ADDRESS>", "\n";
	print "</BODY></HTML>", "\n";
}

sub escape
{
	local ($string) = @_;

	$string =~ s/([^\w\s])/sprintf ("&#%d;", ord ($1))/ge;

	return ($string);
}

sub check_nntp
{
	while (<NNTP>) {
		if (/^(200|201)/) {
			last;
		} elsif (/^4|5\d+/) {
			&return_error (500, $error,
				"The NNTP server retured an error.");
		}
	}
}

sub set_newsgroup
{
	local ($group) = @_;
	local ($group_info, $status, $first_post, $last_post);

	print NNTP "group ", $group, "\n";
	$group_info = <NNTP>;
	($status, $first_post, $last_post)
		= (split (/\s+/, $group_info))[0, 2, 3];

	if ($status != 211) {
		&return_error (500, $error,
			"Could not get group information for $group.");
	} else {
		return ($first_post, $last_post);
	}
}

sub show_article
{
	local ($group, $number) = @_;
	local ($useful_headers, $header_line);
	
	$useful_headers = '(From:|Subject:|Date:|Organization:)';

	print NNTP "head $number", "\n";
	$header_line = <NNTP>;

	if ($header_line =~ /^221/) {
		&print_header ($group);
		print "<PRE>", "\n";

		while (<NNTP>) {
			if (/^$useful_headers/) {
				$_ = &escape ($_);
				print "<B>", $_, "</B>";
			} elsif (/^\.\s*$/) {	
				last;
			}
		}

		print "\n";
		print NNTP "body $number", "\n";
		<NNTP>;

		while (<NNTP>) {	
			last if (/^\.\s*$/);
			$_ = &escape ($_);
			print;
		}

		print "</PRE>", "\n";
		&print_footer ();
	} else {
		&return_error (500, $error,
			"Article number $number could not be retrieved.");
	}
}

sub show_all_articles
{
	local ($id, $group, $first_article, $last_article) = @_;
	local ($this_script, %all, $count, @numbers, $article,
	       $subject, @threads, $query);

	$this_script = $ENV{'SCRIPT_NAME'};
	$count = 0;

	print NNTP "xhdr subject $first_article-$last_article", "\n";
	<NNTP>;

	&print_header ("Newsgroup: $group");
	print "<UL>", "\n";

	while (<NNTP>) {
		last if (/^\.\s*$/);
		$_ = &escape ($_);

		($article, $subject) = split (/\s+/, $_, 2);

		$subject =~ s/^\s*(.*)\b\s*/$1/;
		$subject =~ s/^[Rr][Ee]:\s*//;

		if (defined ($all{$subject})) {
			$all{$subject} = join ("-", $all{$subject}, $article);
		} else {
			$count++;
			$all{$subject} = join ("\0", $count, $article); 
		}
	}

	@numbers = sort by_article_number keys (%all);

	foreach $subject (@numbers) {
		$article = (split("\0", $all{$subject}))[1];

		@threads = split (/-/, $article);

		foreach (@threads) {		
			$query = join ("", $this_script, "?", "group=", $id, 
					   "&", "article=", $_);

			print qq|<LI><A HREF="$query">$subject</A>|, "\n";
		}
	}

	print "</UL>", "\n";
	&print_footer ();
}

sub by_article_number
{
    $all{$a} <=> $all{$b};
}

sub display_newsgroups
{
	local ($script_name, $keyword, $newsgroup, $query);

	&print_header ("CGI NNTP Gateway");
	$script_name = $ENV{'SCRIPT_NAME'};

	print "<UL>", "\n";

	foreach $keyword (keys %groups) {
		$newsgroup = $groups{$keyword};
		$query = join ("", $script_name, "?", "group=", $keyword);

		print qq|<LI><A HREF="$query">$newsgroup</A>|, "\n";
	}

	print "</UL>";
	&print_footer ();
}




