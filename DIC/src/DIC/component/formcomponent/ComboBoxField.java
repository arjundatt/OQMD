package DIC.component.formcomponent;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nishtha Garg on 10/10/15.
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
