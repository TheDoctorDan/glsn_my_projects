import java.awt.Graphics;
import java.awt.Polygon;

public class Map extends java.applet.Applet {
    public void paint(Graphics screen) {
        screen.drawString("Florida", 185, 75);
        screen.drawLine(185,80,222,80);
        screen.drawRect(2, 2, 345, 345);
        screen.drawRoundRect(182,61,43,24,10,8);
        int x[] = { 10, 234, 253, 261, 344, 336, 295, 259, 205, 211,
            195, 191, 120, 94, 81, 12, 10 };
        int y[] = { 12, 15, 25, 71, 209, 278, 310, 274, 188, 171, 174,
            118, 56, 68, 49, 37, 12 };
        int pts = x.length;
        Polygon poly = new Polygon(x, y, pts);
        screen.drawPolygon(poly);
        screen.fillOval(235,140,15,15);
        screen.fillOval(225,130,15,15);
        screen.fillOval(245,130,15,15);
        for (int ax = 50; ax < 150; ax += 10)
            for (int ay = 120; ay < 320 ; ay += 10)
                screen.drawArc(ax, ay, 10, 10, 0, -180);
    }
}
