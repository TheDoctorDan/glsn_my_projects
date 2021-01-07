#!/usr/local/bin/perl5

use GD;
require "getopts.pl";

$usage = "Usage: $0 -i input_file [-o output_file]";

&Getopts (":i:o:");
($input, $output) = ($opt_i, $opt_o);

$input || die $usage, "\n";
$output || ($output = "-");

open (ORIGINAL,    "<" . $input);
open (TRANSPARENT, ">" . $output) || die "Cannot create GIF file.", "\n";

$image = newFromGif GD::Image (ORIGINAL) || die "Cannot read GIF file!\n";
$white = $image->colorClosest (255, 255, 255);
$image->transparent ($white);

print TRANSPARENT $image->gif;

close (TRANSPARENT);
close (ORIGINAL);

exit (0);




