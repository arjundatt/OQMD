package DIC.util.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/8/13
 * Time: 6:46 PM
 */
public class DatabaseUtility {
    static final String JDBC_DRIVER = "com.ibm.db2.jcc.DB2Driver";

    public static Connection getConnection(String dbIP, String port, String instance, String userName, String pass) throws SQLException {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            String url = "jdbc:derby://" + dbIP + ":" + port + "/" + instance + ";ssl=basic";
            connection = DriverManager.getConnection(url, userName, pass);
            System.out.println("Connected to database");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
        }
        return connection;
    }

    public static Collection<String> getSchemas(Connection connection) {
        ArrayList<String> schemaNames = new ArrayList<String>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet schema = metadata.getSchemas();
            while (schema.next()) {
                schemaNames.add(schema.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schemaNames;
    }

    public static Collection<String> getTables(Connection connection, String schemaName) {
        ArrayList<String> tableNames = new ArrayList<String>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, schemaName, null, null);
            while (tables.next()) {
                tableNames.add(tables.getString(3));
                getMetaData(connection, schemaName, tableNames.get(tableNames.size() - 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    public static Collection<String> getColumns(Connection connection, String schemaName, String tableName) {
        ArrayList<String> columns = new ArrayList<String>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet col = metadata.getColumns(null, schemaName, tableName, null);
            while (col.next()) {
                columns.add(col.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    public static Vector<Vector<String>> getMetaData(Connection connection, String schemaName, String tableName) {
        Vector<Vector<String>> metaData = new Vector<Vector<String>>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet metadataColumns = metadata.getColumns(null, schemaName, tableName, null);
            while (metadataColumns.next()) {
                Vector<String> temp = new Vector<String>();
                temp.add(metadataColumns.getString(4));
                temp.add(metadataColumns.getString(6));
                temp.add(metadataColumns.getString(7));
                metaData.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metaData;
    }

    public static HashMap<String,String> getTableMetaData(Connection connection,String schemaName, String tableName ){
        HashMap<String,String> tableMetaData = new HashMap<String, String>();
        try {
            String sql = "select * from \"" + schemaName + "\".\"" + tableName + "\"";
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            tableMetaData.put("col_count", String.valueOf(resultSetMetaData.getColumnCount()));
            resultSet.last();
            tableMetaData.put("record_count", String.valueOf(resultSet.getRow()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableMetaData;
    }

    public static Vector getData(Connection connection, String schemaName, String tableName) {
        String sql = "select * from \"" + schemaName + "\".\"" + tableName + "\"";
        return executeQuery(connection, sql);
    }

    public static boolean updateQuery(Connection connection, String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        return true;
    }

    public static Vector executeQuery(Connection connection, String sql) {
        Vector<Vector<Object>> vector = new Vector<Vector<Object>>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            final int countColumns = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Vector<Object> temp = new Vector<Object>();
                for (int i = 1; i <= countColumns; i++) {
                    temp.add(resultSet.getObject(i));
                }
                vector.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vector;
    }



    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = getConnection("192.168.33.163", "1530", "demo1", "demo1", "demo1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getSchemas(connection);
    }
}
