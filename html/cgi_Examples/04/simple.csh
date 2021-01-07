#!/usr/local/bin/csh

echo "Content-type: text/plain"
echo ""

if ($?WWW_name) then
    echo "Hi $WWW_name -- Nice to meet you."
else
    echo "Don't want to tell me your name, huh?"
    echo "I know you are calling in from $REMOTE_HOST."
    echo ""
endif

if ($?WWW_age) then
    echo "You are $WWW_age years old."
else
    echo "Are you shy about your age?"
endif
echo ""

if ($?WWW_drink) then
    echo "You like:" `echo $WWW_drink | tr "#" " "`
else
    echo "I guess you don't like any fluids."
endif

exit(0)
