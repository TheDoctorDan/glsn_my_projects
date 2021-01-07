import java.awt.*;
import java.net.*;

public class ButtonLink extends java.applet.Applet {
    Bookmark bmList[] = new Bookmark[3];

    public void init() {
        bmList[0] = new Bookmark("Sams' Teach Yourself Java 1.2 in 21 Days",
            "http://www.prefect.com/java21");
        bmList[1] = new Bookmark("Macmillan Computer Publishing",
            "http://www.mcp.com");
        bmList[2]= new Bookmark("Sun's Java Site",
            "http://java.sun.com");

        GridLayout gl = new GridLayout(bmList.length, 1, 10, 10);
        setLayout(gl);
        for (int i = 0; i < bmList.length; i++) {
            add(new Button(bmList[i].name));
        }
    }

    public boolean action(Event evt, Object arg) {
        if (evt.target instanceof Button) {
            linkTo( (String)arg );
            return true;
        }
        else return false;
    }

    void linkTo(String name) {
        URL theURL = null;
        for (int i = 0; i < bmList.length; i++) {
            if (name.equals(bmList[i].name))
                theURL = bmList[i].url;
        }
        if (theURL != null)
            getAppletContext().showDocument(theURL);
    }
}

class Bookmark {
    String name;
    URL url;

    Bookmark(String name, String theURL) {
        this.name = name;
        try {
            this.url = new URL(theURL);
        } catch (MalformedURLException e) {
            System.out.println("Bad URL: " + theURL);
        }
    }
}
