import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

public class Palindrome extends java.applet.Applet {
    Font f = new Font("TimesRoman", Font.BOLD, 36);

    public void paint(Graphics screen) {
        screen.setFont(f);
        screen.setColor(Color.red);
        screen.drawString("Go hang a salami, I'm a lasagna hog.", 5, 40);
    }
}
