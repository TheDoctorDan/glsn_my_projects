import java.awt.Button;
import java.awt.Color;

public class Inner extends java.applet.Applet {
    Button b1 = new Button("One");
    BlueButton b2 = new BlueButton("Two");

    public void init() {
        add(b1);
        add(b2);
    }
    class BlueButton extends Button {
        BlueButton(String label) {
            super(label);
            this.setBackground(Color.blue);
        }
    }
}
