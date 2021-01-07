import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 

public class SwingColorTest extends JFrame {
    SwingColorControls RGBcontrols, HSBcontrols;
    JPanel swatch;

    public SwingColorTest() {
        super("Color Test");

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 3, 5, 15));
        swatch = new JPanel();
        swatch.setBackground(Color.black);
        RGBcontrols = new SwingColorControls(this, "Red",
            "Green", "Blue");
        HSBcontrols = new SwingColorControls(this, "Hue",
            "Saturation", "Brightness");
        pane.add(swatch);
        pane.add(RGBcontrols);
        pane.add(HSBcontrols);

        setContentPane(pane);
    }

    public static void main(String[] args) {
        JFrame frame = new SwingColorTest();

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(l);

        frame.pack();
        frame.setVisible(true);
    }

    public Insets getInsets() {
        return new Insets(10, 10, 10, 10);
    }
}

class SwingColorControls extends JPanel {
    SwingColorTest frame;
    JTextField tfield1, tfield2, tfield3;

    SwingColorControls(SwingColorTest parent,
        String l1, String l2, String l3) {

        frame = parent;
        setLayout(new GridLayout(3,2,10,10));
        tfield1 = new JTextField("0");
        tfield2 = new JTextField("0");
        tfield3 = new JTextField("0");
        add(new JLabel(l1, JLabel.RIGHT));
        add(tfield1);
        add(new JLabel(l2, JLabel.RIGHT));
        add(tfield2);
        add(new JLabel(l3, JLabel.RIGHT));
        add(tfield3);
    }

    public Insets getInsets() {
        return new Insets(10, 10, 0, 0);
    }
}
