package DIC.component.rightview.tablecomponent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by Arnab Saha on 10/11/15.
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
