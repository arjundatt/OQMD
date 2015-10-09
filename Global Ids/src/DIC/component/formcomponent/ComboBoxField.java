package DIC.component.formcomponent;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/12/13
 * Time: 5:53 PM
 */
public class ComboBoxField extends JPanel {
    private JComboBox comboBox;
    public ComboBoxField(String label, JComboBox comboBox) {
        setLayout(new BorderLayout());
        add(BorderLayout.WEST, new JLabel(label));
        this.comboBox = comboBox;
        Dimension d = new Dimension(279, 20);
        comboBox.setPreferredSize(d);
        add(BorderLayout.EAST, comboBox);
    }

    public String getSelected() {
        return String.valueOf(comboBox.getSelectedItem());
    }

    public JComboBox getComboBoxField() {
        return comboBox;
    }

}
