package DIC.component.rightview;

import DIC.util.database.DatabaseUtility;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 11/28/2015
 * Time: 1:24 PM
 */
public class MappingView extends JPanel {
    private boolean DEBUG = false;
    private String tableId;
    private String mappedTableId;

    public MappingView(String tableId, String mappedTableId) {
        super(new GridLayout(1, 0));
        this.tableId = tableId;
        this.mappedTableId = mappedTableId;

        JTable table = new JTable(new MappingTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Fiddle with the Sport column's cell editors/renderers.
        setUpSportColumn(table, table.getColumnModel().getColumn(2));

        //Add the scroll pane to this panel.
        add(scrollPane);
    }

    public void setUpSportColumn(JTable table,
                                 TableColumn sportColumn) {
        //Set up the editor for the sport cells.
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem("Snowboarding");
        comboBox.addItem("Rowing");
        comboBox.addItem("Knitting");
        comboBox.addItem("Speed reading");
        comboBox.addItem("Pool");
        comboBox.addItem("None of the above");
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);
    }

    class MappingTableModel extends AbstractTableModel {
        private String[] columnNames = {"Table 1 column id", "Table 1 column name",
                "Table 2 column name",
                " "};
        private Object[][] data = {
                {"Kathy", "Kathy",
                        "Snowboarding", new Boolean(false)},
                {"Kathy", "John",
                        "Rowing", new Boolean(true)},
                {"Kathy", "Sue",
                        "Knitting", new Boolean(false)},
                {"Kathy", "Jane",
                        "Speed reading", new Boolean(true)},
                {"Kathy", "Joe",
                        "Pool", new Boolean(false)}
        };

//        private Object[][] data2 = getData();

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                        + " to " + value
                        + " (an instance of "
                        + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    private Vector<Vector<Object>> getMappingData() {
        Vector<Vector<Object>> data = null;
        try {
            data = new Vector<Vector<Object>>();
            String tableSql = "select DIC_COLUMN_ID, DIC_COLUMN_NAME, DIC_COLUMN_REGEXID from DIC_COLUMN where DIC_COLUMN_TABLE_ID = '" + tableId + "'\n";
            Vector<Vector<String>> tablesVector = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(tableSql);
            tablesVector.remove(0);

            String mappedTableSql = "select DIC_COLUMN_ID, DIC_COLUMN_NAME, DIC_COLUMN_REGEXID, DIC_COLUMN_EFFICIENCY from DIC_COLUMN where DIC_COLUMN_TABLE_ID = '" + mappedTableId + "'\n";
            Vector<Vector<String>> mappedTablesVector = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(mappedTableSql);
            mappedTablesVector.remove(0);

            for (Vector<String> tableDetail : tablesVector) {
                for (Vector<String> mappedTableDetail : mappedTablesVector) {
                    if (mappedTableDetail.get(2).equals(tableDetail.get(2))) {
                        Vector<Object> aRow = new Vector<Object>();
                        aRow.add(tableDetail.get(1)); //first col name
                        aRow.add(mappedTableDetail.get(1)); //second col name
                        aRow.add(mappedTableDetail.get(2));  //efficiency
                        data.add(aRow);
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MappingView");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MappingView newContentPane = new MappingView("10020595", "10018752");
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

class Element {
    private String id;
    private String name;

    public Element(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}