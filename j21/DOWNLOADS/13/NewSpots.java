import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;

public class NewSpots extends java.applet.Applet implements MouseListener {
    final int MAXSPOTS = 10;
    int xspots[] = new int[MAXSPOTS];
    int yspots[] = new int[MAXSPOTS];
    int currspots = 0;

    public void init() {
        setBackground(Color.white);
        addMouseListener(this);
    }

    public void mouseClicked(MouseEvent evt) {
        if (currspots < MAXSPOTS) {
            addspot(evt.getX(), evt.getY());
        }
        else {
            System.out.println("Too many spots.");
        }
    }

    public void mousePressed(MouseEvent evt) {
        // do nothing
    }

    public void mouseReleased(MouseEvent evt) {
        // do nothing
    }

    public void mouseEntered(MouseEvent evt) {
        // do nothing
    }

    public void mouseExited(MouseEvent evt) {
        // do nothing
    }

    void addspot(int x,int y) {
        xspots[currspots] = x;
        yspots[currspots] = y;
        currspots++;
        repaint();
    }

    public void paint(Graphics g) {
        g.setColor(Color.blue);
        for (int i = 0; i < currspots; i++) {
            g.fillOval(xspots[i] - 10, yspots[i] - 10, 20, 20);
        }
    }
}
