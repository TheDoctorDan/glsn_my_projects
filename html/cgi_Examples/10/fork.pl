#!/usr/local/bin/perl

$| = 1;
print "Content-type: text/plain", "\n\n";
print "We are about to create the child!", "\n";

if ($pid = fork) {
        print <<End_of_Parent;

I am the parent speaking. I have successfully created a child process.
The Process Identification Number (PID) of the child process is: $pid.

The child will be cleaning up all the files in the directory. It might
take a while, but you do not have to wait!

End_of_Parent
 
} else {
        close (STDOUT);
        
        system ("/usr/bin/rm", "-fr", "/tmp/CGI_test", "/var/tmp/CGI");
	exit (0);
}

print "I am the parent again! Now it is time to exit.", "\n";
print "My child process will work on its own! Good Bye!", "\n";

exit(0);
