import java.awt.*;

public class CheckACzech extends java.applet.Applet {
    Checkbox c1 = new Checkbox("Milos Forman");
    Checkbox c2 = new Checkbox("Paulina Porizkova");
    Checkbox c3 = new Checkbox("Ivan Reitman");
    Checkbox c4 = new Checkbox("Tom Stoppard");
    Checkbox c5 = new Checkbox("Ivana Trump");

    public void init() {
        add(c1);
        c2.setState(true);
        add(c2);
        add(c3);
        add(c4);
        add(c5);
    }
}
