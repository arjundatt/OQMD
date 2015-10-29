package DIC;

import DIC.component.detailview.MapperEditorView;
import DIC.component.detailview.SqlEditorView;
import DIC.component.lefthierarchy.LeftHierarchy;
import DIC.component.rightview.ColumnInfo;
import DIC.component.rightview.DefaultRightViewDisplay;
import DIC.component.rightview.InstanceInfo;
import DIC.component.rightview.tablecomponent.KTable;
import DIC.component.treecomponent.HierarchyTreeNode;
import DIC.util.database.DatabaseUtility;
import DIC.xml.XMLTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        int Width = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int Height = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setSize(Width - 100, Height - 100);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        /*addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    xmlTree.writeXml(jTree);
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }
            }
        });*/
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
        String name = node.getAttribute("dic_column_name");
        String length = node.getAttribute("dic_column_length");
        String type = node.getAttribute("dic_column_type");
        ColumnInfo columnInfo = new ColumnInfo(name, length, type);
        JPanel columnInfoPanel = new JPanel();
        columnInfoPanel.add(columnInfo);
        rightPanel.init("Column Info", columnInfoPanel);
    }

    private void showInstanceView() {
        String connectionName = node.getAttribute("dic_instance_connectionname");
        String system = node.getAttribute("dic_instance_systemname");
        String instanceName = node.getAttribute("dic_instance_instancename");
        String databaseType = node.getAttribute("dic_instance_databasetype");
        String port = node.getAttribute("dic_instance_portnumber");
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
            currentChild.add(temp.getAttribute("dic_table_name"));
            if (temp.getAttribute("dic_table_metadatadiscovered").equals("1")) {
                currentChild.add(Integer.parseInt(temp.getAttribute("dic_table_record")));
            } else {
                currentChild.add("");
            }
            if (temp.getAttribute("dic_table_columndiscovered").equals("1")) {
                currentChild.add(Integer.parseInt(temp.getAttribute("dic_table_columncount")));
            } else {
                currentChild.add("");
            }
            currentChild.add(temp.getAttribute("dic_table_metadatadiscovered"));
            data.add(currentChild);
        }
        table = new KTable(data, columns);
        rightPanel.init("Tables", table);
        SqlEditorView sqlEditor = new SqlEditorView(connection);
        rightPanel.addTab("Sql Editor", sqlEditor, false);
        //todo add mapper tab
        MapperEditorView mapperEditorView = new MapperEditorView(connection);
        rightPanel.addTab("Mapper", mapperEditorView, false);
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
        if (node.getAttribute("dic_table_metadatadiscovered").equals("1")) {
            Enumeration<HierarchyTreeNode> children = node.children();
            String[] col = new String[]{"Name", "Data Type", "Size", "Is Primary Key"};
            Vector<String> columns = new Vector<String>(Arrays.asList(col));
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (children.hasMoreElements()) {
                Vector<Object> currentChild = new Vector<Object>();
                HierarchyTreeNode temp = children.nextElement();
                currentChild.add(temp.getAttribute("dic_column_name"));
                currentChild.add(temp.getAttribute("dic_column_type"));
                currentChild.add(Integer.parseInt(temp.getAttribute("dic_column_length")));
                if (temp.getAttribute("dic_table_timestamp") != null) {
                    currentChild.add(Long.parseLong(temp.getAttribute("dic_table_timestamp")));
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
            currentChild.add(temp.getAttribute("dic_instance_databasetype"));
            currentChild.add(temp.getAttribute("dic_instance_connectionname"));
            currentChild.add(temp.getAttribute("dic_instance_instancename"));
            currentChild.add(temp.getAttribute("dic_instance_portnumber"));
            currentChild.add(temp.getAttribute("dic_instance_systemname"));
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
                connection = DatabaseUtility.getConnection(instanceNode.getAttribute("dic_instance_systemname"), instanceNode.getAttribute("dic_instance_databasetype"), instanceNode.getAttribute("dic_instance_portnumber"), instanceNode.getAttribute("dic_instance_instancename"), instanceNode.getAttribute("dic_instance_username"), instanceNode.getAttribute("dic_instance_password"));
                leftHierarchy.setConnection(connection);
                leftHierarchy.getConnectionMap().put(instanceNode, connection);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
