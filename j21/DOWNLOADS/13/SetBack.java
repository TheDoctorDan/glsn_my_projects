import java.awt.*;

public class SetBack extends java.applet.Applet {

    Button redButton,blueButton,greenButton,whiteButton,blackButton;

    public void init() {
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        redButton = new Button("Red");
        add(redButton);
        blueButton = new Button("Blue");
        add(blueButton);
        greenButton = new Button("Green");
        add(greenButton);
        whiteButton = new Button("White");
        add(whiteButton);
        blackButton = new Button("Black");
        add(blackButton);
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof Button) {
            changeColor((Button)evt.target);
            return true;
        } else return false;
    }

    void changeColor(Button b) {
        if (b == redButton) setBackground(Color.red);
        else if (b == blueButton) setBackground(Color.blue);
        else if (b == greenButton) setBackground(Color.green);
        else if (b == whiteButton) setBackground(Color.white);
        else setBackground(Color.black);

        repaint();
    }
}
