package DIC.component.rightview.tablecomponent;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Created by Arnab Saha on 10/11/15.
 */
public class KTableRenderer implements TableCellRenderer {
    JLabel label = new JLabel();

    public KTableRenderer(JTable table) {
        TableColumnModel columnModel =table.getColumnModel();
        int column;
        for (column = 0; column < columnModel.getColumnCount(); column++) {
            TableColumn tableColumn = columnModel.getColumn(column);
            TableCellRenderer renderer = tableColumn.getHeaderRenderer();
            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            Component component = renderer.getTableCellRendererComponent(table,tableColumn.getHeaderValue(), false, false, -1, column);
            tableColumn.setPreferredWidth(component.getPreferredSize().width);
        }
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        label.setOpaque(true);
        label.setText(String.valueOf(value));
        Color alternateColor = Color.decode("#eeeeee");
        Color usualColor = Color.WHITE;
        label.setBackground(row % 2 == 0 ? alternateColor : usualColor);
        if (value == null) {
            label.setText("");
        }
        if (value instanceof Boolean) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setBackground(row % 2 == 0 ? alternateColor : usualColor);
            checkBox.setSelected((Boolean) value);
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
            return checkBox;
        } else if (value instanceof Number) {
            label.setHorizontalAlignment(JLabel.RIGHT);
        } else {
            label.setHorizontalAlignment(JLabel.LEFT);
        }

        if(isSelected){
            label.setBackground(Color.decode("#8bbced"));
        }
        return label;
    }
}
