import java.awt.*;

public class Hamlet extends java.applet.Applet {
    List hm = new List(5, true);

    public void init() {
        hm.addItem("Hamlet");
        hm.addItem("Claudius");
        hm.addItem("Gertrude");
        hm.addItem("Polonius");
        hm.addItem("Horatio");
        hm.addItem("Laertes");
        hm.addItem("Ophelia");
        add(hm);
    }
}
