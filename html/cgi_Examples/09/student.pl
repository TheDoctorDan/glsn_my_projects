#!/usr/local/bin/perl5

require "common.pl";

use Sprite;

$query_string = $ENV{'QUERY_STRING'};
$script = $ENV{'SCRIPT_NAME'};
$request_method = $ENV{'REQUEST_METHOD'};

$webmaster = "shishir\@bu\.edu";
$database = "/home/shishir/student.db";
$main_form = "/student.html";
$commands = '(add|modify_form|modify|view|delete)';

$delimiter = "::";
$error = "CGI Student Database Error";

if ($query_string =~ /^\b$commands\b$/) {
	&parse_form_data (*DB);
	&check_all_fields ();

	&check_database ();

	$rdb = new Sprite ();
	$rdb->set_delimiter ("Read",  $delimiter);
	$rdb->set_delimiter ("Write", $delimiter);

	$command_status = &$query_string ();

	if ($command_status) {
		$rdb->close ($database);
		print "Location: ", $main_form, "\n\n";
	} else {
		$rdb->close ();
	}

} else {
	&return_error (500, $error,
			    "Invalid command passed through QUERY_STRING.");
}

exit (0);

sub check_database
{
	local ($exclusive_lock, $unlock, $header);

	$exclusive_lock = 2;
	$unlock = 8;

	if (! (-e $database) ) {
		if ( open (DATABASE, ">" . $database) ) {
			flock (DATABASE, $exclusive_lock);

	                $header = join ($delimiter,"Student","YOG","Address");
		        print DATABASE $header, "\n";

			flock (DATABASE, $unlock);
			close (DATABASE);
		} else {
			&return_error (500, $error,
				"Cannot create new student database.");
		}
	}
}

