#!/usr/local/bin/perl5

require "common.pl";

use GD;

$| = 1;
$webmaster = "shishir\@bu\.edu";

print "Content-type: image/gif", "\n\n";

&parse_form_data (*color_text);
$message = $color_text{'message'};
$color = $color_text{'color'};

if (!$message) {
    $message = "This is an example of " . $color . " text";
}

$font_length = 8;
$font_height = 16;
$length = length ($message);

$x = $length * $font_length;
$y = $font_height;

$image = new GD::Image ($x, $y);

$white = $image->colorAllocate (255, 255, 255);

if ($color eq "Red") {
    @color_index = (255, 0, 0);
} elsif ($color eq "Blue") {
    @color_index = (0, 0, 255);
} elsif ($color eq "Green") {
    @color_index = (0, 255, 0);
} elsif ($color eq "Yellow") {
    @color_index = (255, 255, 0);
} elsif ($color eq "Orange") {
    @color_index = (255, 165, 0);
} elsif ($color eq "Purple") {
    @color_index = (160, 32, 240);
} elsif ($color eq "Brown") {
    @color_index = (165, 42, 42);
} elsif ($color eq "Black") {
    @color_index = (0, 0, 0);
}

$selected_color = $image->colorAllocate (@color_index);
$image->transparent ($white);

$image->string (gdLargeFont, 0, 0, $message, $selected_color);
print $image->gif;

exit(0);
