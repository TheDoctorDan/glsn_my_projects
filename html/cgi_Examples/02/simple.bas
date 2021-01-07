Sub CGI_Main ()
      Send ("Content-type: text/plain")
      Send ("")
      Send ("Server Name")
      Send ("")
      Send ("The server name is: " & CGI_ServerName)
End Sub