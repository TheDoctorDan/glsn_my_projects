$SIG{'CHLD'} = "wait_for_child_to_die";

while (1) {
    ( ($ip_name, $ip_address) = &accept_connection (COOKIE, SOCKET) )
        || die "Could not accept connection.", "\n";
    
    if (fork) {
        #
        # Parent Process (do almost nothing here)
        #
    } else {
        #
        # Child Process (do almost everything here)
        #
    }

    &close_connection (COOKIE);    
}

sub wait_for_child_to_die
{
    wait;
}
