require "sys/socket.ph";

$sockaddr = 'S n a4 x8';

$remote_host = "ruby.ora.com";
$remote_port_number = 5000;

chop ($hostname = `hostname`);
($name, $aliases, $protocol) = getprotobyname ('tcp');

($name, $aliases, $type, $length, $current_address) = 
	gethostbyname($hostname);

($name, $aliases, $type, $length, $remote_address) = 
	gethostbyname($remote_host);

$current_port = pack($sockaddr, &AF_INET, 0, $current_address);
$remote_port = pack($sockaddr, &AF_INET, $remote_port_number, $remote_address);

socket (CONNECTION, &PF_INET, &SOCK_STREAM, $protocol) ||
	die "Cannot create socket.\n";
bind (CONNECTION, $current_port) || die "Cannot bind socket.\n";
connect (CONNECTION, $remote_port) || die "Cannot connect socket.\n";

select (CONNECTION);
$| = 1;
print "$ARGV[0]", "\n";
print "quit", "\n";

select (STDOUT);
while (<CONNECTION>) {
	if (!/^(\+|\|)|(Thanks for)/) {
		print;
	} 
}

exit(0);


