import java.awt.*;

public class SelectASpaniard extends java.applet.Applet {
    Choice span = new Choice();

    public void init() {
        span.addItem("Pedro Almodóvar");
        span.addItem("Antonio Banderas");
        span.addItem("Charo");
        span.addItem("Xavier Cugat");
        span.addItem("Julio Iglesias");
        add(span);
    }
}
