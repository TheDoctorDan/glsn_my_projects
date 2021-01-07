#!/usr/local/bin/perl5

use GD;

$| = 1;
print "Content-type: image/gif", "\n\n";

$max_length = 175;
$image = new GD::Image ($max_length, $max_length);

$white = $image->colorAllocate (255, 255, 255);
$red = $image->colorAllocate (255, 0, 0);
$blue = $image->colorAllocate (0, 0, 255);

@origin = (30, 140);

$image->string (gdLargeFont, 12, 15, "System Load Average", $blue);
$image->line (@origin, 105 + $origin[0], $origin[1], $blue);
$image->line (@origin, $origin[0], $origin[1] - 105, $blue);

for ($y_axis=0; $y_axis <= 100; $y_axis = $y_axis + 10) {
	$image->line (  $origin[0] - 5, 
			$origin[1] - $y_axis,
			$origin[0] + 5,
			$origin[1] - $y_axis,
			$blue  );
}

for ($x_axis=0; $x_axis <= 100; $x_axis = $x_axis + 25) {
	$image->line (	$x_axis + $origin[0],
			$origin[1] - 5,
			$x_axis + $origin[0],
			$origin[1] + 5,
			$blue );
}

$uptime = `/usr/ucb/uptime`;
($load_averages) = ($uptime =~ /average: (.*)$/);
@loads[0..2] = split(/,\s/, $load_averages);

for ($loop=0; $loop < 2; $loop++) {
        if ($loads[$loop] > 10) {
                $loads[$loop] = 10;
        }
}

$polygon = new GD::Polygon;

$polygon->addPt (@origin);
for ($loop=1; $loop <= 3; $loop++) {
	$polygon->addPt ( $origin[0] + (25 * $loop), 
			  $origin[1] - ($loads[$loop-1] * 10)  );
}
$polygon->addPt (100 + $origin[0], $origin[1]);

$image->filledPolygon ($polygon, $red);
print $image->gif;

exit(0);
