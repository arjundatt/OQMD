package DIC.util.commons;

import javax.swing.*;
import javax.swing.text.TextAction;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/28/13
 * Time: 4:16 PM
 */
public class Utility {
    public static String getLocalisedText(String property) throws IOException {
        Properties p = new Properties();
        p.load(new FileInputStream("resourceBundle.properties"));
        return p.getProperty(property);
    }

    public static JButton getButton(ImageIcon imageIcon, String toolTipText) {
        JButton button = new JButton();
        Image image = imageIcon.getImage();
        button.setIcon(new ImageIcon(image.getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
        button.setRolloverIcon(new ImageIcon(image.getScaledInstance(17, 17, Image.SCALE_SMOOTH)));
        button.setToolTipText(toolTipText);
        return button;
    }

    public static JButton getButton(ImageIcon icon, String toolTipText, int mnemonic) {
        JButton button = getButton(icon, toolTipText);
        button.setMnemonic(mnemonic);
        return button;
    }


    public static JButton getButton(ImageIcon icon, String toolTipText, int mnemonic, TextAction textAction){
        JButton button = new JButton(textAction);
        button = getButton(button, icon, toolTipText, mnemonic);
        return button;
    }

    public static JButton getButton(JButton button,ImageIcon icon, String toolTipText, int mnemonic) {
        Image image = icon.getImage();
        button.setIcon(new ImageIcon(image.getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
        button.setRolloverIcon(new ImageIcon(image.getScaledInstance(17, 17, Image.SCALE_SMOOTH)));
        button.setText("");
        button.setToolTipText(toolTipText);
        button.setMnemonic(mnemonic);
        return button;
    }
}
