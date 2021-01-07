import java.awt.*;

public class PopUpWindowDialog extends java.applet.Applet {
    Frame window;
    Button open, close;

    public void init() {
        open = new Button("Open Window");
        add(open);
        close = new Button("Close Window");
        add(close);

        window = new BaseFrame2("A Pop Up Window");
        window.resize(150,150);
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof Button) {
            String label = (String)arg;
            if (label.equals("Open Window")) {
                if (!window.isShowing())
                    window.show();
            } else {
                if (window.isShowing())
                    window.hide();
            }
            return true;
        } else
            return false;
    }
}
