#!/usr/local/bin/gdtcl

puts "Content-type: image/gif\n"

set font_height 16
set font_length 8
set color $env(WWW_color)

if {[info exists env(WWW_message)]} {
    set message $env(WWW_message)
} else {
    set message "This is an example of $color text"
}

set message_length  [string length $message]
set x  [expr $message_length * $font_length]
set y  $font_height

set image  [gd create $x $y]
set white  [gd color new $image 255 255 255]

if {[string compare $color "Red"] == 0} {
    set color_index [list 255 0 0]
} elseif {[string compare $color "Blue"] == 0} {        
    set color_index [list 0 0 255]
} elseif {[string compare $color "Green"] == 0} {
    set color_index [list 0 255 0]
} elseif {[string compare $color "Yellow"] == 0} {
    set color_index [list 255 255 0]
} elseif {[string compare $color "Orange"] == 0} {
    set color_index [list 255 165 0]
} elseif {[string compare $color "Purple"] == 0} {
    set color_index [list 160 32 240]
} elseif {[string compare $color "Brown"] == 0} {
    set color_index [list 165 42 42]
} elseif {[string compare $color "Black"] == 0} {
    set color_index [list 0 0 0]
}

set selected_color  [gd color new $image $color_index]
gd color transparent $image $white
gd text $image $selected_color large 0 0 $env(WWW_message)
gd writeGIF $image stdout   

flush stdout
gd destroy $image  

exit 0

