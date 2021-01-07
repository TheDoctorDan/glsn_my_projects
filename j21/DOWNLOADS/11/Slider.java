import java.awt.*;

public class Slider extends java.applet.Applet {
    GridLayout gl = new GridLayout(1,1);
    Scrollbar bar = new Scrollbar(Scrollbar.HORIZONTAL,
    50,0,1,100);

    public void init() {
        setLayout(gl);
        add(bar);
    }
}
