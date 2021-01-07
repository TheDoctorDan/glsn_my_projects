#!/usr/local/bin/sybperl

require "common.pl";
require "sybperl.pl";

$user = "shishir";
$password = "mkhBhd9v2sK";
$server = $ENV{'DSQUERY'} || "Books";

$dbproc = &dblogin ($user, $password, $server);

@fields = ('Author', 'Book', 'Publisher', 'Year', 'Pages');
$title = "CGI Publishing Company Book Database";

&parse_form_data (*DB);
($book_name = $DB{'Book'}) =~ s/^\s*(.*)\b\s*$/$1/;

if ($book_name =~ /^[\w\s]+$/) {
 	&dbcmd ($dbproc, " select * from Catalog where Book = '$book_name' ");
	&dbsqlexec ($dbproc);
	$status = &dbresults ($dbproc);

	if ($status == $SUCCEED) {
		while ( (@books = &dbnextrow ($dbproc)) ) {
			$book_string = join ("\0", @books);
			push (@all_books, $book_string);
		}

		&dbexit ($dbproc);
		&display_table ($title, *fields, *all_books, "\0");
	} else {
		&return_error (500, "Sybase Database CGI Error",
			"The book title(s) you specified does not exist.");
	}
} else {
	&return_error (500, "Sybase Database CGI Error",
			    "Invalid characters in book name.");
}

exit(0);


sub display_table
{
	local ($title, *columns, *selected_entries, $delimiter) = @_;
	local ($name, $entry);

	print "Content-type: text/html", "\n\n";
	print "<HTML>", "\n";
	print "<HEAD><TITLE>", $title, "</TITLE></HEAD>", "\n";
	print "<BODY>", "\n";
	print "<TABLE BORDER=2>", "\n";
	print "<CAPTION>", $title, "</CAPTION>", "\n";
	print "<TR>", "\n";

	foreach $name (@columns) {
		print "<TH>", $name, "\n";
	}

	foreach $entry (@selected_entries) {
		$entry =~ s/$delimiter/<TD>/go;
		print "<TR>", "<TD>", $entry, "\n";
	}

	print "</TABLE>", "\n";
	print "</BODY></HTML>", "\n";
}



