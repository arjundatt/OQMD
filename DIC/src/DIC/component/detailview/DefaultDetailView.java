package DIC.component.detailview;

import DIC.component.rightview.DefaultRightViewDisplay;
import DIC.component.rightview.DefaultTableView;
import DIC.component.rightview.tablecomponent.KTable;
import DIC.util.commons.SQLParser;
import DIC.util.database.DatabaseUtility;
import org.apache.commons.math.util.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by Arnab Saha on 10/10/15.
 */

public class DefaultDetailView extends DefaultRightViewDisplay implements PropertyChangeListener {
    public DefaultDetailView() {
        super();
        init("General Information", createDefaultPanel());

    }

    /**
     * This method creates the initial panel which contains the general information
     *
     * @return the panel containing the general information
     */
    private JPanel createDefaultPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label1 = new JLabel("EXECUTE- To execute Query");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(label1);
        JLabel label2 = new JLabel("CLEAR RESULT- To remove Result after execution");
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(label2);
        JLabel label3 = new JLabel("End every Query with a semicolon(;)");
        label3.setHorizontalAlignment(SwingConstants.CENTER);
        label3.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(label3);
        /*JideLabel label4 = new JideLabel("Use ctrl + space to auto complete queries");
        label4.setHorizontalAlignment(SwingConstants.CENTER);
        label4.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(label4);*/
        return panel;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("sql")) {
            init("General Information", createDefaultPanel());
            final String sql = String.valueOf(evt.getNewValue());
            String[] queriesArray = sql.split(";");
            final List<String> queries = Arrays.asList(queriesArray);
            final int count = 1;
            SwingWorker swingWorker = new SwingWorker() {
                @Override
                protected String doInBackground() throws Exception {
                    runQuery(count);
                    return null;
                }

                @Override
                protected void done() {
                    removeTab(1);
                }

                private void runQuery(int count) {
                    JPanel panel;
                    for (String query : queries) {
                        panel = new JPanel(new BorderLayout());
                        long start = System.nanoTime();
                        query = query.trim();
                        if (query.toLowerCase().startsWith("select")) {
                            Vector<Vector<Object>> data = null;
                            try {
                                data = DatabaseUtility.executeQuery((Connection) evt.getOldValue(), query);
                                Vector<Object> cols = data.elementAt(0);
                                Vector<String> columns = new Vector<String>();
                                for (Object column : cols) {
                                    columns.add(String.valueOf(column));
                                }
                                data.remove(0);
                                KTable table = new KTable(data, columns);
                                table.hideToolPanel();
                                panel.add(BorderLayout.CENTER, table);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                JLabel errorMessage = new JLabel("<html><h3><font color='red' size='8'>" + e.getMessage() + "</font></h3></html>");
                                errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
                                errorMessage.setVerticalAlignment(SwingConstants.CENTER);
                                panel.add(BorderLayout.CENTER, errorMessage);
                            }
                        } else {
                            try {
                                int rowsAffected = DatabaseUtility.updateQuery((Connection) evt.getOldValue(), query);
                                JLabel label;
                                if (sql.toLowerCase().startsWith("insert")) {
                                    label = new JLabel(rowsAffected + " rows inserted");
                                } else if (sql.toLowerCase().startsWith("update")) {
                                    label = new JLabel(rowsAffected + " rows updated");
                                } else if (sql.toLowerCase().startsWith("delete")) {
                                    label = new JLabel(rowsAffected + " rows deleted");
                                } else {
                                    label = new JLabel(rowsAffected + " rows affected");
                                }
                                panel.add(BorderLayout.CENTER, label);
                            } catch (SQLException e) {
                                JLabel errorMessage = new JLabel("<html><h3><font color='red' >" + e.getMessage() + "</font></h3></html>");
                                errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
                                errorMessage.setVerticalAlignment(SwingConstants.CENTER);
                                panel.add(BorderLayout.CENTER, errorMessage);
                            }
                        }
                        double denominator = 1000000000;
                        double x = (System.nanoTime() - start) / denominator;
                        double timeNeededForTheThreadToComplete = MathUtils.round(x, 2, BigDecimal.ROUND_HALF_DOWN);
                        addTab("Query " + count++ + " executed in " + timeNeededForTheThreadToComplete + " secs", panel, true);
                    }
                }
            };
            DefaultTableView progressBar = new DefaultTableView();
            addTab("Query Running", progressBar.showProgress(true), false);
            swingWorker.execute();
            getTabbedPane().setSelectedIndex(1);

        } else if (evt.getPropertyName().equals("closeAllTabs")) {
            int i = 1;
            while (removeTab(i++)) {
            }
            removeTab(1);
        } else if (evt.getPropertyName().equals("mapper")) {       //mapper view
            init("General Information", createDefaultPanel());
            final String sql = String.valueOf(evt.getNewValue());
            SwingWorker swingWorker = new SwingWorker() {
                @Override
                protected String doInBackground() throws Exception {
                    runQuery();
                    return null;
                }

                @Override
                protected void done() {
                    removeTab(1);
                }

                private void runQuery() {
                    JPanel rdbmsPanel, hbasePanel;
                    rdbmsPanel = new JPanel(new BorderLayout());
                    hbasePanel = new JPanel(new BorderLayout());
//                    long start = System.nanoTime();
                    String query = sql.trim();
                    if (query.toLowerCase().startsWith("select")) {
                        Vector<Vector<Object>> data = null;
                        try {
                            data = DatabaseUtility.executeQuery((Connection) evt.getOldValue(), query);
                            Vector<Object> cols = data.elementAt(0);
                            Vector<String> columns = new Vector<String>();
                            for (Object column : cols) {
                                columns.add(String.valueOf(column));
                            }
                            data.remove(0);
                            KTable table = new KTable(data, columns);
                            table.hideToolPanel();
                            rdbmsPanel.add(BorderLayout.CENTER, table);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            JLabel errorMessage = new JLabel("<html><h3><font color='red' size='8'>" + e.getMessage() + "</font></h3></html>");
                            errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
                            errorMessage.setVerticalAlignment(SwingConstants.CENTER);
                            rdbmsPanel.add(BorderLayout.CENTER, errorMessage);
                        }
                    }
                    SQLParser sqlParser = new SQLParser(query);
                    sqlParser.parse();
                    ArrayList<String> columnIDs = sqlParser.getColumnIDs();
                    //todo for arjun: the above statement retrives the columnIDs
                    String text = "<html><h3><font color='red' size='8'>";
                    for (String columnID : columnIDs) {
                        text += " " + columnID + ", ";
                        System.out.println(columnID);
                    }
                    text += "</font></h3></html>";
                    JLabel errorMessage = new JLabel(text);
                    errorMessage.setHorizontalAlignment(SwingConstants.CENTER);
                    errorMessage.setVerticalAlignment(SwingConstants.CENTER);
                    hbasePanel.add(errorMessage);
                    /*double denominator = 1000000000;
                    double x = (System.nanoTime() - start) / denominator;
                    double timeNeededForTheThreadToComplete = MathUtils.round(x, 2, BigDecimal.ROUND_HALF_DOWN);*/
                    addTab("Data from RDBMS", rdbmsPanel, true);
                    addTab("Data from Hbase", hbasePanel, true);
                }
            };
            DefaultTableView progressBar = new DefaultTableView();
            addTab("Query Running", progressBar.showProgress(true), false);
            swingWorker.execute();
            getTabbedPane().setSelectedIndex(1);

        }
    }
}
