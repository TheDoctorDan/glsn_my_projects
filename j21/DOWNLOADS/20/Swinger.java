import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*; 

public class Swinger extends JFrame {

    public Swinger() {
        super("Swinger");

        String note = "I receive a disproportionate amount of " +
            "joy from being clicked. Please interact with me.";
        JButton hotButton = new JButton(note);

        JPanel pane = new JPanel();
        pane.add(hotButton);

        setContentPane(pane);
    }

    public static void main(String[] args) {
        JFrame frame = new Swinger();

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
