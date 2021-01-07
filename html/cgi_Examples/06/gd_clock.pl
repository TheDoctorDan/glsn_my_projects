#!/usr/local/bin/perl5

use GD;

$| = 1;
print "Content-type: image/gif", "\n\n";

$max_length = 150;
$center = $radius = $max_length / 2;
@origin = ($center, $center);
$marker = 5;
$hour_segment = $radius * 0.50;
$minute_segment = $radius * 0.80;
$deg_to_rad = (atan2 (1, 1) * 4) / 180;

$image = new GD::Image ($max_length, $max_length);

$black = $image->colorAllocate (0, 0, 0);
$red = $image->colorAllocate (255, 0, 0);
$green = $image->colorAllocate (0, 255, 0);
$blue = $image->colorAllocate (0, 0, 255);

($seconds, $minutes, $hour) = localtime (time);
$hour_angle = ($hour + ($minutes / 60) - 3) * 30 * $deg_to_rad;
$minute_angle = ($minutes + ($seconds / 60) - 15) * 6 * $deg_to_rad;

$image->arc (@origin, $max_length, $max_length, 0, 360, $blue);

for ($loop=0; $loop < 360; $loop = $loop + 30) {
	local ($degrees) = $loop * $deg_to_rad;

	$image->line (  $origin[0] + (($radius - $marker) * cos ($degrees)),
			$origin[1] + (($radius - $marker) * sin ($degrees)),
			$origin[0] + ($radius * cos ($degrees)),
			$origin[1] + ($radius * sin ($degrees)),
			$red  );
}

$image->line (	@origin, 
		$origin[0] + ($hour_segment * cos ($hour_angle)),
		$origin[1] + ($hour_segment * sin ($hour_angle)),
		$green  );
				
$image->line (	@origin,
		$origin[0] + ($minute_segment * cos ($minute_angle)),
		$origin[1] + ($minute_segment * sin ($minute_angle)),
		$green  );

$image->arc (@origin, 6, 6, 0, 360, $red);
$image->fill ($origin[0] + 1, $origin[1] + 1, $red);
print $image->gif;

exit(0);