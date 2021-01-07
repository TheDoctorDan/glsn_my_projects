import java.awt.*;

class BaseFrame1 extends Frame {
    String message = "This is a Window";
    Label l;

    BaseFrame1(String title) {
        super(title);
        setLayout(new BorderLayout());

        l = new Label(message, Label.CENTER);
        l.setFont(new Font("Helvetica", Font.PLAIN, 12));
        add("Center", l);
   }

   public Insets getInsets() {
       return new Insets(20,0,25,0);
   }
}
