package DIC.component.rightview;

import com.jidesoft.swing.JideLabel;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/22/13
 * Time: 1:13 PM
 */
public class ColumnInfo extends JPanel {
    String name, length, type;

    public ColumnInfo(String name, String length, String type) {
        setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));
        setLayout(new GridLayout(5,2,500,10));
        this.name = name;
        this.type = type;
        this.length = length;
        add(new JideLabel("Name"));
        add(new JideLabel(name));
        add(new JideLabel("Length"));
        add(new JideLabel(length));
        add(new JideLabel("Type"));
        add(new JideLabel(type));
    }
}
