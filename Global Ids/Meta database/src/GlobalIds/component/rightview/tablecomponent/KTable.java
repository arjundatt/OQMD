package DIC.component.rightview.tablecomponent;

import com.jidesoft.grid.JideTable;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideLabel;
import com.jidesoft.swing.JideScrollPane;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/8/13
 * Time: 5:23 PM
 */
public class KTable extends JPanel implements ActionListener {
    private JideTable table;
    private JPanel buttonPanel;
    private JPanel searchPanel;
    JideButton search;
    JideButton refresh;
    JideButton export;
    JideButton closeSearchPanelButton;
    JideButton nextButton;
    JideButton prevButton;
    JideLabel label;

    public KTable(Vector<Vector<Object>> data, Vector<String> columnNames) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createButtonPanel();
        add(buttonPanel);
        createSearchPanel();
        add(searchPanel);
        add(createTable(data, columnNames));
        updateUI();
    }

    public JideButton getRefresh() {
        return refresh;
    }

    private void createSearchPanel() {
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1, 2));
        JPanel leftSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search");
        leftSearchPanel.add(searchLabel);
        JTextField searchTextField = new JTextField(15);
        leftSearchPanel.add(searchTextField);
        prevButton = new JideButton(new ImageIcon(getClass().getResource("../../../resource/back_round.gif")));
        prevButton.setToolTipText("Previous");
        leftSearchPanel.add(prevButton);
        nextButton = new JideButton(new ImageIcon(getClass().getResource("../../../resource/next.gif")));
        nextButton.setToolTipText("Next");
        leftSearchPanel.add(nextButton);
        searchPanel.add(leftSearchPanel);
        closeSearchPanelButton = new JideButton(new ImageIcon(getClass().getResource("../../../resource/cancel.gif")));
        closeSearchPanelButton.addActionListener(this);
        JPanel closeSearchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeSearchPanel.add(closeSearchPanelButton);
        searchPanel.add(closeSearchPanel);
        searchPanel.setVisible(false);
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        searchPanel.setMaximumSize(new Dimension(1200, 15));
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search = new JideButton(new ImageIcon(getClass().getResource("../../../resource/search_16.gif")));
        search.setToolTipText("Search");
        search.addActionListener(this);
        buttonPanel.add(search);
        refresh = new JideButton(new ImageIcon(getClass().getResource("../../../resource/refresh_16.gif")));
        refresh.setToolTipText("Refresh");
        buttonPanel.add(refresh);
        export = new JideButton(new ImageIcon(getClass().getResource("../../../resource/export.gif")));
        export.setToolTipText("Export");
        buttonPanel.add(export);
        buttonPanel.setMaximumSize(new Dimension(1200, 15));
    }

    public JTable getTable() {
        return table;
    }

    private JScrollPane createTable(final Vector<Vector<Object>> data, final Vector<String> columnNames) {
        table = new JideTable(data, columnNames) {
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
        return new JideScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == search) {
            searchPanel.setVisible(!searchPanel.isVisible());
        }
        if (e.getSource() == closeSearchPanelButton) {
            searchPanel.setVisible(false);
        }
    }
}
