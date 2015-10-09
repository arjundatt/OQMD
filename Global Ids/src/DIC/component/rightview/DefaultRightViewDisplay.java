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
    private JideTabbedPane tabbedPane;
    private DefaultTableView tableView;

    public DefaultTableView getTableView() {
        return tableView;
    }

    public JideTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public DefaultRightViewDisplay() {
        setLayout(new BorderLayout());
        tabbedPane = new JideTabbedPane();
        tableView = new DefaultTableView();
        tableView = (DefaultTableView) tableView.emptyDisplay();
        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public synchronized void init(String defaultTableName, Component component) {
        tabbedPane.removeAll();
        addTab(defaultTableName, component,false);
    }

    @Override
    public synchronized void addTab(String title, Component component, boolean close) {
        tabbedPane.addTab(title, component);
        tabbedPane.setShowCloseButtonOnTab(close);
//        tabbedPane.setShowCloseButtonOnSelectedTab(close);
        updateUI();
    }

    @Override
    public synchronized boolean removeTab(int tabIndex) {
        if (tabbedPane.getTabCount() > tabIndex) {
            tabbedPane.remove(tabIndex);
            return true;
        }
        return false;
    }

    @Override
    public synchronized void refresh(String tabName, int index, Component component) {
        tabbedPane.setComponentAt(index, component);
        tabbedPane.updateUI();
    }
}
