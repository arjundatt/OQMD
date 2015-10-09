package DIC.component.rightview;

import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/22/13
 * Time: 11:02 AM
 */
public class DefaultRightViewDisplay extends JPanel implements RightViewDisplay {
    JideTabbedPane tabbedPane;
    DefaultTableView tableView;

    public DefaultTableView getTableView() {
        return tableView;
    }

    public DefaultRightViewDisplay() {
        setLayout(new BorderLayout());
        tabbedPane = new JideTabbedPane();
        tableView = new DefaultTableView();
        tableView = (DefaultTableView) tableView.emptyDisplay();
        init("Empty Display", tableView);
        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void init(String defaultTableName, Component component) {
        tabbedPane.removeAll();
        tabbedPane.addTab(defaultTableName, component);
    }

    @Override
    public void addTab(String title, Component component) {
        tabbedPane.addTab(title, component);
    }

    @Override
    public void removeTab(int tabIndex) {
        if (tabbedPane.getTabCount() > tabIndex)
            tabbedPane.remove(tabIndex);
    }

    @Override
    public synchronized void refresh(String tabName, int index, Component component) {
        tabbedPane.setComponentAt(index, component);
        tabbedPane.updateUI();
    }
}
