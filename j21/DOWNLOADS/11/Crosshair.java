import java.awt.*;

public class Crosshair extends java.applet.Applet {
    GridLayout gl = new GridLayout(1,1);
    MyCanvas can = new MyCanvas();

    public void init() {
        setLayout(gl);
        add(can);
    }

}

class MyCanvas extends java.awt.Canvas {
    public void paint(Graphics g) {
        int x = size().width / 2;
        int y = size().height / 2;
        g.setColor(Color.black);
        g.drawLine(x-10,y,x-2,y);
        g.drawLine(x+10,y,x+2,y);
        g.drawLine(x,y-10,x,y-2);
        g.drawLine(x,y+10,x,y+2);
    }
}