sub check_all_fields
{
	local ($key);

	foreach $key (keys %DB) {
		if ($DB{$key} =~ /[`\!;\|\*\$&<>]/) {
		      &return_error (500, $error,
				"Invalid characters in the [$key] field.");
		}
	}
}

sub build_check_condition
{
	local ($columns) = @_;
	local ($all_fields, $loop, $key, $sign, $sql_condition);

	@all_fields = split (/,/, $columns);

	for ($loop=0; $loop <= $#all_fields; $loop = $loop + 2) {
		$key  = $all_fields[$loop];
		$sign = $all_fields[$loop + 1];

		if ($DB{$key}) {
		      $DB{$key} =~ s/(\W)/\\$1/g; 

		      $sql_condition = join (" and ", $sql_condition,
				    "( $key $sign '$DB{$key}' )");
		}
	}

	if ($sql_condition) {
		$sql_condition =~ s/^ and //;

		return ($sql_condition);
	} else {
		&return_error (500, $error, "No query was entered.");
	}
}

sub database_error
{
	&return_error (500, $error,
		"Sprite database error. Please check the log file.");
}

sub check_select_command
{
	local ($value, $no_elements) = @_;

	if (!$value) {
		&database_error ();
	} elsif (!$no_elements) {
		&return_error (500, $error, 
			"The record you specified does not exist.");
	} else {
		return (1);
	}
}

sub add
{
	$DB{'Address'} =~ s/\n/<BR>/g;
        $DB{'Address'} =~ s/(['"])/\\$1/g;
        $DB{'Student'} =~ s/(['"])/\\$1/g;

	$rdb->sql (<<End_of_Insert) || &database_error ();
		
insert into $database
	(Student, YOG, Address)
values
	('$DB{'Student'}', '$DB{'YOG'}', '$DB{'Address'}')

End_of_Insert

	return (1);
}

sub modify_form
{
	local (@info, $modify_status, $no_elements, $status);

	$DB{'Student'} =~ s/(['"])/\\$1/g;

	@info = $rdb->sql (<<End_of_Select);

select * from $database
where (Student = '$DB{'Student'}')

End_of_Select

	$status = shift (@info);
	$no_elements = scalar (@info);

	$modify_status = &check_select_command ($status, $no_elements);

	if ($modify_status) {
		&display_modify_form ($info[0]);
	}

	return (0);
}

sub display_modify_form
{
	local ($fields) = @_;
	local ($student, $yog, $address);

	($student, $yog, $address) = split (/\0/, $fields);
	$address =~ s/<BR>/\n/g;

	$student = &escape_html ($student);
	$yog = &escape_html ($yog);

	print <<End_of_Modify_Form;
Content-type: text/html

<HTML>
<HEAD><TITLE>CGI Educational Center</TITLE></HEAD>
<BODY>
<H1>Modify Student Information</H1>
<HR>
<B>Student Name: $student</B>
<P>
<FORM ACTION="$script?modify" METHOD="POST">
<INPUT TYPE="hidden" NAME="Student" VALUE="$student">
Year of Graduation:
<INPUT TYPE="text" NAME="YOG" SIZE=4 MAXLENGTH=4 VALUE="$yog">
<P>
Address (Mailing Information):
<TEXTAREA NAME="Address" ROWS=4 COLS=40>
$address
</TEXTAREA>
<P>
<INPUT TYPE="submit" VALUE="Modify Record For: $student">
<INPUT TYPE="reset"  VALUE="Clear the Information">
</FORM>
<HR>
</BODY>
</HTML>

End_of_Modify_Form
}

sub escape_html
{
	local ($string) = @_;
	local (%html_chars, $html_string);

	%html_chars = ('&', '&amp;',
                       '>', '&gt;',
                       '<', '&lt;',
                       '"', '&quot;');

	$html_string = join ("", keys %html_chars);
	
	$string =~ s/([$html_string])/$html_chars{$1}/go;

	return ($string);
}

sub view
{
	local ( $fields, $query, @students,
		$view_status, $status, $no_elements);

	$fields = 'Student,=,Address,=~';

	if ($DB{'YOG'}) {
		if ($DB{'Sign'} eq 'greater') {
			$DB{'Sign'} = '>';
		} elsif ($DB{'Sign'} eq 'less') {
			$DB{'Sign'} = '<';
		} else {
			$DB{'Sign'} = '=';
		}

		$fields = join (",", $fields, 'YOG', $DB{'Sign'});
	}

	$query = &build_check_condition ($fields);

	@students = $rdb->sql (<<End_of_Display);

select * from $database
	where $query

End_of_Display

	$status = shift (@students);
	$no_elements = scalar (@students);

	$view_status = &check_select_command ($status, $no_elements);

	if ($view_status) {
		&display_results ("View Students", *students);
	}

	return (0);
}

sub display_results
{
	local ($title, *data) = @_;
	local ($student, $yog, $address);

	print "Content-type: text/html", "\n";
	print "Pragma: no-cache", "\n\n";
	print "<TITLE>CGI Educational Center</TITLE>";
	print "<H1>", $title, "</H1>";
	print "<HR>";

	foreach (@data) {
	        s/([^\w\s\0])/sprintf ("&#%d;", ord ($1))/ge;

		($student, $yog, $address) = split ("\0", $_, 3);

		$student = "NULL"                      if (!$student);
		$yog     = "Unknown graduation date"   if (!$yog);
		$address = "No address specified"      if (!$address);

		print "<BR>", "\n";
		print "<B>", $student, "</B> ", "($yog)", "<BR>", "\n";

		$address =~ s/&#60;BR&#62;/<BR>/g;
		print $address, "<BR>", "\n";
	}

	print "<HR>", "\n";
}

sub delete
{
	local ($fields, $query);

	$fields = 'Student,=,YOG,=';
	$query = &build_check_condition ($fields);

	$rdb->sql (<<End_of_Delete) || &database_error ();

delete from $database
	where $query

End_of_Delete

	return (1);
}

sub modify
{
	local (@fields, $key);

	@fields = ('YOG', 'Address');

	$DB{'Address'} =~ s/\n/<BR>/g;
	$DB{'YOG'} =~ s/(['"])/\\$1/g;
        $DB{'Student'} =~ s/(['"])/\\$1/g;
        $DB{'Address'} =~ s/(['"])/\\$1/g;

	foreach $key (@fields) {
		$rdb->sql (<<Update_Database) || &database_error ();

update $database
set $key = ('$DB{$key}') 
where (Student = '$DB{'Student'}');

Update_Database

	}

	return (1);
}
