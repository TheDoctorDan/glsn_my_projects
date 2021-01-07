import java.awt.*;

public class Labels extends java.applet.Applet {
    Label lefty = new Label("Bleeding heart!");
    Label center = new Label("Centrist!", Label.CENTER);
    Label righty = new Label("Hardliner!", Label.RIGHT);
    Font lf = new Font("Helvetica", Font.BOLD, 14);
    GridLayout layout = new GridLayout(3,1);

    public void init() {
        setFont(lf);
        setLayout(layout);
        add(lefty);
        add(center);
        add(righty);
    }
}
