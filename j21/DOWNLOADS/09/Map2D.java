import java.awt.*;
import java.awt.geom.*;

public class Map2D extends java.applet.Applet {
    public void paint(Graphics screen) {
        Graphics2D screen2D = (Graphics2D)screen;
        setBackground(Color.blue);
        // Draw waves
        screen2D.setColor(Color.white);
        BasicStroke pen = new BasicStroke(2F,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        screen2D.setStroke(pen);
        for (int ax = 10; ax < 340; ax += 10)
            for (int ay = 30; ay < 340 ; ay += 10) {
                Arc2D.Float wave = new Arc2D.Float(ax, ay,
                    10, 10, 0, -180, Arc2D.OPEN);
                screen2D.draw(wave);
            }
        // Draw Florida
        GradientPaint gp = new GradientPaint(0F,0F,Color.green,
            50F,50F,Color.orange,true);
        screen2D.setPaint(gp);
        GeneralPath fl = new GeneralPath();
        fl.moveTo(10F,12F);
        fl.lineTo(234F,15F);
        fl.lineTo(253F,25F);
        fl.lineTo(261F,71F);
        fl.lineTo(344F,209F);
        fl.lineTo(336F,278F);
        fl.lineTo(295F,310F);
        fl.lineTo(259F,274F);
        fl.lineTo(205F,188F);
        fl.lineTo(211F,171F);
        fl.lineTo(195F,174F);
        fl.lineTo(191F,118F);
        fl.lineTo(120F,56F);
        fl.lineTo(94F,68F);
        fl.lineTo(81F,49F);
        fl.lineTo(12F,37F);
        fl.closePath();
        screen2D.fill(fl);
        // Draw ovals
        screen2D.setColor(Color.black);
        BasicStroke pen2 = new BasicStroke();
        screen2D.setStroke(pen2);
        Ellipse2D.Float e1 = new Ellipse2D.Float(235,140,15,15);
        Ellipse2D.Float e2 = new Ellipse2D.Float(225,130,15,15);
        Ellipse2D.Float e3 = new Ellipse2D.Float(245,130,15,15);
        screen2D.fill(e1);
        screen2D.fill(e2);
        screen2D.fill(e3);
    }
}
