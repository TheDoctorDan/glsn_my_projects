// Palindrome.java

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

public class Palindrome extends java.applet.Applet {
	Font f = new Font("TimesRoman", Font.BOLD, 36);

	public void paint(Graphics screen) {
		int	line;
		int	incr;

		line=40;
		incr=36;
		screen.setFont(f);
		screen.setColor(Color.red);
		screen.drawString("Go hang a salami, I'm a lasagna hog.", 5, line);
		line+=incr;

		screen.setColor(Color.blue);
		screen.drawString("Able.", 5, line);
		line+=incr;
		screen.setColor(Color.green);
		screen.drawString("was.", 5, line);
		line+=incr;
		screen.drawString("was.", 5, 112);
	}
}

