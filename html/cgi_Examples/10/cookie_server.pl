#!/usr/local/bin/perl

require "sockets.pl";

srand (time|$$);

$HTTP_server = "128.197.27.7";
$separator = "\034";
$expire_time = 15 * 60;

%DATA = ();
$max_cookies = 10;
$no_cookies = 0;

$error = 500;
$success = 200;

$port = 5000;
&listen_to_port (SOCKET, $port) || die "Cannot create socket.", "\n";

while (1) {
    ( ($ip_name, $ip_address) = &accept_connection (COOKIE, SOCKET) )
	|| die "Could not accept connection.", "\n";

    select (COOKIE);
    $cookie = undef;
	
    if ($ip_address ne $HTTP_server) {
	&print_status ($error, "You are not allowed to connect to server.");
    } else {
	&print_status ($success, "Welcome from $ip_name ($ip_address)");

	while (<COOKIE>) {
	    s/[\000-\037]//g;
	    s/^\s*(.*)\b\s*/$1/;

	    if ( ($remote_address) = /^new\s*(\S+)$/) {
		if ($cookie) {
		    &print_status ($error, "You already have a cookie!");
		} else {
		    if ($no_cookies >= $max_cookies) {
			&print_status ($error, "Cookie limit reached.");
		    } else {
			do {
			    $cookie = &generate_new_cookie ($remote_address);
			} until (!$DATA{$cookie});

			$no_cookies++;
			$DATA{$cookie} = join("::", $remote_address,
					            $cookie, time);
			&print_status ($success, $cookie);
		    }
		}

	    } elsif ( ($check_cookie, $remote_address) = 
			/^cookie\s*(\S+)\s*(\S+)/) {

		if ($cookie) {
		    &print_status ($error, "You already specified a cookie.");
		} else {
		    if ($DATA{$check_cookie}) {
			($old_address) = split(/::/, $DATA{$check_cookie});
							 
			if ($old_address ne $remote_address) {
			    &print_status ($error, "Incorrect IP address.");
			} else {
			    $cookie = $check_cookie;
			    &print_status ($success, "Cookie $cookie set.");
			}
		    } else {
			&print_status ($error, "Cookie does not exist.");
		    }
		}

	    } elsif ( ($variable, $value) = /^(\w+)\s*=\s*(.*)$/) {
		if ($cookie) {
		    $key = join ($separator, $cookie, $variable);
		    $DATA{$key} = $value;
		    &print_status ($success, "$variable=$value");
		} else {
		    &print_status ($error, "You must specify a cookie.");
		}

	    } elsif (/^list$/) {
		if ($cookie) {
		    foreach $key (keys %DATA) {
			$string = join ("", $cookie, $separator);

			if ( ($variable) = $key =~ /^$string(.*)$/) {
			    &print_status ($success, "$variable=$DATA{$key}");
			}
		    }
		    print ".", "\n";

		} else {
		    &print_status ($error, "You don't have a cookie yet.");
		}

	    } elsif (/^delete$/) {
		if ($cookie) {
		    &remove_cookie ($cookie);
		    &print_status ($success, "Cookie $cookie deleted.");
		} else {
		    &print_status ($error, "Select a cookie to delete.");
		}

	    } elsif (/^exit|quit$/) {
		$cookie = undef;
		&print_status ($success, "Bye.");
		last;
	    } elsif (!/^\s*$/) {
		&print_status ($error, "Invalid command.");
	    }
	}
    }

    &close_connection (COOKIE);

    &expire_old_cookies();
}

exit(0);

sub print_status
{
    local ($status, $message) = @_;

    print $status, ": ", $message, "\n";
}

sub generate_new_cookie
{
    local ($remote) = @_;
    local ($random, $temp_address, $cookie_string, $new_cookie);

    $random = rand (time);
    ($temp_address = $remote) =~ s/\.//g;
    $cookie_string = join ("", $temp_address, time) / $random;
    $new_cookie = crypt ($cookie_string, $random);

    return ($new_cookie);
}

sub expire_old_cookies
{
    local ($current_time, $key, $cookie_time);

    $current_time = time;

    foreach $key (keys %DATA) {
	if ($key !~ /$separator/) {
	    $cookie_time = (split(/::/, $DATA{$key}))[2];
	    if ( $current_time >= ($cookie_time + $expire_time) ) {
		&remove_cookie ($key);
	    }
	}
    }
}
	
sub remove_cookie
{
    local ($cookie_key) = @_;
    local ($key, $exact_cookie);

	$exact_cookie = (split(/::/, $DATA{$cookie_key}))[1];
	
    foreach $key (keys %DATA) {
	if ($key =~ /$exact_cookie/) {
	    delete $DATA{$key};
	}
    }

    $no_cookies--;
}


	
