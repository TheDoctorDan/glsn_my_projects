#!/usr/local/bin/perl

$form = 0;
$this_script = $ENV{'SCRIPT_NAME'};
$webmaster = "Shishir Gundavaram (shishir\@bu\.edu)";
$separator = "\034";

$exclusive_lock = 2;
$unlock = 8;
$document_root = "/home/shishir/httpd_1.4.2/public";
$images_dir = "/images";
$quiz_file = $ENV{'PATH_INFO'};

if ($quiz_file) {
    $full_path = $document_root . $quiz_file;
} else {
    &return_error (500, "CGI Quiz File Error", 
                        "A quiz data file has to be specified.");
}

open (FILE, "<" . $full_path) || &return_error (500, "CGI Quiz File Error",
                                     "Cannot open quiz data file [$full_path].");
flock (FILE, $exclusive_lock);

if ($ENV{'REQUEST_METHOD'} eq "POST") {
    &parse_form_data(*QUIZ);
}

print "Content-type: text/html", "\n\n";

while (<FILE>) {
    if (/<\s*quiz\s*>/i) {

        $form++;
        $count = 0;
	
        if ($QUIZ{'cgi_quiz_form'}) {
            $no_correct = $no_wrong = $no_skipped = 0;
            $correct = "Correct! ";
            $wrong = "Wrong! ";
            $skipped = "Skipped! ";
        }

        &print_form_header();

        while (<FILE>) {
            if (($type) = /<\s*question\s*type\s*=\s*"?([^ ">]+)"?\s*>/i) {

                $count++;

                while (<FILE>) {
                    if (!/<\s*\/question\s*>/i) {
                        $line = join("", $line, $_);
                    } else {
                        last;
                    }
                }

                $line =~ s/\n/ /g;

                ($ask) = ($line =~ /<\s*ask\s*>(.*)<\s*\/ask\s*>/i);
                &print_question($ask);

                $type =~ tr/A-Z/a-z/;
                $variable = join("-", $count, $type);

                if ($type =~ /^multiple$/i) {
                    &split_multiple("choice", *choices);
                    &print_radio_buttons(*choices);
                } elsif ($type =~ /^text$/i) {
                    &print_text_field();
                }

                if ($line =~ /<\s*hint\s*>/i) {
                    &split_multiple("hint", *hints);
                    &print_hints(*hints);
                }

                if ($QUIZ{'cgi_quiz_form'} == $form) {
                    local ($answer, %quiz_keys, %quiz_values, @responses,
			                        $user_answer);

                    &set_browser_graphics();

                    ($answer) = ($line =~ /<\s*answer\s*>(.*)<\s*\/answer\s*>/i);
                    &format_string(*answer);

                    $user_answer = $QUIZ{$variable};
                    &format_string(*user_answer);
    
                    &split_multiple("response", *responses);
                    &split_responses(*responses, *quiz_keys, *quiz_values);
                    print "<HR><BR>";

                    if ($user_answer eq $answer) {
                        print $correct;
                        $no_correct++;
                    } elsif ($user_answer eq "") {
                        print $skipped;
                        $no_skipped++;

                        if ($quiz_keys{'skip'}) {
                            print $quiz_values{'skip'}, " ";
                        }
                    } else {
                        print $wrong;
                        $no_wrong++;

                        if ($quiz_keys{'wrong'}) {
                            print $quiz_values{'wrong'}, " ";
                        }
                    }
                    
                    if ($user_answer eq $quiz_keys{$user_answer}) {
                        print $quiz_values{$user_answer}, " ";
                    } 

                    print "<BR><HR><BR>";
                }

                $line = "";

            } elsif (/<\s*\/quiz\s*>/i) {
                last;
            } else {
                print;
            }
        }

        &print_form_footer();

    } else {
        print;
    }

}

flock (FILE, $unlock);
close (FILE);
exit(0);

sub print_form_header
{
    print <<Form_Header;
<FORM ACTION="${this_script}/${quiz_file}?cgi_quiz_form=${form}" METHOD="POST">

Form_Header
}

sub parse_form_data
{
    local (*FORM_DATA) = @_;
	
    local ($query_string, @key_value_pairs, $key_value, $key, $value);
	
    read (STDIN, $query_string, $ENV{'CONTENT_LENGTH'});

    if ($ENV{'QUERY_STRING'}) {
            $query_string = join("&", $query_string, $ENV{'QUERY_STRING'});
    }     

    @key_value_pairs = split (/&/, $query_string);

    foreach $key_value (@key_value_pairs) {
        ($key, $value) = split (/=/, $key_value);
        $value =~ tr/+/ /;
        $value =~ s/%([\dA-Fa-f][\dA-Fa-f])/pack ("C", hex ($1))/eg;

        if (defined($FORM_DATA{$key})) {
            $FORM_DATA{$key} = join ("\0", $FORM_DATA{$key}, $value);
        } else {
            $FORM_DATA{$key} = $value;
        }
    }
}

