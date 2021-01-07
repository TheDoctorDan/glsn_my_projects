import java.awt.*;

public class ColorTest extends java.applet.Applet {
    ColorControls RGBcontrols, HSBcontrols;
    Canvas swatch;

    public void init() {
        setLayout(new GridLayout(1, 3, 5, 15));
        swatch = new Canvas();
        swatch.setBackground(Color.black);
        RGBcontrols = new ColorControls(this, "Red",
            "Green", "Blue");
        HSBcontrols = new ColorControls(this, "Hue",
            "Saturation", "Brightness");
        add(swatch);
        add(RGBcontrols);
        add(HSBcontrols);
    }

    public Insets getInsets() {
        return new Insets(10, 10, 10, 10);
    }

    void update(ColorControls controlPanel) {
        int value1 = Integer.parseInt(controlPanel.tfield1.getText());
        int value2 = Integer.parseInt(controlPanel.tfield2.getText());
        int value3 = Integer.parseInt(controlPanel.tfield3.getText());
        Color c;
        if (controlPanel == RGBcontrols) {  // RGB has changed, update HSB
            c = new Color(value1, value2, value3);
            float[] HSB = Color.RGBtoHSB(value1, value2,
                value3, (new float[3]));
            HSB[0] *= 360;
            HSB[1] *= 100;
            HSB[2] *= 100;
            HSBcontrols.tfield1.setText(String.valueOf((int)HSB[0]));
            HSBcontrols.tfield2.setText(String.valueOf((int)HSB[1]));
            HSBcontrols.tfield3.setText(String.valueOf((int)HSB[2]));
        } else {  // HSB has changed, update RGB
            c = Color.getHSBColor((float)value1 / 360,
                (float)value2 / 100, (float)value3 / 100);
            RGBcontrols.tfield1.setText(String.valueOf(c.getRed()));
            RGBcontrols.tfield2.setText(String.valueOf(c.getGreen()));
            RGBcontrols.tfield3.setText(String.valueOf(c.getBlue()));
        }
        swatch.setBackground(c);
        swatch.repaint();
    }
}
