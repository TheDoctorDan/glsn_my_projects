Sub CGI_Main ()

    Dim loop as Integer
    Dim message as String
    Open "survey.dat" for APPEND as #1

    Print #1, "Results from " + CGI_RemoteHost
    Print #1, "-----< Start of Data >-----"

    For loop = 0 to CGI_NumFormTuples - 1
        Select Case CGI_FormTuples(loop).key
            Case "name":
                message = "Subject name: "
            Case "regular":
                message = "Regular exercise: "
            Case "why":
                message = "Reason for exercise: "
            Case "sports":
                message = "Primarily participates in: "
            Case "interval":
                message = "Exercise frequency: "
        End Select	
        Print #1, message & CGI_FormTuples(loop).value
    Next loop


    Print #1, "-----< End of Data >-----"
    Close #1

    Send ("Content-type: text/html")
    Send ("")
    Send ("<TITLE>Thanks for filling out the survey!</TITLE>")
    Send ("<H1>Thank You!</H1>")
    Send ("<HR>")
    Send ("Thanks for taking the time to fill out the form.")
    Send ("We really appreciate it!")
End Sub

