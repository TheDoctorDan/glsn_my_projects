#!/usr/local/bin/perl5

use GD;

$| = 1;
print "Content-type: image/gif", "\n\n";

($seconds, $minutes, $hour) = localtime (time);

if ($hour > 12) {
        $hour -= 12;
        $ampm = "pm";
} else {
        $ampm = "am";
}

if ($hour == 0) {
        $hour = 12;
}

$time = sprintf ("%02d:%02d:%02d %s", $hour, $minutes, $seconds, $ampm);

$time_length = length($time);
$font_length = 8;
$font_height = 16;
$x = $font_length * $time_length;
$y = $font_height;

$image = new GD::Image ($x, $y);

$black = $image->colorAllocate (0, 0, 0);
$red = $image->colorAllocate (255, 0, 0);

$image->string (gdLargeFont, 0, 0, $time, $red);
print $image->gif;

exit(0);
