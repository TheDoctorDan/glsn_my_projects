import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

public class Neko extends java.applet.Applet
    implements Runnable {

    Image nekoPics[] = new Image[9];
    Image currentImg;
    Thread runner;
    int x;
    int y = 50;

    public void init() {
        String nekoSrc[] = { "right1.gif", "right2.gif",
            "stop.gif", "yawn.gif", "scratch1.gif",
            "scratch2.gif","sleep1.gif", "sleep2.gif",
            "awake.gif" };

        for (int i=0; i < nekoPics.length; i++) {
            nekoPics[i] = getImage(getCodeBase(),
                "images/" + nekoSrc[i]);
        }
    }

    public void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void stop() {
        runner = null;
    }

    public void run() {
        setBackground(Color.white);
        // run from one side of the screen to the middle
        nekoRun(0, size().width / 2);
        // stop and pause
        currentImg = nekoPics[2];
        repaint();
        pause(1000);
        // yawn
        currentImg = nekoPics[3];
        repaint();
        pause(1000);
        // scratch four times
        nekoScratch(4);
        // sleep for 5 "turns"
        nekoSleep(5);
        // wake up and run off
        currentImg = nekoPics[8];
        repaint();
        pause(500);
        nekoRun(x, size().width + 10);
    }

    void nekoRun(int start, int end) {
        for (int i = start; i < end; i += 10) {
            x = i;
            // swap images
            if (currentImg == nekoPics[0])
                currentImg = nekoPics[1];
            else currentImg = nekoPics[0];
            repaint();
            pause(150);
        }
    }

    void nekoScratch(int numTimes) {
        for (int i = numTimes; i > 0; i--) {
            currentImg = nekoPics[4];
            repaint();
            pause(150);
            currentImg = nekoPics[5];
            repaint();
            pause(150);
        }
    }

    void nekoSleep(int numTimes) {
        for (int i = numTimes; i > 0; i--) {
            currentImg = nekoPics[6];
            repaint();
            pause(250);
            currentImg = nekoPics[7];
            repaint();
            pause(250);
        }
    }

    void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) { }
    }

    public void paint(Graphics screen) {
        if (currentImg != null)
            screen.drawImage(currentImg, x, y, this);
    }
}
