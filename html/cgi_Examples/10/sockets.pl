#!/usr/local/bin/perl

##++
##     Socket Library v2.0
##     Last modified: November 23, 1995
##
##     Copyright (c) 1995 by Shishir Gundavaram
##     All Rights Reserved
##
##     E-Mail: shishir@ora.com
##
##     Permission  to  use,  copy, and distribute is hereby granted,
##     providing that the above copyright notice and this permission
##     appear in all copies and in supporting documentation.
##--

require "sys/socket.ph";

sub open_connection
{	
	local ($socket, $remote_host, $remote_service) = @_;
	local ($current_host, $current_address, $remote_address,
	       $remote_port_number, $current_port, $remote_port, $protocol);

	$current_host = &get_current_host ();
	$current_address = &get_packed_address ($current_host);

	$remote_address = &get_packed_address ($remote_host);
	$remote_port_number = &get_port_number ($remote_service);

	$current_port = &create_socket_structure (0, $current_address);
	$remote_port  = &create_socket_structure ($remote_port_number,
						  $remote_address);

	$protocol = (getprotobyname ("tcp"))[2];

	socket  ($socket, &AF_INET, &SOCK_STREAM, $protocol) || return (0);
	bind    ($socket, $current_port)                     || return (0);
	connect ($socket, $remote_port)                      || return (0);

	&unbuffer_socket ($socket);

	return (1);
}

sub listen_to_port
{
	local ($socket, $service_or_number) = @_;
	local ($current_host, $current_address, $protocol,
	       $specified_port_number, $specified_port);

	$current_host = &get_current_host ();
	$current_address = &get_packed_address ($current_host);

	$protocol = (getprotobyname ("tcp"))[2];

	if (defined ($service_or_number)) {
		$specified_port_number = &get_port_number ($service_or_number);
	} else {
		$specified_port_number = 0;
	}

	$specified_port = &create_socket_structure ($specified_port_number,
			 			    $current_address);

	socket  ($socket, &AF_INET, &SOCK_STREAM, $protocol) || return (0);
	bind    ($socket, $specified_port)		     || return (0);
	listen  ($socket, 5)				     || return (0);

	unless (defined ($service_or_number)) {
		$specified_port_number = &get_socket_port ($socket);
	}

	return ($specified_port_number);
}

sub accept_connection
{
	local ($connection, $socket) = @_;
	local ($IP_number, $IP_name);

	accept ($connection, $socket) || return (0);

	&unbuffer_socket ($connection);

	$IP_number = &where_from ($connection);
	$IP_name = &IP_number_to_name ($IP_number);

	return ($IP_name, $IP_number);
}

sub strip_control_chars
{
	local ($input) = @_;

	$input =~ s/[\000-\037]//g;
	return ($input);
}

sub close_connection
{
	local ($connection) = @_;

	&flush_socket ($connection);
	close ($connection);
}

############################################################################

sub get_current_host
{
	local ($host);

	unless ($host = `hostname`) {	
		$host = `uname -n`;
	}

        chop ($host);

	return ($host);
}

sub create_socket_structure 
{
	local ($port_number, $packed_address) = @_;
	local ($socket_template, $structure);

	$socket_template = "S n a4 x8";
	
	$structure = 
	     pack ($socket_template, &AF_INET, $port_number, $packed_address);

	return ($structure);
}

sub get_port_number
{
	local ($service_or_port) = @_;
	local ($port_number);

	if ($service_or_port !~ /^\d+$/) {
		$port_number = 
			(getservbyname ($service_or_port, "tcp"))[2];
	} else {
		$port_number = $service_or_port;
	}

	return ($port_number);
}

sub get_socket_port
{	
	local ($socket_handle) = @_;
	local ($socket_template, $socket_address, $port_number);

	$socket_template = "S n a4 x8";

	$socket_address = getsockname ($socket_handle);
	$port_number = (unpack ($socket_template, $socket_address))[1];

	return ($port_number);
}

sub unbuffer_socket
{
	local ($socket) = @_;
	local ($current_handle);
	
	$current_handle = select ($socket);
	$| = 1;
	select ($current_handle);
}

sub flush_socket
{
	local ($socket) = @_;
	local ($current_handle);

	##++
	##  Based on the code from the flush.pl library
	##--

	$current_handle = select ($socket);
	$| = 1;
	print "";
	$| = 0;
	select ($current_handle);
}

sub get_packed_address
{
	local ($IP_name_or_number) = @_;
	local ($packed_address, @decimal_address);

	if ($IP_name_or_number =~ /^\d+\.{3}\d+$/) {
		@decimal_address = split (/\./, $IP_name_or_number);
		$packed_address = pack ("C4", @decimal_address);
	} else {
		$packed_address = (gethostbyname ($IP_name_or_number))[4];
	}

	return ($packed_address);
}

sub packed_address_to_IP_number
{
	local ($packed_address) = @_;
	local (@decimal_address, $IP_number);

	@decimal_address = unpack ("C4", $packed_address);
	$IP_number = join (".", @decimal_address);

	return ($IP_number);
}

sub where_from
{
	local ($socket) = @_;
	local ($socket_template, $internet_address, $packed_address,
	       $IP_number);

	$socket_template = "S n a4 x8";
	$internet_address = getpeername ($socket);
	$packed_address = (unpack ($socket_template, $internet_address))[2];
	$IP_number = &packed_address_to_IP_number ($packed_address);

	return ($IP_number);
}

sub IP_number_to_name
{
	local ($IP_number) = @_;
	local ($packed_IP_address, $IP_name);

	$packed_IP_address = &get_packed_address ($IP_number);
	($IP_name) = gethostbyaddr ($packed_IP_address, 2);

	return ($IP_name);
}

sub IP_name_to_number
{
	local ($IP_name) = @_;
	local ($packed_IP_address, $IP_number);

	$packed_IP_address = &get_packed_address ($IP_name);
	$IP_number = &packed_address_to_IP_number ($packed_IP_address);

	return ($IP_number);
}

1;


