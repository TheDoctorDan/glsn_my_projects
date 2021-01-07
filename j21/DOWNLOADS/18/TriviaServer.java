import java.io.*;
import java.net.*;
import java.util.Random;

public class TriviaServer extends Thread {
    private static final int PORTNUM = 1234;
    private static final int WAITFORCLIENT = 0;
    private static final int WAITFORANSWER = 1;
    private static final int WAITFORCONFIRM = 2;
    private String[] questions;
    private String[] answers;
    private ServerSocket serverSocket;
    private int numQuestions;
    private int num = 0;
    private int state = WAITFORCLIENT;
    private Random rand = new Random();

    public TriviaServer() {
        super("TriviaServer");
        try {
            serverSocket = new ServerSocket(PORTNUM);
            System.out.println("TriviaServer up and running ...");
        }
        catch (IOException e) {
            System.err.println("Exception: couldn't create socket");
            System.exit(1);
        }
    }

    public static void main(String[] arguments) {
        TriviaServer server = new TriviaServer();
        server.start();
    }

    public void run() {
        Socket clientSocket = null;

        // Initialize the arrays of questions and answers
        if (!initQnA()) {
            System.err.println("Error: couldn't initialize questions and answers");
            return;
        }

        // Look for clients and ask trivia questions
        while (true) {
            // Wait for a client
            if (serverSocket == null)
                return;
            try {
                clientSocket = serverSocket.accept();
            }
            catch (IOException e) {
                System.err.println("Exception: couldn't connect to client socket");
                System.exit(1);
            }

            // Perform the question/answer processing
            try {
                InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader is = new BufferedReader(isr);
                PrintWriter os = new PrintWriter(new
                    BufferedOutputStream(clientSocket.getOutputStream()), false);
                String outLine;

                // Output server request
                outLine = processInput(null);
                os.println(outLine);
                os.flush();

                // Process and output user input
                while (true) {
                    String inLine = is.readLine();
                    if (inLine.length() > 0) {
                        outLine = processInput(inLine);
                        os.println(outLine);
                        os.flush();
                        if (outLine.equals("Bye."))
                            break;
                    }
                }

                // Cleanup
                os.close();
                is.close();
                clientSocket.close();
            }
            catch (Exception e) {
                System.err.println("Exception: " + e);
                e.printStackTrace();
            }
        }
    }

    private boolean initQnA() {
        try {
            File inFile = new File("QnA.txt");
            FileInputStream inStream = new FileInputStream(inFile);
            byte[] data = new byte[(int)inFile.length()];

            // Read the questions and answers into a byte array
            if (inStream.read(data) <= 0) {
                System.err.println("Error: couldn't read questions and answers");
                return false;
            }

            // See how many question/answer pairs there are
            for (int i = 0; i < data.length; i++)
                if (data[i] == (byte)'\n')
                    numQuestions++;
            numQuestions /= 2;
            questions = new String[numQuestions];
            answers = new String[numQuestions];

            // Parse the questions and answers into arrays of strings
            int start = 0, index = 0;
            boolean isQ = true;
            for (int i = 0; i < data.length; i++)
                if (data[i] == (byte)'\n') {
                    if (isQ) {
                        questions[index] = new String(data, start, i - start - 1);
                        isQ = false;
                    }
                    else {
                        answers[index] = new String(data, start, i - start - 1);
                        isQ = true;
                        index++;
                    }
                start = i + 1;
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("Exception: couldn't find the question file");
            return false;
        }
        catch (IOException e) {
            System.err.println("Exception: I/O error trying to read questions");
            return false;
        }

        return true;
    }

    String processInput(String inStr) {
        String outStr = null;

        switch (state) {
            case WAITFORCLIENT:
                // Ask a question
                outStr = questions[num];
                state = WAITFORANSWER;
                break;

            case WAITFORANSWER:
                // Check the answer
                if (inStr.equalsIgnoreCase(answers[num]))
                    outStr = "That's correct! Want another? (y/n)";
                else
                    outStr = "Wrong, the correct answer is " + answers[num] +
                        ". Want another? (y/n)";
                state = WAITFORCONFIRM;
                break;

            case WAITFORCONFIRM:
                // See if they want another question
                if (inStr.equalsIgnoreCase("Y")) {
                    num = Math.abs(rand.nextInt()) % questions.length;
                    outStr = questions[num];
                    state = WAITFORANSWER;
                }
                else {
                    outStr = "Bye.";
                    state = WAITFORCLIENT;
                }
                break;
        }
        return outStr;
    }
}
