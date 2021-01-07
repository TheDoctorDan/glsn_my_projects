import java.awt.*;

public class Alphabet extends java.applet.Applet {
    Button a = new Button("Alibi");
    Button b = new Button("Burglar");
    Button c = new Button("Corpse");
    Button d = new Button("Deadbeat");
    Button e = new Button("Evidence");
    Button f = new Button("Fugitive");
    FlowLayout lm = new FlowLayout(FlowLayout.LEFT);

    public void init() {
        setLayout(lm);
        add(a);
        add(b);
        add(c);
        add(d);
        add(e);
        add(f);
    }
}
