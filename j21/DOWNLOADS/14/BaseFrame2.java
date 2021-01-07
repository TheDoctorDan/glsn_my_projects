import java.awt.*;

class BaseFrame2 extends Frame {
    String message = "This is a Window";
    Label l;
    TextDialog dl;

    BaseFrame2(String title) {
        super(title);
        setLayout(new BorderLayout());

        dl = new TextDialog(this, "Enter Text", true);
        dl.resize(150,150);
        l = new Label(message, Label.CENTER);
        l.setFont(new Font("Helvetica", Font.PLAIN, 12));
        add("Center", l);
        Button b = new Button("Set Text");
        add("South", b);
   }

   public Insets getInsets() {
       return new Insets(20,0,25,0);
   }

    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof Button) {
            dl.show();
            return true;
        } else
            return false;        
    }
}
