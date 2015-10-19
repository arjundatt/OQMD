package DIC.component.lefthierarchy;

import DIC.component.AddConnection;
import DIC.component.formcomponent.PasswordField;
import DIC.component.formcomponent.TextField;
import DIC.component.treecomponent.HierarchyTreeNode;
import DIC.util.database.DatabaseUtility;
import DIC.xml.XMLTree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;

/**
 * Created by Arnab Saha on 10/10/15.
 */

public class LeftHierarchy extends JPanel implements ActionListener, KeyListener {
    private Map<HierarchyTreeNode, Connection> connectionMap = new HashMap<HierarchyTreeNode, Connection>();
    private JTree jTree;
    private JPopupMenu rootPopUp;
    private JPopupMenu instancePopUp;
    private JPopupMenu schemaPopUp;
    private JPopupMenu tablePopUp;
    private JMenuItem temp;
    private HierarchyTreeNode root;
    private JScrollPane scrollPane;
    private AddConnection addConnection;
    private Connection connection;

    public LeftHierarchy() {
        setLayout(new BorderLayout());
        XMLTree xmlTree = new XMLTree();
        root = xmlTree.xmlToTree();
        createLeftHierarchy();
    }

    private void createLeftHierarchy() {
        jTree = new JTree(root);
        jTree.setBorder(BorderFactory.createEmptyBorder(2, 10, 10, 10));
        scrollPane = new JScrollPane(jTree);
        add(scrollPane);
        rootPopUp = new JPopupMenu();
        temp = new JMenuItem("Add Connection");
        temp.setIcon(new ImageIcon(getClass().getResource("../../resource/Connection_manager_16.gif")));
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConnectionDialog();
            }
        });
        rootPopUp.add(temp);
        instancePopUp = new JPopupMenu();
        temp = new JMenuItem("Discover Schemas");
        temp.setIcon(new ImageIcon(getClass().getResource("../../resource/Discover_schemas_16.gif")));
        temp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                HierarchyTreeNode selectedNode = (HierarchyTreeNode) jTree.getLastSelectedPathComponent();
                callConnectionFromInstanceNode(selectedNode);
                if (selectedNode.getChildCount() == 0) {
                    ArrayList<String> result = (ArrayList<String>) DatabaseUtility.getSchemas(connection/*, instanceId*/);
                    for (String schema : result) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", schema);
                        HierarchyTreeNode newChild = new HierarchyTreeNode(schema, map);
                        selectedNode.add(newChild);
                    }
                    jTree.expandPath(new TreePath(selectedNode.getPath()));
                    updateUI();
                }
            }
        });
        instancePopUp.add(temp);
        schemaPopUp = new JPopupMenu();
        temp = new JMenuItem("Discover Tables");
        temp.setIcon(new ImageIcon(getClass().getResource("../../resource/Discover_tables_16.gif")));
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HierarchyTreeNode selectedNode = (HierarchyTreeNode) jTree.getLastSelectedPathComponent();
                HierarchyTreeNode instanceNode = (HierarchyTreeNode) selectedNode.getParent();
                callConnectionFromInstanceNode(instanceNode);
                if (selectedNode.getChildCount() == 0) {
                    ArrayList<String> result = (ArrayList<String>) DatabaseUtility.getTables(connection, selectedNode.toString());
                    for (String table : result) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("metadata_discovered", "false");
                        map.put("column_discovered", "false");
                        map.put("name", table);
                        map.put("time_stamp", String.valueOf(System.nanoTime()));
                        HierarchyTreeNode newChild = new HierarchyTreeNode(table, map);
                        selectedNode.add(newChild);
                    }
                    jTree.expandPath(new TreePath(selectedNode.getPath()));
                    updateUI();
                }
            }
        });
        schemaPopUp.add(temp);
        temp = new JMenuItem("Create Table");
        temp.setIcon(new ImageIcon(getClass().getResource("../../resource/createTable.gif")));
        schemaPopUp.add(temp);

        /* Discover Columns */
        tablePopUp = new JPopupMenu();
        temp = new JMenuItem("Discover Columns");
        temp.setIcon(new ImageIcon(getClass().getResource("../../resource/discover_column_16.gif")));
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HierarchyTreeNode selectedNode = (HierarchyTreeNode) jTree.getLastSelectedPathComponent();
                HierarchyTreeNode instanceNode = (HierarchyTreeNode) selectedNode.getParent().getParent();
                callConnectionFromInstanceNode(instanceNode);
                if (selectedNode.getChildCount() == 0) {
                    ArrayList<String> result = (ArrayList<String>) DatabaseUtility.getColumns(connection, selectedNode.getParent().toString(), selectedNode.toString());
                    for (String column : result) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", column);
                        HierarchyTreeNode newChild = new HierarchyTreeNode(column, map);
                        selectedNode.add(newChild);
                    }
                    selectedNode.setAttribute("column_discovered", "true");
                    jTree.expandPath(new TreePath(selectedNode.getPath()));
                    updateUI();
                }
            }
        });
        tablePopUp.add(temp);

        temp = new JMenuItem("Discover Metadata");
        temp.setIcon(new ImageIcon(getClass().getResource("../../resource/Discover_schemas_16.gif")));
        temp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                HierarchyTreeNode selectedNode = (HierarchyTreeNode) jTree.getLastSelectedPathComponent();
                HierarchyTreeNode instanceNode = (HierarchyTreeNode) selectedNode.getParent().getParent();
                callConnectionFromInstanceNode(instanceNode);
                if (Boolean.valueOf(selectedNode.getAttribute("metadata_discovered"))) {
                    JOptionPane.showMessageDialog(getTopLevelAncestor(), "Metadata Already Discovered", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (Boolean.valueOf(selectedNode.getAttribute("column_discovered"))) {
                        Vector<Vector<Object>> result = DatabaseUtility.getMetaData(connection, selectedNode.getParent().toString(), selectedNode.toString());
                        Enumeration children = selectedNode.children();
                        int index = 0;
                        while (children.hasMoreElements()) {
                            HierarchyTreeNode colNode = (HierarchyTreeNode) children.nextElement();
                            Vector<Object> colMetaData = result.elementAt(index);
                            colNode.setAttribute("type", colMetaData.elementAt(1).toString());
                            colNode.setAttribute("length", colMetaData.elementAt(2).toString());
                            index++;
                        }
                        HashMap<String, String> tableMetaData = DatabaseUtility.getTableMetaData(connection, selectedNode.getParent().toString(), selectedNode.toString());
                        selectedNode.setAttribute("column_count", tableMetaData.get("col_count"));
                        selectedNode.setAttribute("record_count", tableMetaData.get("record_count"));
                        jTree.expandPath(new TreePath(selectedNode.getPath()));
                        selectedNode.setAttribute("metadata_discovered", "true");
                        updateUI();
                    } else {
                        JOptionPane.showMessageDialog(getTopLevelAncestor(), "Column not Discovered", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        tablePopUp.add(temp);

        temp = new JMenuItem("Edit Table");
        temp.setIcon(new ImageIcon(getClass().getResource("../../resource/discover_relationship_16.gif")));
        tablePopUp.add(temp);

        jTree.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HierarchyTreeNode node = (HierarchyTreeNode) jTree.getLastSelectedPathComponent();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    try {
                        if (node != null) {
                            if (node.getLevel() == 0) {
                                rootPopUp.show(jTree, e.getX(), e.getY());
                            } else if (node.getLevel() == 1) {
                                instancePopUp.show(jTree, e.getX(), e.getY());
                            } else if (node.getLevel() == 2) {
                                schemaPopUp.show(jTree, e.getX(), e.getY());
                            } else if (node.getLevel() == 3) {
                                tablePopUp.show(jTree, e.getX(), e.getY());
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }


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
        });

        jTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                HierarchyTreeNode node = (HierarchyTreeNode) value;
                if (node.getLevel() == 0) {
                    setIcon(new ImageIcon(getClass().getResource("../../resource/root_tree.gif")));
                } else if (node.getLevel() == 1) {
                    setIcon(new ImageIcon(getClass().getResource("../../resource/instance_16.gif")));
                } else if (node.getLevel() == 2) {
                    setIcon(new ImageIcon(getClass().getResource("../../resource/schema_16.gif")));
                } else if (node.getLevel() == 3) {
                    setIcon(new ImageIcon(getClass().getResource("../../resource/table_16.gif")));
                } else if (node.getLevel() == 4) {
                    setIcon(new ImageIcon(getClass().getResource("../../resource/column_16.gif")));
                }

                return comp;
            }
        });
        jTree.setSelectionPath(new TreePath(root));
    }

    public static void main(String[] args) {
        LeftHierarchy lf = new LeftHierarchy();
        JFrame frame = new JFrame("test tree");
        frame.add(lf);
        HierarchyTreeNode root1 = (HierarchyTreeNode) lf.jTree.getModel().getRoot();
        root1.add(new HierarchyTreeNode("test", null));
        lf.jTree = new JTree(root1);
        lf.jTree.updateUI();
        lf.updateUI();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public HierarchyTreeNode addConnection(AddConnection temp) {
        DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("database", temp.getDbTypeComboBox().getSelectedItem().toString());
        map.put("instance", temp.getInstanceName().getText());
        map.put("name", temp.getConnectionName().getText());
        map.put("uname", temp.getUserName().getText());
        map.put("pass", temp.getPassword().getText());
        map.put("port", temp.getPort().getText());
        map.put("system", temp.getSystem().getText());
        HierarchyTreeNode instanceNode = new HierarchyTreeNode(temp.getConnectionName().getText(), map);
        root.add(instanceNode);
        model.reload(root);
        updateUI();
        return instanceNode;
    }

    private void showConnectionDialog() {
        addConnection = new AddConnection((JFrame) this.getTopLevelAncestor());
        JButton ok = addConnection.getOk();
        ok.addActionListener(this);
        TextField connectionName = addConnection.getConnectionName();
        connectionName.getTextField().addKeyListener(this);
        TextField system = addConnection.getSystem();
        system.getTextField().addKeyListener(this);
        TextField instanceName = addConnection.getInstanceName();
        instanceName.getTextField().addKeyListener(this);
        TextField port = addConnection.getPort();
        port.getTextField().addKeyListener(this);
        TextField userName = addConnection.getUserName();
        userName.getTextField().addKeyListener(this);
        PasswordField passwordField = addConnection.getPassword();
        passwordField.getPasswordField().addKeyListener(this);
    }

    public JTree getjTree() {
        return jTree;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setConnectionMap(HierarchyTreeNode hierarchyTreeNode, Connection connection1) {
        connectionMap.put(hierarchyTreeNode, connection1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (addConnection.validateFields())
            getConnectionCall();
        else
            addConnection.setErrorDisplay();
        addConnection.getMessageDisplay().updateUI();
    }

    private void getConnectionCall() {
        try {
            connection = DatabaseUtility.getConnection(addConnection.getSystem().getText(), addConnection.getDbTypeComboBox().getSelectedItem().toString(), addConnection.getPort().getText(), addConnection.getInstanceName().getText(), addConnection.getUserName().getText(), addConnection.getPassword().getText());
            HierarchyTreeNode instanceNode = addConnection(addConnection);
            connectionMap.put(instanceNode, connection);
            addConnection.close();
            //add the connection parameters to db
            DatabaseUtility.addConnectionToMetadatabase(addConnection.getConnectionName().getText(),addConnection.getSystem().getText(), addConnection.getDbTypeComboBox().getSelectedItem().toString(), addConnection.getPort().getText(), addConnection.getInstanceName().getText(), addConnection.getUserName().getText(), addConnection.getPassword().getText());

        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

    public Map<HierarchyTreeNode, Connection> getConnectionMap() {
        return connectionMap;
    }

    /**
     * This function takes the instance node as a parameter and calls the specific connection from the instanceNode if
     * that connection is not present in the connection map.
     *
     * @param instanceNode instance Node
     */
    private void callConnectionFromInstanceNode(HierarchyTreeNode instanceNode) {
        connection = connectionMap.get(instanceNode);
        if (connection == null) {
            try {
                connection = DatabaseUtility.getConnection(instanceNode.getAttribute("system"), instanceNode.getAttribute("database"), instanceNode.getAttribute("port"), instanceNode.getAttribute("instance"), instanceNode.getAttribute("uname"), instanceNode.getAttribute("pass"));
                getConnectionMap().put(instanceNode, connection);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            if (addConnection.validateFields())
                getConnectionCall();
            else
                addConnection.setErrorDisplay();
        addConnection.getMessageDisplay().updateUI();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
