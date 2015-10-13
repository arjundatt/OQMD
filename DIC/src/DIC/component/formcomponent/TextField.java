package DIC.component.formcomponent;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/8/13
 * Time: 12:13 PM
 */
public class TextField extends JPanel {
    private JTextField textField;

    public TextField(String label, String defaultValue, int width, boolean compulsory) {
        setLayout(new BorderLayout());
        add(BorderLayout.WEST, new JLabel(label));
        if (compulsory) {
            JLabel label1 = new JLabel(String.format("<html>%s<font color='red'>%s</font></html>", "", "* "));
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(Box.createHorizontalGlue());
            panel.add(label1);
            textField = new JTextField(width);
            panel.add(textField);
            add(BorderLayout.EAST, panel);
        } else {
            textField = new JTextField(width);
            add(BorderLayout.EAST, textField);
        }
        setText(defaultValue);
    }

    public TextField(String label, int width, boolean compulsory) {
        setLayout(new BorderLayout());
        add(BorderLayout.WEST, new JLabel(label));
        if (compulsory) {
            JLabel label1 = new JLabel(String.format("<html>%s<font color='red'>%s</font></html>", "", "* "));
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(Box.createHorizontalGlue());
            panel.add(label1);
            textField = new JTextField(width);
            panel.add(textField);
            add(BorderLayout.EAST, panel);
        } else {
            textField = new JTextField(width);
            add(BorderLayout.EAST, textField);
        }
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public JTextField getTextField() {
        return textField;
    }


}


