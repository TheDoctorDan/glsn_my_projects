import java.awt.*;

public class Checkers extends java.applet.Applet implements Runnable {
    Thread runner;
    int xPos = 5;
    int xMove = 4;
    Image offscreenImg;
    Graphics offscreen;


    public void init() {
        offscreenImg = createImage(size().width, size().height);
        offscreen = offscreenImg.getGraphics();
    }

    public void start() {
        if (runner == null); {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void stop() {
       runner = null;
    }

    public void run() {
        Thread thisThread = Thread.currentThread();
        while (runner == thisThread) {
            xPos += xMove;
            if ((xPos > 105) | (xPos < 5))
                xMove *= -1;
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { }
        }
   }

   public void update(Graphics screen) {
       paint(screen);
   }

   public void paint(Graphics screen) {
      // Draw background
      offscreen.setColor(Color.black);
      offscreen.fillRect(0,0,100,100);
      offscreen.setColor(Color.white);
      offscreen.fillRect(100,0,100,100);
      // Draw checker
      offscreen.setColor(Color.red);
      offscreen.fillOval(xPos,5,90,90);
      screen.drawImage(offscreenImg, 0, 0, this);
   }

   public void destroy() {
       offscreen.dispose();
   }
}
