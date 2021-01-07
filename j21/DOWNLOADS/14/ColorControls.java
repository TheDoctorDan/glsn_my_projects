import java.awt.*;

class ColorControls extends Panel {
    ColorTest applet;
    TextField tfield1, tfield2, tfield3;

    ColorControls(ColorTest parent,
        String l1, String l2, String l3) {

        applet = parent;
        setLayout(new GridLayout(3,2,10,10));
        tfield1 = new TextField("0");
        tfield2 = new TextField("0");
        tfield3 = new TextField("0");
        add(new Label(l1, Label.RIGHT));
        add(tfield1);
        add(new Label(l2, Label.RIGHT));
        add(tfield2);
        add(new Label(l3, Label.RIGHT));
        add(tfield3);

    }

    public Insets getInsets() {
        return new Insets(10, 10, 0, 0);
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof TextField) {
            applet.update(this);
            return true;
        }
        else return false;
    }
}
