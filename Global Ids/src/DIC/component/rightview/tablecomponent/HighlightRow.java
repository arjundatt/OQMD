package DIC.component.rightview.tablecomponent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/27/13
 * Time: 4:30 PM
 */

public class HighlightRow extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        if (row == table.getSelectedRow()) {
        setBorder(BorderFactory.createMatteBorder(2, 1, 2, 1, Color.blue));
//        }
        return this;
    }
}
