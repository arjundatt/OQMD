package DIC.component.lefthierarchy;

import DIC.component.treecomponent.HierarchyTreeNode;
import DIC.util.database.DatabaseUtility;

import java.sql.SQLException;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 10/22/2015
 * Time: 9:01 PM
 */
public class TreeMakerFromMetaDatabase {
    /**
     * This function generates the left hierarchy tree from the meta database
     */
    public static HierarchyTreeNode makeTree() {
        String query = "SELECT dic_instance_id, \n" +
                "       dic_instance_databasetype, \n" +
                "       dic_instance_instancename, \n" +
                "       dic_instance_connectionname, \n" +
                "       dic_instance_password, \n" +
                "       dic_instance_portnumber, \n" +
                "       dic_instance_systemname, \n" +
                "       dic_instance_username, \n" +

                "       dic_schema_id, \n" +
                "       dic_schema_name, \n" +
                "       dic_schema_hbasetable, \n" +
                "       dic_schema_instance_id, \n" +

                "       dic_table_id, \n" +
                "       dic_table_columncount, \n" +
                "       dic_table_iscolumnfamily, \n" +
                "       dic_table_columndiscovered, \n" +
                "       dic_table_metadatadiscovered, \n" +
                "       dic_table_name, \n" +
                "       dic_table_record, \n" +
                "       dic_table_timestamp, \n" +
                "       dic_table_schema_id, \n" +

                "       dic_column_id, \n" +
                "       dic_column_length, \n" +
                "       dic_column_name, \n" +
                "       dic_column_type, \n" +
                "       dic_column_table_id \n" +

                "FROM   dic_instance \n" +
                "       LEFT OUTER JOIN dic_schema \n" +
                "         ON dic_schema_instance_id = dic_instance_id \n" +
                "       LEFT OUTER JOIN dic_table \n" +
                "         ON dic_schema_id = dic_table_schema_id \n" +
                "       LEFT OUTER JOIN dic_column \n" +
                "         ON dic_column_table_id = dic_table_id ";

        HierarchyTreeNode root = null;
        try {
            Vector<Vector<String>> vector = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(query);
            root = new HierarchyTreeNode("root");
            Vector<String> columns = vector.get(0);
            vector.remove(0);
            for (Vector<String> row : vector) {
                // instance
                String dic_instance_id = row.get(0);
                String dic_instance_databasetype = row.get(1);
                String dic_instance_instancename = row.get(2);
                String dic_instance_connectionname = row.get(3);
                String dic_instance_password = row.get(4);
                String dic_instance_portnumber = String.valueOf(row.get(5));
                String dic_instance_systemname = row.get(6);
                String dic_instance_username = row.get(7);

                HierarchyTreeNode instanceNode = getChild(root, "dic_instance_id", dic_instance_id);
                if (instanceNode == null) {
                    instanceNode = new HierarchyTreeNode(dic_instance_connectionname);
                    instanceNode.setAttribute("dic_instance_id", dic_instance_id);
                    instanceNode.setAttribute("dic_instance_databasetype", dic_instance_databasetype);
                    instanceNode.setAttribute("dic_instance_instancename", dic_instance_instancename);
                    instanceNode.setAttribute("dic_instance_connectionname", dic_instance_connectionname);
                    instanceNode.setAttribute("dic_instance_password", dic_instance_password);
                    instanceNode.setAttribute("dic_instance_portnumber", dic_instance_portnumber);
                    instanceNode.setAttribute("dic_instance_systemname", dic_instance_systemname);
                    instanceNode.setAttribute("dic_instance_username", dic_instance_username);
                    root.add(instanceNode);
                }
                // schema
                String dic_schema_id = row.get(8);
                String dic_schema_name = row.get(9);
                String dic_schema_hbasetable = String.valueOf(row.get(10));
                String dic_schema_instance_id = row.get(11);

                HierarchyTreeNode schemaNode = getChild(instanceNode, "dic_schema_id", dic_schema_id);
                if (schemaNode == null) {
                    schemaNode = new HierarchyTreeNode(dic_schema_name);
                    schemaNode.setAttribute("dic_schema_id", dic_schema_id);
                    schemaNode.setAttribute("dic_schema_name", dic_schema_name);
                    schemaNode.setAttribute("dic_schema_hbasetable", dic_schema_hbasetable);
                    schemaNode.setAttribute("dic_schema_instance_id", dic_schema_instance_id);
                    instanceNode.add(schemaNode);
                }
                // table
                if (row.get(12) == null)
                    continue;
                String dic_table_id = row.get(12);
                String dic_table_columncount = String.valueOf(row.get(13));
                String dic_table_iscolumnfamily = String.valueOf(row.get(14));
                String dic_table_columndiscovered = String.valueOf(row.get(15));
                String dic_table_metadatadiscovered = String.valueOf(row.get(16));
                String dic_table_name = row.get(17);
                String dic_table_record = String.valueOf(row.get(18));
                String dic_table_timestamp = String.valueOf(row.get(19));
                String dic_table_schema_id = row.get(20);

                HierarchyTreeNode tableNode = getChild(schemaNode, "dic_table_id", dic_table_id);
                if (tableNode == null) {
                    tableNode = new HierarchyTreeNode(dic_table_name);
                    tableNode.setAttribute("dic_table_id", dic_table_id);
                    tableNode.setAttribute("dic_table_columncount", dic_table_columncount);
                    tableNode.setAttribute("dic_table_iscolumnfamily", dic_table_iscolumnfamily);
                    tableNode.setAttribute("dic_table_columndiscovered", dic_table_columndiscovered);
                    tableNode.setAttribute("dic_table_metadatadiscovered", dic_table_metadatadiscovered);
                    tableNode.setAttribute("dic_table_name", dic_table_name);
                    tableNode.setAttribute("dic_table_record", dic_table_record);
                    tableNode.setAttribute("dic_table_timestamp", dic_table_timestamp);
                    tableNode.setAttribute("dic_table_schema_id", dic_table_schema_id);
                    schemaNode.add(tableNode);
                }
                //columns
                if (row.get(21) == null)
                    continue;
                String dic_column_id = row.get(21);
                String dic_column_length = String.valueOf(row.get(22));
                String dic_column_name = row.get(23);
                String dic_column_type = row.get(24);
                String dic_column_table_id = row.get(25);

                HierarchyTreeNode columnNode = new HierarchyTreeNode(dic_column_name);
                columnNode.setAttribute("dic_column_id", dic_column_id);
                columnNode.setAttribute("dic_column_length", dic_column_length);
                columnNode.setAttribute("dic_column_name", dic_column_name);
                columnNode.setAttribute("dic_column_type", dic_column_type);
                columnNode.setAttribute("dic_column_table_id", dic_column_table_id);

                tableNode.add(columnNode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return root;
    }

    private static HierarchyTreeNode getChild(HierarchyTreeNode parentNode, String key, String instanceName) {
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            HierarchyTreeNode child = (HierarchyTreeNode) parentNode.getChildAt(i);
            if (child.getAttribute(key) != null && child.getAttribute(key).equals(instanceName))
                return child;
        }
        return null;
    }

    public static void main(String[] args) {
        makeTree();
    }
}
