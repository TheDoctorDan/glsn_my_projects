#!/usr/local/bin/perl

require "timelocal.pl";
require "bigint.pl";

($chosen_date = $ARGV[0]) =~ s/\s*//g;

if ($chosen_date =~ m|^(\d+)/(\d+)/(\d+)$|) {
    ($month, $day, $year) = ($1, $2, $3);

    $month -= 1;

    if ($year > 1900) {
        $year -= 1900; 
    }

    $chosen_secs = &timelocal (undef, undef, undef, $day, $month, $year);

    $seconds_in_day = 60 * 60 * 24;   
    $difference = &bsub ($chosen_secs, time);
    $no_days = &bdiv ($difference, $seconds_in_day);
    $no_days =~ s/^(\+|-)//;

    print $no_days;
    exit(0);

} else {
    print " [Error in date format] ";
    exit(1);
}

