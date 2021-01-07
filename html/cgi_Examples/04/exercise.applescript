set survey_file to "Macintosh HD:survey.dat"

survey_file = "Macintosh HD:survey.dat"

set crlf to (ASCII character 13) & (ASCII character 10)
set http_header to 	"HTTP/1.0 200 OK" & crlf & -
                    "Server: WebSTAR/1.0 ID/ACGI" & crlf & -
                    "MIME-Version: 1.0" & crlf & "Content-type: text/html" & -
                    crlf & crlf

on +event WWW=sdoc; path_args -
    given +class post;:post_args, +class add;:client_address

    set post_args_without_plus to dePlus post_args
    set decoded_post_args to Decode URL post_args_without_plus

    set name 	   to findNamedArgument(decoded_post_args, "name")
    set regular  to findNamedArgument(decoded_post_args, "regular")
    set why 	    to findNamedArgument(decoded_post_args, "why")
    set sports 	 to findNamedArgument(decoded_post_args, "sports")
    set interval to findNamedArgument(decoded_post_args, "interval")

    try
        set survey_file_handle to open file alias survey_file
        position file survey_file at (get file length survey_file)
    on error
        create file survey_file owner "ttxt"
        set survey_file_handle to open file alias survey_file
    end try

    set survey_output to "Results from " & client_address & crlf & -
                         "-----< Start of Data >-----" & crlf & -
                         "Subject name: " & name & crlf & -
                         "Regular exercise: " & regular & crlf & -
                         "Reason for exercise: " & why & crlf & -
                         "Primarily participates in: " & sports & crlf & -
                         "Exercise frequency: " & interval & crlf & -
                         "-----< End of Data >-----" & crlf

    write file survey_file_handle text survey_output
    close file survey_file_handle

    set thank_you to http_header & -
                "<TITLE>Thanks for filling out the survey!</TITLE>" & -
                "<H1>Thank You!</H1>" & "<HR>" & -
                "Thanks for taking the time to fill out the form." & -
                "We really appreciate it!"

    return thank_you
end +event WWW=sdoc;

on findNamedArgument(theText, theArg)
    try
        set oldDelims to AppleScript's text item delimiters
        set AppleScript's text item delimiters to "&"
        set numItems to (count of text items in theText)
        
        repeat with textCount from 1 to numItems
            set thisItem to text item textCount of theText
            try
                set AppleScript's text item delimiters to "="
				            set argName to (first text item of thisItem)
				            if argName = theArg then
                    set resItem to (second text item of thisItem)
                    exit repeat
				            else
					                set resItem to ""
				            end if
				            set AppleScript's text item delimiters to "&"
            on error
				            set AppleScript's text item delimiters to "&"
            end try
        end repeat
		
        set AppleScript's text item delimiters to oldDelims
    on error
        set AppleScript's text item delimiters to oldDelims
        set resItem to ""
    end try
    return resItem
end findNamedArgument

