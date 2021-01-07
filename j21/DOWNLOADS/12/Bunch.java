import java.awt.*;

public class Bunch extends java.applet.Applet {
    GridLayout family = new GridLayout(3,3,10,10);
    Button marcia = new Button("Marcia");
    Button carol = new Button("Carol");
    Button greg = new Button("Greg");
    Button jan = new Button("Jan");
    Button alice = new Button("Alice");
    Button peter = new Button("Peter");
    Button cindy = new Button("Cindy");
    Button mike = new Button("Mike");
    Button bobby = new Button("Bobby");

    public void init() {
        setLayout(family);
        add(marcia);
        add(carol);
        add(greg);
        add(jan);
        add(alice);
        add(peter);
        add(cindy);
        add(mike);
        add(bobby);
    }
}
