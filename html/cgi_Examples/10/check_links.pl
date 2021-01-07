sub check_url 
{
	local ($url) = @_;
	local ($current_host, $host, $service, $file, $first_line);

	if (($host, $service, $file) = 
		($url =~ m|http://([^/:]+):{0,1}(\d*)(\S*)$|)) {

		chop ($current_host = `/bin/hostname`);
		$host = $current_host if ($host eq "localhost");

		$service = "http" unless ($service);
		$file = "/"	  unless ($file);

		&open_connection (HTTP, $host, $service) || return (0);
	
		print HTTP "HEAD $file HTTP/1.0", "\n\n";

		chop ($first_line = <HTTP>);

		if ($first_line =~ /200/) {
			return (1);
		} else {
			return (0);
		}

		close (HTTP);
	} else {
		return (0);
	}
}
