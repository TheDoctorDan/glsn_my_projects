import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class ChangeTitle extends JFrame implements ActionListener {
    JButton b1 = new JButton("Rosencrantz");
    JButton b2 = new JButton("Guildenstern");

    public ChangeTitle() {
        super("Title Bar");

        b1.addActionListener(this);
        b2.addActionListener(this);
        JPanel pane = new JPanel();
        pane.add(b1);
        pane.add(b2);

        setContentPane(pane);
    }

    public static void main(String[] args) {
        JFrame frame = new ChangeTitle();

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(l);

        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == b1)
            setTitle("Rosencrantz");
        else if (source == b2)
            setTitle("Guildenstern");
        repaint();
    }
}
