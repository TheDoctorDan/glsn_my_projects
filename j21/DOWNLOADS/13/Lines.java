import java.awt.Graphics;
import java.awt.Color;
import java.awt.Event;
import java.awt.Point;

public class Lines extends java.applet.Applet {
    final int MAXLINES = 10;
    Point starts[] = new Point[MAXLINES]; // starting points
    Point ends[] = new Point[MAXLINES];    // endingpoints
    Point anchor;    // start of current line
    Point currentpoint; // current end of line
    int currline = 0; // number of lines

    public void init() {
        setBackground(Color.white);
    }

    public boolean mouseDown(Event evt, int x, int y) {
        if (currline < MAXLINES) {
            anchor = new Point(x,y);
            return true;
        }
        else  {
            System.out.println("Too many lines.");
            return false;
        }
    }

    public boolean mouseUp(Event evt, int x, int y) {
        if (currline < MAXLINES) {
             addline(x,y);
             return true;
        }
        else return false;
    }

    public boolean mouseDrag(Event evt, int x, int y) {
        if (currline < MAXLINES) {
            currentpoint = new Point(x,y);
            repaint();
            return true;
        }
        else return false;
    }

    void addline(int x,int y) {
        starts[currline] = anchor;
        ends[currline] = new Point(x,y);
        currline++;
        currentpoint = null;
        anchor = null;
        repaint();
    }

    public void paint(Graphics g) {
        // Draw existing lines
        for (int i = 0; i < currline; i++) {
            g.drawLine(starts[i].x, starts[i].y,
                 ends[i].x, ends[i].y);
        }

        // draw current line
        g.setColor(Color.blue);
        if (currentpoint != null)
            g.drawLine(anchor.x,anchor.y,
                currentpoint.x,currentpoint.y);
    }
}
