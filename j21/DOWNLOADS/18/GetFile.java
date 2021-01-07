import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class GetFile extends Frame implements Runnable {
    Thread runner;
    URL page;
    TextArea box = new TextArea("Getting text ...");

    public GetFile() {
        super("Get File");
        add(box);
        try {
            page = new URL("http://www.prefect.com/java21/index.html");
        }
        catch (MalformedURLException e) {
            System.out.println("Bad URL: " + page);
        }
    }

    public static void main(String[] arguments) {
        GetFile frame = new GetFile();

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(l);

        frame.pack();
        frame.setVisible(true);
        if (frame.runner == null) {
            frame.runner = new Thread(frame);
            frame.runner.start();
        }
    }

    public void run() {
        URLConnection conn = null;
        InputStreamReader in;
        BufferedReader data;
        String line;
        StringBuffer buf = new StringBuffer();
        try {
            conn = this.page.openConnection();
            conn.connect();
            box.setText("Connection opened ...");
            in = new InputStreamReader(conn.getInputStream());
            data = new BufferedReader(in);
            box.setText("Reading data ...");
            while ((line = data.readLine()) != null) {
                buf.append(line + "\n");
            }
            box.setText(buf.toString());
        }
        catch (IOException e) {
            System.out.println("IO Error:" + e.getMessage());
        }
    }
}
