import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*; 

public class Framework extends JFrame {

    public Framework() {
        super("Application Title");

        // Add components here
    }

    public static void main(String[] args) {
        JFrame frame = new Framework();

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(l);

        frame.pack();
        frame.setVisible(true);
    }
}
