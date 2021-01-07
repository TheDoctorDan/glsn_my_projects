require "/usr/local/lib/perl/sys/socket.ph";

$sockaddr = 'S n a4 x8';

chop ($hostname = `hostname`);
($name, $aliases, $protocol) = getprotobyname ('tcp');
$port_number = 5000;
$port = pack ($sockaddr, &AF_INET, $port_number, "\0\0\0\0");

socket (FORTUNE, &PF_INET, &SOCK_STREAM, $protocol) 
	|| die "Cannot create socket.\n";
bind (FORTUNE, $port) || die "Cannot bind socket.\n";
listen (FORTUNE, 5) || die "Cannot listen to socket.\n";

for (;;) {
	accept (CONNECTION, FORTUNE) || die "Cannot accept socket.\n";

	select (CONNECTION);
	$| = 1;
	
	$pack_address = getpeername(CONNECTION);
	($af_inet, $port, $address) = unpack($sockaddr, $pack_address);
	$ip_address = join(".", unpack("C4", $address));
	$ip_name = gethostbyaddr($address, 2);
	
	$welcome = "Welcome from: $ip_name ($ip_address)";
	$center = int ((80 - length($welcome)) / 2);

	print "\n", " " x $center, $welcome, "\n";
	print "-" x 80, "\n";
	print `fortune`;
	print  "-" x 80, "\n";

	$date = `date`;
	$end = "Fortune Server by Shishir Gundavaram - $date";
	$end_center = int ((80 - length($end)) / 2);
	print " " x $end_center, $end, "\n";

	close (CONNECTION);
}

exit(0);

	



