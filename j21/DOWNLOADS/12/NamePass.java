import java.awt.*;

public class NamePass extends java.applet.Applet {

  void buildConstraints(GridBagConstraints gbc, int gx, int gy,
      int gw, int gh, int wx, int wy) {

      gbc.gridx = gx;
      gbc.gridy = gy;
      gbc.gridwidth = gw;
      gbc.gridheight = gh;
      gbc.weightx = wx;
      gbc.weighty = wy;
  }

  public void init() {
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();
      setLayout(gridbag);

      // Name label
      buildConstraints(constraints, 0, 0, 1, 1, 10, 40);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      Label label1 = new Label("Name:", Label.LEFT);
      gridbag.setConstraints(label1, constraints);
      add(label1);

      // Name text field
      buildConstraints(constraints, 1, 0, 1, 1, 90, 0);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      TextField tfname = new TextField();
      gridbag.setConstraints(tfname, constraints);
      add(tfname);

      // password label
      buildConstraints(constraints, 0, 1, 1, 1, 0, 40);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.EAST;
      Label label2 = new Label("Password:", Label.LEFT);
      gridbag.setConstraints(label2, constraints);
      add(label2);

      // password text field
      buildConstraints(constraints, 1, 1, 1, 1, 0, 0);
      constraints.fill = GridBagConstraints.HORIZONTAL;
      TextField tfpass = new TextField();
      tfpass.setEchoCharacter('*');
      gridbag.setConstraints(tfpass, constraints);
      add(tfpass);

      // OK Button
      buildConstraints(constraints, 0, 2, 2, 1, 0, 20);
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.CENTER;
      Button okb = new Button("OK");
      gridbag.setConstraints(okb, constraints);
      add(okb);
  }
}
