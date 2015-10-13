package DIC.component.formcomponent;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Globalids
 * Date: 8/8/13
 * Time: 12:13 PM
 */
public class PasswordField extends JPanel {
    private JPasswordField password;
    public PasswordField(String label, String defaultValue,int width, boolean compulsory) {
        setLayout(new BorderLayout());
        add(BorderLayout.WEST, new JLabel(label));
        if (compulsory) {
            JLabel label1 = new JLabel(String.format("<html>%s<font color='red'>%s</font></html>", "", "* "));
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(Box.createHorizontalGlue());
            panel.add(label1);
            password = new JPasswordField(width);
            panel.add(password);
            add(BorderLayout.EAST, panel);
        } else {
            password = new JPasswordField(width);
            add(BorderLayout.EAST, password);
        }
        setText(defaultValue);
    }

    public PasswordField(String label,int width, boolean compulsory) {
        setLayout(new BorderLayout());
        add(BorderLayout.WEST, new JLabel(label));
        if (compulsory) {
            JLabel label1 = new JLabel(String.format("<html>%s<font color='red'>%s</font></html>", "", "* "));
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(Box.createHorizontalGlue());
            panel.add(label1);
            password = new JPasswordField(width);
            panel.add(password);
            add(BorderLayout.EAST, panel);
        } else {
            password = new JPasswordField(width);
            add(BorderLayout.EAST, password);
        }
    }

    public String getText() {
        return String.valueOf(password.getPassword());
    }

    public void setText(String text) {
        password.setText(text);
    }

    public JPasswordField getPasswordField() {
        return password;
    }
}


