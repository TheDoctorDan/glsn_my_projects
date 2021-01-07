import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*; 
 
public class UnaButton extends JFrame {

    public UnaButton() {
        super("The UnaButton");

        ImageIcon una = new ImageIcon("unabom.gif");
        JButton button = new JButton(una);

        JPanel pane = new JPanel();
        pane.add(button);

        setContentPane(pane);
    }

    public static void main(String[] args) {
        JFrame frame = new UnaButton();

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
