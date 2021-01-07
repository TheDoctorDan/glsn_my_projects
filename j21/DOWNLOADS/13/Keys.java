import java.awt.Graphics;
import java.awt.Event;
import java.awt.Font;
import java.awt.Color;

public class Keys extends java.applet.Applet {

    char currkey;
    int currx;
    int curry;

    public void init() {
        currx = (size().width / 2) -8;  // default
        curry = (size().height / 2) -16;

        setBackground(Color.white);
        setFont(new Font("Helvetica",Font.BOLD,36));
        requestFocus();
    }

    public boolean keyDown(Event evt, int key) {
        switch (key) {
        case Event.DOWN:
            curry += 5;
            break;
        case Event.UP:
            curry -= 5;
            break;
        case Event.LEFT:
            currx -= 5;
            break;
        case Event.RIGHT:
            currx += 5;
            break;
        default:
            currkey = (char)key;
        }

        repaint();
        return true;
    }

    public void paint(Graphics g) {
        if (currkey != 0) {
            g.drawString(String.valueOf(currkey), currx,curry);
        }
    }
}
