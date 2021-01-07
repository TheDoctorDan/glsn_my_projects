import java.awt.Graphics;
import java.awt.Image;

public class Fillmore extends java.applet.Applet {
    Image whig;

    public void init() {
        whig = getImage(getCodeBase(),
            "images/fillmore.jpg");
    }

    public void paint(Graphics screen) {
        int iWidth = whig.getWidth(this);
        int iHeight = whig.getHeight(this);
        int xPos = 10;
        // 25%
        screen.drawImage(whig, xPos, 10,
            iWidth / 4, iHeight / 4, this);
        // 100%
        xPos += (iWidth / 4) + 10;
        screen.drawImage(whig, xPos, 10, this);
    }
}
