require "sys/socket.ph";

$log_file = "webster.log";
$server_manager = "Shishir Gundavaram";
$server_email = "shishir@ora.com";

$sockaddr = 'S n a4 x8';
chop ($hostname = `hostname`);
($name, $aliases, $protocol) = getprotobyname ('tcp');
$port_number = 5000;
$port = pack ($sockaddr, &AF_INET, $port_number, "\0\0\0\0");

$domains = "ora.com$";

socket (WEBSTER, &PF_INET, &SOCK_STREAM, $protocol) 
	|| die "Cannot create socket.\n";
bind (WEBSTER, $port) || die "Cannot bind socket.\n";
listen (WEBSTER, $max_connect) || die "Cannot listen to socket.\n";

while (1) {
	accept (CONNECTION, WEBSTER) || die "Cannot accept socket.\n";

	select (CONNECTION);
	$| = 1;

	if ($pid = fork) {
		waitpid ($pid, 0);
	} else {
		if (fork) {
			exit(0);
		} else {
			$pack_address = getpeername(CONNECTION);
			($af_inet, $port, $address) = 
				unpack($sockaddr, $pack_address);
			$ip_address = join(".", unpack("C4",$address));
			$ip_name = gethostbyaddr($address, 2);

			&log_connection();

			if ($ip_name !~ /$domains/i) {
				&unauthorized_connection();
			} else {
				&authorized_connection();
			}

			close (CONNECTION);
			exit(0);
		}
	}
}

exit(0);

sub log_connection
{
	local ($exclusive_lock, $unlock_lock) = (2, 8);
	local ($date);

	chop ($date = `date`);

	open (LOG, ">>". $log_file);
	flock (LOG, $exclusive_lock);

	print LOG "- $ip_name ($ip_address) at $date", "\n";

	flock (LOG, $unlock_lock);
	close (LOG);
}

sub welcome_message
{
	local ($date, $remote_string, $remote_info, $date_string);

	chop ($date = `date`);		
	$remote_string = "$ip_name ($ip_address)";
	$remote_info = pack("A46", $remote_string);
	$date_string = pack("A46", $date);

	print <<End_of_Welcome;

+------------------------------------------------------+
| Dictionary Server, Copyright 1995 Shishir Gundavaram |
| Last modified: July 23, 1995, All Rights Reserved.   |
+------------------------------------------------------+
| From: $remote_info |
| Date: $date_string |
+------------------------------------------------------+
| You can type in a word to look up, and "exit" or     |
| "quit" to close connection.                          |
+------------------------------------------------------+

End_of_Welcome

}

sub unauthorized_connection
{
	print <<End_of_Sorry;

Sorry...

Currently, only users in certain domains are enabled
to use this server. Again, sorry for the inconvenience.

$server_manager
$server_email

End_of_Sorry

}

sub authorized_connection
{
	local ($word);

	&welcome_message();

	while (1) {
		$word = <CONNECTION>;
		$word =~ s/[\000-\037 ]//g;
	
		if ($word =~ /\W/) {
			if ($word =~ /[`\!;\|\*\$&<>]/) {
				&malicious_user();
				last;
			} else {
				print "No alphanumeric symbols allowed.\n\n";
			}
		} else {
			if (($word =~ /^quit$/i) || ($word =~ /^exit$/i)) {
				print "\nThanks for using this service!\n\n";
				last;
			} elsif ($word ne "") {
				print `webster $word`;
			}
		} 
	}

	# last resumes here
}

sub malicious_user
{
	print <<End_of_Warning;

Whar are you trying to do? That's very rude!
I hope you were not trying to execute system commands.
If your intent was to break in, please do not ever
visit this server again!!

End_of_Warning
}

