import java.io.*;
import java.net.*;

public class Trivia {
    private static final int PORTNUM = 1234;

    public static void main(String[] arguments) {
        Socket socket = null;
        InputStreamReader isr = null;
        BufferedReader in = null;
        PrintWriter out = null;
        String address;

        // Check the command-line args for the host address
        if (arguments.length != 1) {
            System.out.println("Usage: java Trivia <address>");
            return;
        }
        else
            address = arguments[0];

        // Initialize the socket and streams
        try {
            socket = new Socket(address, PORTNUM);
            isr = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(isr);
            out = new PrintWriter(socket.getOutputStream(),true);
        }
        catch (IOException e) {
            System.err.println("Exception: couldn't create stream socket "
                + e.getMessage());
            System.exit(1);
        }

        // Process user input and server responses
        try {
            StringBuffer str = new StringBuffer(128);
            String inStr;
            int c;

            while ((inStr = in.readLine()) != null) {
                System.out.println("Server: " + inStr);
                if (inStr.equals("Bye."))
                    break;
                while ((c = System.in.read()) != '\n')
                    str.append((char)c);
                System.out.println("Client: " + str);
                out.println(str.toString());
                out.flush();
                str.setLength(0);
            }
            // Cleanup
            out.close();
            in.close();
            socket.close();
        }
        catch (IOException e) {
            System.err.println("I/O error: "+ e.toString());
        }
    }
}
