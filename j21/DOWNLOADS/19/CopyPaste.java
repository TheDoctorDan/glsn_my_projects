import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

public class CopyPaste extends Frame
    implements ActionListener, ClipboardOwner {

    Button copy, paste;
    TextField tfCopy, tfPaste;
    Clipboard clip;

    public static void main(String[] arguments) {
        CopyPaste test = new CopyPaste();
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        test.addWindowListener(l);
        test.setSize(200, 150);
        test.show();
    }

    CopyPaste() {
        super("Copy and Paste");
        clip = getToolkit().getSystemClipboard();
        FlowLayout flo = new FlowLayout();
        setLayout(flo);

        copy = new Button("Copy From");
        tfCopy = new TextField(25);
        paste = new Button("Paste To");
        tfPaste = new TextField(25);

        copy.addActionListener(this);
        paste.addActionListener(this);
        paste.setEnabled(false);

        add(copy);
        add(tfCopy);
        add(paste);
        add(tfPaste);
    }

    void doCopy() {
        if (tfCopy.getText() != null) {
            String txt = tfCopy.getText();
            StringSelection trans = new StringSelection(txt);
            clip.setContents(trans, this);
            paste.setEnabled(true);
        }
    }

    void doPaste() {
        Transferable toPaste = clip.getContents(this);
        if (toPaste != null) {
            try {
                String txt = (String)toPaste.getTransferData(
                    DataFlavor.stringFlavor);
                tfPaste.setText(txt);
                paste.setEnabled(false);
            } catch (Exception e) {
                System.out.println("Error -- " + e.toString());
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == copy)
            doCopy();
        else if (e.getSource() == paste)
            doPaste();
    }

    public void lostOwnership(Clipboard clip,
        Transferable contents) {
    }
}
