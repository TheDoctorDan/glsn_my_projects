import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;

public class NewLines extends java.applet.Applet
    implements MouseListener, MouseMotionListener {

    final int MAXLINES = 10;
    Point starts[] = new Point[MAXLINES]; // starting points
    Point ends[] = new Point[MAXLINES];    // endingpoints
    Point anchor;    // start of current line
    Point currentpoint; // current end of line
    int currline = 0; // number of lines

    public void init() {
        setBackground(Color.white);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent evt) {
        if (currline < MAXLINES)
            anchor = new Point(evt.getX(), evt.getY());
        else
            System.out.println("Too many lines.");
    }

    public void mouseReleased(MouseEvent evt) {
        if (currline < MAXLINES)
             addline(evt.getX(), evt.getY());
    }

    public void mouseDragged(MouseEvent evt) {
        if (currline < MAXLINES) {
            currentpoint = new Point(evt.getX(), evt.getY());
            repaint();
        }
    }

    public void mouseEntered(MouseEvent evt) {
        // do nothing
    }

    public void mouseClicked(MouseEvent evt) {
        // do nothing
    }

    public void mouseExited(MouseEvent evt) {
        // do nothing
    }

    public void mouseMoved(MouseEvent evt) {
        // do nothing
    }

    void addline(int x, int y) {
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
