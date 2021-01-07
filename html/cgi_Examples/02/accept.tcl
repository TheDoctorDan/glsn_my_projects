#!/usr/local/bin/tclsh

puts "Content-type: text/plain\n"

set http_accept $env(HTTP_ACCEPT)
set browser $env(HTTP_USER_AGENT)

puts "Here is a list of the MIME types that the client, which"
puts "happens to be $browser, can accept:\n"

set mime_types [split $http_accept ,]

foreach type $mime_types {
    puts "- $type"
}

exit 0