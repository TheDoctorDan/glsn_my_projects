import java.awt.*;

public class Border extends java.applet.Applet {
    BorderLayout b = new BorderLayout();
    Button north = new Button("North");
    Button south = new Button("South");
    Button east = new Button("East");
    Button west = new Button("West");
    Button center = new Button("Center");

    public void init() {
        setLayout(b);
        add("North", north);
        add("South", south);
        add("East", east);
        add("West", west);
        add("Center", center);
    }
}
