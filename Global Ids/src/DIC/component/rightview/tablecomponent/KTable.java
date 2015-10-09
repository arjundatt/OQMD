package DIC.component.rightview.tablecomponent;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/8/13
 * Time: 5:23 PM
 */
public class KTable extends JPanel implements ActionListener, KeyListener {
    private JTable table;
    private JPanel toolPanel;
    private JPanel searchPanel;
    private JButton search;
    private JButton refresh;
    private JButton export;
    private JButton closeSearchPanelButton;
    private JButton nextButton;
    private JButton prevButton;
    private JTextField searchTextField;
    private int row;
    private String lastSearchedString;
    private int searchCount;
    private JCheckBox matchCaseCheckBox, wordsCheckBox;
//    private JideLabel matchCaseLabel, wordsLabel;

    public KTable(Vector<Vector<Object>> data, Vector<String> columnNames) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        lastSearchedString = null;
        createButtonPanel();
        add(toolPanel);
        createSearchPanel();
        add(searchPanel);
        add(createTable(data, columnNames));
        table.getTableHeader().setReorderingAllowed(false);
        updateUI();
    }

    public JButton getRefresh() {
        return refresh;
    }

    private void createSearchPanel() {
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1, 2));
        JPanel leftSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search");
        leftSearchPanel.add(searchLabel);
        searchTextField = new JTextField(15);
        searchTextField.addKeyListener(this);
        leftSearchPanel.add(searchTextField);
        prevButton = new JButton(new ImageIcon(getClass().getResource("../../../resource/back_round.gif")));
        prevButton.setToolTipText("Previous");
        prevButton.addActionListener(this);
        prevButton.setEnabled(false);
        leftSearchPanel.add(prevButton);
        nextButton = new JButton(new ImageIcon(getClass().getResource("../../../resource/next.gif")));
        nextButton.setToolTipText("Next");
        nextButton.setEnabled(false);
        nextButton.addActionListener(this);
        leftSearchPanel.add(nextButton);
        matchCaseCheckBox = new JCheckBox("Match Case", false);
        matchCaseCheckBox.addActionListener(this);
        leftSearchPanel.add(matchCaseCheckBox);
        wordsCheckBox = new JCheckBox("Words", false);
        wordsCheckBox.addActionListener(this);
        leftSearchPanel.add(wordsCheckBox);
        searchPanel.add(leftSearchPanel);
        closeSearchPanelButton = new JButton(new ImageIcon(getClass().getResource("../../../resource/cancel.gif")));
        closeSearchPanelButton.addActionListener(this);
        JPanel closeSearchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeSearchPanel.add(closeSearchPanelButton);
        searchPanel.add(closeSearchPanel);
        searchPanel.setVisible(false);
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        searchPanel.setMaximumSize(new Dimension(1200, 15));
    }

    private void createButtonPanel() {
        toolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search = new JButton(new ImageIcon(getClass().getResource("../../../resource/search_16.gif")));
        search.setToolTipText("Search");
        search.addActionListener(this);
        toolPanel.add(search);
        refresh = new JButton(new ImageIcon(getClass().getResource("../../../resource/refresh_16.gif")));
        refresh.setToolTipText("Refresh");
        toolPanel.add(refresh);
        export = new JButton(new ImageIcon(getClass().getResource("../../../resource/export.gif")));
        export.setToolTipText("Export");
        toolPanel.add(export);
        toolPanel.setMaximumSize(new Dimension(1200, 15));
    }

    public JTable getTable() {
        return table;
    }

    private JScrollPane createTable(final Vector<Vector<Object>> data, final Vector<String> columnNames) {
        table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        KTableRenderer renderer = new KTableRenderer(table);
        table.setDefaultRenderer(Object.class, renderer);
        TableModel model = table.getModel();

        TableRowSorter<TableModel> tableSorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(tableSorter);
        table.setRowSelectionAllowed(true);   //TODO making no difference
        return new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private void search(String toSearch, boolean nextSearch) {
        if (!(lastSearchedString != null && lastSearchedString.equals(toSearch))) {
            searchCount = 0;
            row = 0;
        } else {
            if (nextSearch) {
                row++;
            } else {
                row--;
            }
        }
        if (nextSearch) {
            if (nextRow(toSearch) != -1) {
                table.setRowSelectionInterval(row, row);
//                table.scrollRowToVisible(row);
            }
            int temp1 = row;
            int temp2 = searchCount;
            row++;
            if (nextRow(toSearch) == -1)
                nextButton.setEnabled(false);
            row = temp1;
            searchCount = temp2;
        } else {
            if (prevRow(toSearch) != -1) {
                table.setRowSelectionInterval(row, row);
//                table.scrollRowToVisible(row);
            }
            nextButton.setEnabled(true);
            int temp1 = row;
            int temp2 = searchCount;
            row--;
            if (prevRow(toSearch) == -1)
                prevButton.setEnabled(false);
            row = temp1;
            searchCount = temp2;
        }
    }

    private int prevRow(String toSearch) {
        boolean flag = false;
        for (; row >= 0 || flag; row--) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                Object o = table.getValueAt(row, col);
                if (o != null) {
                    String value = String.valueOf(o);
                    if (matchCaseCheckBox.isSelected() && wordsCheckBox.isSelected() && value.equals(toSearch)) {
                        flag = true;
                        lastSearchedString = toSearch;
                        break;
                    } else if (matchCaseCheckBox.isSelected() && value.contains(toSearch)) {
                        flag = true;
                        lastSearchedString = toSearch;
                        break;
                    } else if (wordsCheckBox.isSelected() && value.equals(toSearch)) {
                        flag = true;
                        lastSearchedString = toSearch;
                        break;
                    } else if (value.toLowerCase().contains(toSearch.toLowerCase())) {
                        flag = true;
                        lastSearchedString = toSearch;
                        break;
                    }
                }
            }
            if (flag) {
                searchCount--;
                if (searchCount <= 0) {
                    prevButton.setEnabled(false);
                }
                return row;
            }
        }
        return -1;
    }

    private int nextRow(String toSearch) {
        boolean flag = false;
        for (; row < table.getRowCount() || flag; row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                Object o = table.getValueAt(row, col);
                if (o != null) {
                    String value = String.valueOf(o);
                    if (matchCaseCheckBox.isSelected() && wordsCheckBox.isSelected() && value.equals(toSearch)) {
                        flag = true;
                        lastSearchedString = toSearch;
                        break;
                    } else if (matchCaseCheckBox.isSelected() && !wordsCheckBox.isSelected() && value.contains(toSearch)) {
                        flag = true;
                        lastSearchedString = toSearch;
                        break;
                    } else if (wordsCheckBox.isSelected() && !matchCaseCheckBox.isSelected() && value.equals(toSearch)) {
                        flag = true;
                        lastSearchedString = toSearch;
                        break;
                    } else if (!matchCaseCheckBox.isSelected() && !wordsCheckBox.isSelected() && value.toLowerCase().contains(toSearch.toLowerCase())) {
                        flag = true;
                        lastSearchedString = toSearch;
                        break;
                    }
                }
            }
            if (flag) {
                searchCount++;
                if (searchCount > 1) {
                    prevButton.setEnabled(true);
                }
                return row;
            }
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            searchPanel.setVisible(!searchPanel.isVisible());
            searchTextField.setFocusable(true);
            searchTextField.requestFocus();
        }
        if (e.getSource() == closeSearchPanelButton) {
            searchPanel.setVisible(false);
        }
        if (e.getSource() == nextButton) {
            search(searchTextField.getText(), true);
            updateUI();
        }
        if (e.getSource() == prevButton) {
            search(searchTextField.getText(), false);
            updateUI();
        }
        if (e.getSource() == wordsCheckBox || e.getSource() == matchCaseCheckBox) {
            table.getSelectionModel().clearSelection();
            row = 0;
            searchCount = 0;
            lastSearchedString = null;
            prevButton.setEnabled(false);
            nextButton.setEnabled(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        table.getSelectionModel().clearSelection();
        row = 0;
        searchCount = 0;
        lastSearchedString = null;
        prevButton.setEnabled(false);
        nextButton.setEnabled(true);
        if (searchTextField.getText().equals("")) {
            nextButton.setEnabled(false);
        }
    }

    public void hideToolPanel(){
        toolPanel.setVisible(false);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
