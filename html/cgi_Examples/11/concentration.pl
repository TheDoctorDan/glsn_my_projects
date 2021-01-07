#!/usr/local/bin/perl

@BOARD = ();
$display = "";
$spaces = " " x 5;
$images_dir = "/icons";

$query_string = $ENV{'QUERY_STRING'};

if ($query_string) {
    ($new_URL_query, $user_selections) = &undecode_query_string (*BOARD);
    &draw_current_board (*BOARD, $new_URL_query, $user_selections);
} else {
    &create_game (*BOARD);
    $new_URL_query = &build_decoded_query (*BOARD);
    &draw_clear_board ($new_URL_query);
}

&display_board ();

exit(0);

sub create_game
{
    local (*game_board) = @_;
    local ($loop, @number, $random);
	
    srand (time | $$);
	
    for ($loop=1; $loop <= 16; $loop++) {
	$game_board[$loop] = 0;
    }
	
    for ($loop=1; $loop <= 8; $loop++) {
	$number[$loop] = 0;
    }
	
    for ($loop=1; $loop <= 16; $loop++) {
	do {
	    $random = int (rand(8)) + 1;
	} until ($number[$random] < 2);

	$game_board[$loop] = $random;
	$number[$random]++;
    }
}

sub build_decoded_query
{
    local (*game_board) = @_;
    local ($URL_query, $loop, @temp_board);
	
    for ($loop=1; $loop <= 16; $loop++) {
	($temp_board[$loop] = $game_board[$loop]) =~ 
		s/(\w+)/sprintf ("%lx", $1 * (($loop * 50) + 100))/e;
    }

    $URL_query = join ("%", @temp_board);

    return ($URL_query);
}

sub build
{
    local (@string) = @_;

    $display = join ("", $display, @string);
}

sub draw_clear_board
{
    local ($URL_query) = @_;
    local ($URL, $inner, $outer, $index, $anchor);

    $URL = join ("", $ENV{'SCRIPT_NAME'}, "?", $URL_query);
	
    for ($outer=1; $outer <= 4; $outer++) {
	for ($inner=1; $inner <= 4; $inner++) {
	    $index = (4 * ($outer - 1)) + $inner;
	    $anchor = join("%", "", $index, "0");
			
	    &build(qq|<A HREF="$URL$anchor">**</A>|, $spaces);
	}
	&build ("\n\n");
    }
}

sub undecode_query_string
{
    local (*game_board) = @_;
    local ($user_choices, $loop, $original_query, $URL_query);

    $ENV{'QUERY_STRING'} =~ /^((%\w+\+{0,1}){16})%(.*)$/;

    ($original_query, $user_choices) = ($1, $3);
    @game_board = split (/%/, $original_query);

    for ($loop=1; $loop <= 16; $loop++) {
        $game_board[$loop] =~ s|(\w+)|hex ($1) / (($loop * 50) + 100)|e;
    }

    $URL_query = join ("%", @game_board);

    return ($URL_query, $user_choices);
}

sub draw_current_board
{
    local (*game_board, $URL_query, $user_choices) = @_;
    local ($one, $two, $count, $script, $URL, $outer, $inner, $index, $anchor);

    ($one, $two) = split (/%/, $user_choices);
    $count = 0;

    if ( int ($game_board[$one]) == int ($game_board[$two]) ) {
	$game_board[$one] = join ("", $game_board[$one], "+");
	$game_board[$two] = join ("", $game_board[$two], "+");
    }			      

    $URL_query = &build_decoded_query (*game_board);

    $script = $ENV{'SCRIPT_NAME'};
    $URL = join ("", $script, "?", $URL_query);
			      
    for ($outer=1; $outer <= 4; $outer++) {
        for ($inner=1; $inner <= 4; $inner++) {
	    $index = (4 * ($outer - 1)) + $inner;

	    if ($game_board[$index] =~ /\+/) {
		$game_board[$index] =~ s/\+//;
		&build (sprintf ("%02d", $game_board[$index]), $spaces);
		$count++;
		
	    } elsif ( ($index == $one) || ($index == $two) ) {
		&build (sprintf ("%02d", $game_board[$index]), $spaces);
	
	    } else {
		if ($one && $two) {
		    $anchor = join("%", "", $index, "0");
		} else {
		    $anchor = join("%", "", $one, $index);
		}
				
		&build(qq|<A HREF="$URL$anchor">**</A>|, $spaces);
	    }
	}
    
	&build ("\n\n");
    }

    if ($count == 16) {
	&build ("<HR>You Win!\nIf you want to play again, ");
	&build (qq|click <A HREF="$script">here</A><BR>|);
    }
}

sub display_board
{
    local ($client_browser, $nongraphic_browsers);

    $client_browser = $ENV{'HTTP_USER_AGENT'};
    $nongraphic_browsers = 'Lynx|CERN-LineMode';

    print "Content-type: text/html", "\n\n";
	
    if ($client_browser =~ /$nongraphic_browsers/) {
	print "Welcome to the game of Concentration!", "\n";
    } else {
	print qq|<IMG SRC="$images_dir/concentration.gif">|;

	$display =~ s|\*\*</A>|<IMG SRC="$images_dir/question.gif"></A> |g;
	$display =~ s|(\d+)\s|<IMG SRC="$images_dir/$1.gif">   |g;
	$display =~ s|\n\n|\n\n\n|g;
	$display =~ s|You Win!|<IMG SRC="$images_dir/win.gif">|g;
    }

    print "<HR>", "<PRE>", "\n";
    print $display, "\n";
    print "</PRE>", "<HR>", "\n";
}

