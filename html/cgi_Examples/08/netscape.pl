sub parse_client_cookies
{
    local (*COOKIE_DATA) = @_;
    
    local (@key_value_pairs, $key_value, $key, $value);

    @key_value_pairs = split (/;\s/, $ENV{'HTTP_COOKIE'});

    foreach $key_value (@key_value_pairs) {
        ($key, $value) = split (/=/, $key_value);

        $key   =~ tr/+/ /;
        $value =~ tr/+/ /;

        $key   =~ s/%([\dA-Fa-f][\dA-Fa-f])/pack ("C", hex ($1))/eg;
        $value =~ s/%([\dA-Fa-f][\dA-Fa-f])/pack ("C", hex ($1))/eg;

        if (defined($FORM_DATA{$key})) {
            $FORM_DATA{$key} = join ("\0", $FORM_DATA{$key}, $value);
        } else {
            $FORM_DATA{$key} = $value;
        }
    }
}
