set webmaster {shishir@bu.edu}

proc return_error { status keyword message } {
    global webmaster

    puts "Content-type: text/html"
    puts "Status: $status $keyword\n"

    puts "<title>CGI Program - Unexpected Error</title>"
    puts "<H1>$keyword</H1>"
    puts "<HR>$message</HR>"

    puts "Please contact $webmaster for more information"
}

proc parse_form_data { form_info } {
    global env
    upvar $form_info FORM_DATA

    if {[string compare $request_method "POST"] == 0} {
        set query_string [read stdin $env(CONTENT_LENGTH)]
    } elseif {[string compare $request_method "GET"] == 0} {
        set query_string $env(QUERY_STRING)
    } else {
        return_error 500 {Server Error} {Server uses unsupported method}
        exit 1
    }

    set key_value_pairs [split $query_string &]


    foreach key_value $key_value_pairs {
        set pair [split $key_value =]
        set key [lindex $pair 0]
        set value [lindex $pair 1]

        regsub -all {\+} $value { } value

        while {[regexp {%[0-9A-Fa-f][0-9A-Fa-f]} $value matched]} {
            scan $matched "%%%x" hex
            set symbol [ctype char $hex]
            regsub -all $matched $value $symbol value
        }

        if {[info exists FORM_DATA($key)]} {
            append FORM_DATA($key) "\0" $FORM_DATA($key)
        } else {
            set FORM_DATA($key) $value
        }                
    }
}
