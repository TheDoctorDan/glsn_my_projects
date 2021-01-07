import java.awt.*;

class TextDialog extends Dialog {
    TextField tf;
    BaseFrame2 theFrame;

    TextDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);

        theFrame = (BaseFrame2)parent;
        setLayout(new BorderLayout(10,10));
        tf = new TextField(theFrame.message,20);
        add("Center", tf);

        Button b = new Button("OK");
        add("South", b);
    }

    public Insets insets() {
        return new Insets(30,10,10,10);
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof Button) {
            String label = (String)arg;
            if (label == "OK") {
                hide();
                theFrame.l.setText(tf.getText());
            }
            return true;
        } else
            return false;
    }
}
