package DIC;

import DIC.component.detailview.SqlEditorView;
import DIC.component.lefthierarchy.LeftHierarchy;
import DIC.component.rightview.*;
import DIC.component.rightview.tablecomponent.*;
import DIC.component.treecomponent.*;
import DIC.util.database.DatabaseUtility;
import DIC.xml.XMLTree;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by Nishtha Garg on 10/10/15.
 */
public class ApplicationWindows extends JFrame implements MouseListener {

    private Connection connection;
    private KTable table;
    private JTree jTree;
    private XMLTree xmlTree;
    private HierarchyTreeNode node;
    private LeftHierarchy leftHierarchy;
    private DefaultRightViewDisplay rightPanel;

    public ApplicationWindows(String message) throws HeadlessException {

        JLabel label = new JLabel("Welcome " + message);
        createApplicationWindows(label);

        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    xmlTree.writeXml(jTree);
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }
            }
        });
        node = (HierarchyTreeNode) jTree.getLastSelectedPathComponent();
        showConnectionTable();
    }

    private void createApplicationWindows(JLabel label) {
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        getContentPane().add(BorderLayout.NORTH, label);

        leftHierarchy = new LeftHierarchy();

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(leftHierarchy);

        rightPanel = new DefaultRightViewDisplay();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(200);
        splitPane.setOneTouchExpandable(false);
        add(splitPane);

        jTree = leftHierarchy.getjTree();
        jTree.addMouseListener(this);
        xmlTree = new XMLTree();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2007_STYLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ApplicationWindows("OQMD");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            node = (HierarchyTreeNode) ((JTree) e.getSource()).getLastSelectedPathComponent();
            if (node.getLevel() > 1)
                callConnectionFromInstanceNode(node);
            try {
                if (node != null) {
                    if (node.getLevel() == 0) {
                        showConnectionTable();
                    } else if (node.getLevel() == 1) {                  //TODO left click
                        showInstanceView();
                    } else if (node.getLevel() == 2) {
                        showSchemaView();
                    } else if (node.getLevel() == 3) {
                        showTableView();
                    } else if ((node.getLevel() == 4)) {
                        showColumnView();
                    }

                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void showColumnView() {
        String name = node.getAttribute("name");
        String length = node.getAttribute("length");
        String type = node.getAttribute("type");
        ColumnInfo columnInfo = new ColumnInfo(name, length, type);
        JPanel columnInfoPanel = new JPanel();
        columnInfoPanel.add(columnInfo);
        rightPanel.init("Column Info", columnInfoPanel);
    }

    private void showInstanceView() {
        String connectionName = node.getAttribute("name");
        String system = node.getAttribute("system");
        String instanceName = node.getAttribute("instance");
        String databaseType = node.getAttribute("database");
        String port = node.getAttribute("port");
        InstanceInfo instanceInfo = new InstanceInfo(connectionName, system, instanceName, databaseType, port);
        JPanel instanceInfoPanel = new JPanel();
        instanceInfoPanel.add(instanceInfo);
        rightPanel.init("Instance Info", instanceInfoPanel);
    }

    private void showSchemaView() {
        Enumeration<HierarchyTreeNode> children = node.children();
        String[] col = {"Table Names", "Record Count", "Column Count", "Metadata Discover"};
        Vector<String> columns = new Vector<String>(Arrays.asList(col));
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (children.hasMoreElements()) {
            Vector<Object> currentChild = new Vector<Object>();
            HierarchyTreeNode temp = children.nextElement();
            currentChild.add(temp.getAttribute("name"));
            if (temp.getAttribute("metadata_discovered").equals("true")) {
                currentChild.add(Integer.parseInt(temp.getAttribute("record_count")));
            } else {
                currentChild.add("");
            }
            if (temp.getAttribute("column_discovered").equals("true")) {
                currentChild.add(Integer.parseInt(temp.getAttribute("column_count")));
            } else {
                currentChild.add("");
            }
            currentChild.add(Boolean.valueOf(temp.getAttribute("metadata_discovered")));
            data.add(currentChild);
        }
        table = new KTable(data, columns);
        rightPanel.init("Tables", table);
        SqlEditorView sqlEditor = new SqlEditorView(connection);
        rightPanel.addTab("Sql Editor", sqlEditor, false);
        //todo add mapper tab
    }

    private void showTableView() {
        JLabel metadataLabel = new JLabel("Metadata Not Discovered");
        metadataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.init("Metadata", metadataLabel);
        JLabel tableLabel = new JLabel("Metadata Not Discovered");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.addTab("Data", tableLabel, false);
        showMetaData();
        showTableData();
    }

    private void showMetaData() {
        HierarchyTreeNode instanceNode = (HierarchyTreeNode) (node.getParent()).getParent();
        callConnectionFromInstanceNode(instanceNode);
        if (node.getAttribute("metadata_discovered").equals("true")) {
            Enumeration<HierarchyTreeNode> children = node.children();
            String[] col = new String[]{"Name", "Data Type", "Size", "Is Primary Key"};
            Vector<String> columns = new Vector<String>(Arrays.asList(col));
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (children.hasMoreElements()) {
                Vector<Object> currentChild = new Vector<Object>();
                HierarchyTreeNode temp = children.nextElement();
                currentChild.add(temp.getAttribute("name"));
                currentChild.add(temp.getAttribute("type"));
                currentChild.add(Integer.parseInt(temp.getAttribute("length")));
                if (temp.getAttribute("time_stamp") != null) {
                    currentChild.add(Long.parseLong(temp.getAttribute("time_stamp")));
                } else {
                    currentChild.add("");
                }
                data.add(currentChild);
            }
            table = new KTable(data, columns);
            rightPanel.refresh("Tables", 0, table);
        }
        rightPanel.updateUI();
    }

    private void showTableData() {
        SwingWorker newWorker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                firePropertyChange("message", null, "Fetching Data ...");
                Vector data = DatabaseUtility.getData(connection, node.getParent().toString(), node.toString());
                firePropertyChange("message", null, "Fetching Columns ...");
                Vector<String> columns = new Vector<String>(DatabaseUtility.getColumns(connection, node.getParent().toString(), node.toString()));
                firePropertyChange("message", null, "Creating Data Table...");
                table = new KTable(data, columns);
                return null;
            }

            @Override
            protected void done() {
                table.getRefresh().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showTableData();
                    }
                });
                rightPanel.refresh("Data", 1, rightPanel.getTableView().refresh(table));
            }
        };
        rightPanel.refresh("Data", 1, rightPanel.getTableView().showProgress(true));
        newWorker.execute();
        newWorker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("message")) {
                    rightPanel.getTableView().getLabel().setText((String) evt.getNewValue());
                    rightPanel.updateUI();
                }
            }
        });
    }

    private void showConnectionTable() {
        Enumeration<HierarchyTreeNode> children = node.children();
        String[] col = {"Database", "Instance Name", "Connection Name", "Port", "System"};
        Vector<String> columns = new Vector<String>(Arrays.asList(col));
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (children.hasMoreElements()) {
            Vector<Object> currentChild = new Vector<Object>();
            HierarchyTreeNode temp = children.nextElement();
            currentChild.add(temp.getAttribute("database"));
            currentChild.add(temp.getAttribute("instance"));
            currentChild.add(temp.getAttribute("name"));
            currentChild.add(temp.getAttribute("port"));
            currentChild.add(temp.getAttribute("system"));
            data.add(currentChild);
        }
        table = new KTable(data, columns);
        rightPanel.init("Connection Info", table);
        rightPanel.updateUI();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * This function dynamically generates the instance level node from any node given
     *
     * @param node HierarchyTreeNode
     * @return instance level node
     */
    public HierarchyTreeNode getInstanceNode(HierarchyTreeNode node) {
        if (node.getLevel() > 1)
            return getInstanceNode((HierarchyTreeNode) node.getParent());
        else
            return node;
    }

    /**
     * This function takes the instance node as a parameter and calls the specific connection from the instanceNode if
     * that connection is not present in the connection map.
     *
     * @param node instance Node
     */
    private synchronized void callConnectionFromInstanceNode(HierarchyTreeNode node) {
        HierarchyTreeNode instanceNode = getInstanceNode(node);
        connection = leftHierarchy.getConnectionMap().get(instanceNode);
        if (connection == null) {
            try {
                connection = DatabaseUtility.getConnection(instanceNode.getAttribute("system"), instanceNode.getAttribute("database"), instanceNode.getAttribute("port"), instanceNode.getAttribute("instance"), instanceNode.getAttribute("uname"), instanceNode.getAttribute("pass"));
                leftHierarchy.setConnection(connection);
                leftHierarchy.getConnectionMap().put(instanceNode, connection);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
