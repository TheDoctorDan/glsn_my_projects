import java.awt.*;

public class OutOfSite extends java.applet.Applet {
    Label siteLabel = new Label("Site Name: ");
    TextField site = new TextField(25);
    Label addressLabel = new Label("Site Address: ");
    TextField address = new TextField(25);
    Label passwordLabel = new Label("Admin Password: ");
    TextField password = new TextField(25);

    public void init() {
        add(siteLabel);
        add(site);
        add(addressLabel);
        add(address);
        add(passwordLabel);
        password.setEchoCharacter('*');
        add(password);
    }
}
