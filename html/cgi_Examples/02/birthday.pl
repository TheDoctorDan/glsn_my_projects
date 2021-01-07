#!/usr/local/bin/perl

$size_of_form_information = $ENV{'CONTENT_LENGTH'};
read (STDIN, $form_info, $size_of_form_information);

$form_info =~ s/%([\dA-Fa-f][\dA-Fa-f])/pack ("C", hex ($1))/eg;

($field_name, $birthday) = split (/=/, $form_info);

print "Content-type: text/plain", "\n\n";
print "Hey, your birthday is on: $birthday. That's what you told me, right?", "\n";

exit (0);
