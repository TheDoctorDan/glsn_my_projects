import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class WellAdjusted extends JFrame implements AdjustmentListener {
    BorderLayout bord = new BorderLayout();
    JTextField value = new JTextField();
    JScrollBar bar = new JScrollBar(SwingConstants.HORIZONTAL,
        50, 10, 0, 100);

    public WellAdjusted() {
        super("Well Adjusted");

        bar.addAdjustmentListener(this);
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setEditable(false);
        JPanel pane = new JPanel();
        pane.setLayout(bord);
        pane.add(value, "South");
        pane.add(bar, "Center");

        setContentPane(pane);
    }

    public static void main(String[] args) {
        JFrame frame = new WellAdjusted();

        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(l);

        frame.pack();
        frame.setVisible(true);
    }

    public void adjustmentValueChanged(AdjustmentEvent evt) {
        Object source = evt.getSource();
        if (source == bar) {
            int newValue = bar.getValue();
            value.setText("" + newValue);
        }
        repaint();
    }
}
