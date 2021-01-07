import java.awt.*;

public class Slacker extends java.applet.Applet {
    String note = "I am extremely tired and would prefer not " +
        "to be clicked. Please interact somewhere else.";
    Button tired = new Button(note);

    public void init() {
        add(tired);
    }
}
