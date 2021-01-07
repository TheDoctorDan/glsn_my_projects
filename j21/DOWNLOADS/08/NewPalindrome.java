import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

public class NewPalindrome extends java.applet.Applet {
    Font f = new Font("TimesRoman", Font.BOLD, 36);
    String palindrome;

    public void paint(Graphics screen) {
        screen.setFont(f);
        screen.setColor(Color.red);
        screen.drawString(palindrome, 5, 50);
    }

    public void init() {
        palindrome = getParameter("palindrome");
        if (palindrome == null)
        palindrome = "Dennis and Edna sinned";
    }
}

