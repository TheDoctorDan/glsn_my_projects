import java.awt.*;

public class VCR extends java.applet.Applet {
    Button rewind = new Button("Rewind");
    Button play = new Button("Play");
    Button ff = new Button("Fast Forward");
    Button stop = new Button("Stop");
    Button eat = new Button("Eat Tape");

public void init() {
    add(rewind);
    add(play);
    add(ff);
    add(stop);
    add(eat);
    }
}
