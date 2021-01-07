import java.awt.*;

public class Virginia extends java.applet.Applet {
    String letter = "Dear Editor:\n" +
        "I am 8 years old.\n" +
        "Some of my little friends say there is no Santa Claus." +
            " Papa\n" +
        "says, ''If you see it in The Sun it's so.'' Please tell" +
            " me the truth,\n" +
        "is there a Santa Claus?\n\n" +
        "Virginia O'Hanlon\n" +
        "115 West 95th Street\n" +
        "New York";
    TextArea lt;

    public void init() {
        lt = new TextArea(letter, 10, 50);
        add(lt);
        }
}
