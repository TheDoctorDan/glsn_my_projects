#!/bin/csh

echo "Content-type: text/plain"
echo ""

if ($?QUERY_STRING) then
    set command = `echo $QUERY_STRING | awk 'BEGIN {FS = "="} { print $2 }'`

    if ($command == "fortune") then
        /usr/local/bin/fortune
    else if ($command == "finger") then
        /usr/ucb/finger
    else 
        /usr/local/bin/date
    endif

else
    /usr/local/bin/date
endif
