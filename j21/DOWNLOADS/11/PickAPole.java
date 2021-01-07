import java.awt.*;

public class PickAPole extends java.applet.Applet {
    CheckboxGroup p = new CheckboxGroup();
    Checkbox p1 = new Checkbox("Samuel Goldwyn", p, false);
    Checkbox p2 = new Checkbox("Krzysztof Kieslowski", p, true);
    Checkbox p3 = new Checkbox("Klaus Kinski", p, false);
    Checkbox p4 = new Checkbox("Joanna Pacula", p, false);
    Checkbox p5 = new Checkbox("Roman Polanski", p, false);

    public void init() {
        add(p1);
        add(p2);
        add(p3);
        add(p4);
        add(p5);
    }
}
