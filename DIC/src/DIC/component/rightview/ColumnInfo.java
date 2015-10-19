package DIC.component.rightview;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Arnab Saha on 10/11/15.
 */
public class ColumnInfo extends JPanel {
    String name, length, type;

    public ColumnInfo(String name, String length, String type) {
        setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));
        setLayout(new GridLayout(5,2,500,10));
        this.name = name;
        this.type = type;
        this.length = length;
        add(new JLabel("Name"));
        add(new JLabel(name));
        add(new JLabel("Length"));
        add(new JLabel(length));
        add(new JLabel("Type"));
        add(new JLabel(type));
    }
}
