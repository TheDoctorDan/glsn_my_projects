set crlf to (ASCII character 13) & (ASCII character 10)
set http_header to "HTTP/1.0 200 OK" & crlf & -
         "Server: WebSTAR/1.0 ID/ACGI" & crlf & -
         "MIME-Version: 1.0" & crlf & "Content-type: text/html" & crlf & crlf
         
on +event WWW=sdoc; path_args -
   given +class kfor;:http_search_args, +class post;:post_args, +class meth;:method,
         +class addr;:client_address, +class user;:username, +class pass;:password, 
         +class frmu;:from_user, +class svnm;:server_name, +class svpt;:server_port,
         +class scnm;:script_name, +class ctyp;:content_type, +class refr;:referer,
         +class Agnt;:user_agent, +class Kact;:action, +class Kapt;:action_path,
        +class Kcip;:client_ip, +class Kfrq;:full_request
   
   set virtual_document to http_header & -
       "<H1>Server Software</H1><BR><HR>" & crlf -
       "The server that is responding to your request is: " & server_name & crlf -
       "<BR>" & crlf

   return virtual_document
end +event WWW=sdoc;
