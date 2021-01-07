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

    void update(SwingColorControls controlPanel) {
        Color c;
        // get string values from text fields, convert to ints
        int value1 = Integer.parseInt(controlPanel.tfield1.getText());
        int value2 = Integer.parseInt(controlPanel.tfield2.getText());
        int value3 = Integer.parseInt(controlPanel.tfield3.getText());

        if (controlPanel == RGBcontrols) {
            // RGB has changed, update HSB
            c = new Color(value1, value2, value3);

            // convert RGB values to HSB values
            float[] HSB = Color.RGBtoHSB(value1, value2, value3,
                (new float[3]));
            HSB[0] *= 360;
            HSB[1] *= 100;
            HSB[2] *= 100;

            // reset HSB fields
            HSBcontrols.tfield1.setText(String.valueOf((int)HSB[0]));
            HSBcontrols.tfield2.setText(String.valueOf((int)HSB[1]));
            HSBcontrols.tfield3.setText(String.valueOf((int)HSB[2]));

        } else {
            // HSB has changed, update RGB
            c = Color.getHSBColor((float)value1 / 360,
                (float)value2 / 100, (float)value3 / 100);

            // reset RGB fields
            RGBcontrols.tfield1.setText(String.valueOf(c.getRed()));
            RGBcontrols.tfield2.setText(String.valueOf(c.getGreen()));
            RGBcontrols.tfield3.setText(String.valueOf(c.getBlue()));
        }

        // update swatch
        swatch.setBackground(c);
        swatch.repaint();
    }
}

class SwingColorControls extends JPanel
    implements ActionListener, FocusListener {

    SwingColorTest frame;
    JTextField tfield1, tfield2, tfield3;

    SwingColorControls(SwingColorTest parent,
        String l1, String l2, String l3) {

        frame = parent;
        setLayout(new GridLayout(3,2,10,10));
        tfield1 = new JTextField("0");
        tfield2 = new JTextField("0");
        tfield3 = new JTextField("0");
        tfield1.addFocusListener(this);
        tfield2.addFocusListener(this);
        tfield3.addFocusListener(this);
        tfield1.addActionListener(this);
        tfield2.addActionListener(this);
        tfield3.addActionListener(this);
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

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JTextField)
            frame.update(this);
    }

    public void focusLost(FocusEvent evt) {
        frame.update(this);
    }

    public void focusGained(FocusEvent evt) { }
}