sub set_browser_graphics
{
    local ($nongraphic_browsers, $client_browser);

    $nongraphic_browsers = 'Lynx|CERN-LineMode';
    $client_browser = $ENV{'HTTP_USER_AGENT'};

    if ($client_browser !~ /$nongraphic_browsers/) {
        $correct = "<IMG SRC=\"$images_dir/correct.gif\">";
        $wrong = "<IMG SRC=\"$images_dir/wrong.gif\">";
        $skipped = "<IMG SRC=\"$images_dir/skipped.gif\">";
    } 
}

sub print_question
{
    local ($question) = @_;

    print <<Question;
<H3>Question $count</H3>
<EM>$question</EM>
<P>

Question
}

sub format_string
{
    local (*string) = @_;

    $string =~ s/^\s*(.*)\b\s*$/$1/;
    $string =~ s/\s+/\s/g;
    $string =~ tr/A-Z/a-z/;
}

sub split_multiple
{
    local ($tag, *multiple) = @_;
    local ($info, $first, $loop);

    if ( ($tag eq "choice") || ($tag eq "response") ) {
        ($first, $info) = ($line =~ /<\s*$tag\s*([^>]+)>(.*)<\s*\/$tag\s*>/i);
        $info =~ s/<\s*$tag\s*([^>]+)>/$1$separator/ig;
        $info = join("$separator", $first, $info);
    } else {
        ($info) = ($line =~ /<\s*$tag\s*>(.*)<\s*\/$tag\s*>/i);
        $info =~ s/<\s*$tag\s*>//ig;
    }

    @multiple = split(/<\s*\/$tag\s*>/i, $info);

    for ($loop=0; $loop <= $#multiple; $loop++) {
        $multiple[$loop] =~ s/^\s*(.*)\b\s*$/$1/;
    }
}

sub print_radio_buttons
{
    local (*buttons) = @_;

    local ($loop, $letter, $value, $checked, $user_answer);

    if ($QUIZ{'cgi_quiz_form'}) {
        $user_answer = $QUIZ{$variable};
    }

    for ($loop=0; $loop <= $#buttons; $loop++) {
        ($letter, $value) = split(/$separator/, $buttons[$loop], 2);
        $letter =~ s/^\s*(.*)\b\s*$/$1/;
        $value =~ s/^\s*(.*)\b\s*$/$1/;
     
       if ($user_answer eq $letter) {
            $checked = "CHECKED";
        } else {
            $checked = "";
        }

        print <<Radio_Button;
<INPUT TYPE="radio" NAME="$variable" VALUE="$letter" $checked>
$value<BR>

Radio_Button
    }
}

sub print_text_field
{
    local ($default);

    if ($QUIZ{'cgi_quiz_form'}) {
        $default = $QUIZ{$variable};
    } else {
        $default = "";
    }

    print <<Text_Field;
<INPUT TYPE="text" NAME="$variable" SIZE=50 VALUE="$default"><BR>

Text_Field
}

sub print_hints
{
    local (*list) = @_;
    local ($loop);

    print "<UL>", "\n";

    for ($loop=0; $loop <= $#list; $loop++) {
        print <<Unordered_List;
<LI>$list[$loop]

Unordered_List
    }

    print "</UL>", "\n";
}

sub split_responses
{
    local (*all, *index, *message) = @_;
    local ($loop, $key, $value);

    for ($loop=0; $loop <= $#all; $loop++) {
        ($key, $value) = split(/$separator/, $all[$loop], 2);
        &format_string(*key);
        $value =~ s/^\s*(.*)\b\s*$/$1/;
		
        $index{$key} = $key;
        $message{$key} = $value;
    }
}

sub print_form_footer
{
    if (!$QUIZ{'cgi_quiz_form'}) {
        print '<INPUT TYPE="submit" VALUE="Submit Quiz">';
        print '<INPUT TYPE="reset"  VALUE="Clear Answers">';
    } else {
	        print <<Status;
Results: $no_correct Correct -- $no_wrong Wrong -- $no_skipped Skipped<BR>

Status
    }

    print "</FORM>";
}

sub return_error
{
        local ($status, $keyword, $message) = @_;

        print "Content-type: text/html", "\n";
        print "Status: ", $status, " ", $keyword, "\n\n";

        print <<End_of_Error;

<title>CGI Program - Unexpected Error</title>
<h1>$keyword</h1>
<hr>$message</hr>
Please contact $webmaster for more information.

End_of_Error

        exit(1);
}


