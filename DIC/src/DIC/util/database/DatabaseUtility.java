package DIC.util.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class DatabaseUtility {
    static final String JDBC_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DERBY = "Derby";
    static final String ORACLE = "Oracle";

    /*Dic_Instance_ID INTEGER NOT NULL,"
                + "Dic_Instance_DatabaseType VARCHAR(20)  NOT NULL, "
                + "Dic_Instance_InstanceName VARCHAR(20) NOT NULL, "
                + "Dic_Instance_ConnectionName VARCHAR(40) NOT NULL, "
                + "Dic_Instance_Password VARCHAR(20) NOT NULL, "
                + "Dic_Instance_PortNumber INTEGER NOT NULL, "
                + "Dic_Instance_SystemName VARCHAR(40) NOT NULL, "
                + "Dic_Instance_UserName VARCHAR(40) NOT NULL, "*/
    public static Connection getDerbyConnection(String dbIP, String port, String instance, String userName, String pass) throws SQLException {
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

    public static void addConnectionToMetadatabase(String connectionName,String dbIp, String dbType, String dbPort, String instanceName, String userName, String password) throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl", "ngarg", "200104701");
            Statement st = con.createStatement();
            String query = "Insert into Dic_Instance (Dic_Instance_ID," +
                    "Dic_Instance_DatabaseType," +
                    "Dic_Instance_InstanceName," +
                    "Dic_Instance_ConnectionName," +
                    "Dic_Instance_Password," +
                    "Dic_Instance_PortNumber," +
                    "Dic_Instance_SystemName," +
                    "Dic_Instance_UserName) "+
                    "values(2,'"
                    +dbType+"','"
                    +instanceName+"','"
                    +connectionName+"','"
                    +password+"','"
                    +dbPort+"','"
                    +dbIp+"','"
                    +userName+"')";
            st.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static Connection getOracleConnection(String dbIP, String port, String instance, String userName, String pass) throws SQLException {
        Connection connection = null;
        try {
            Class.forName(ORACLE_DRIVER);
            String url = "jdbc:oracle:thin:@" + dbIP + ":" + port + ":" + instance;
            System.out.println("URL = " + url);
            connection = DriverManager.getConnection(url, userName, pass);
            System.out.println("Connected to database");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
        }
        return connection;
    }

    public static Connection getConnection(String dbIP, String dbType, String port, String instance, String userName, String pass) throws SQLException {
        if (dbType.equals(DERBY))
            return getDerbyConnection(dbIP, port, instance, userName, pass);
        else if (dbType.equals(ORACLE))
            return getOracleConnection(dbIP, port, instance, userName, pass);
        return null;
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

    public static Vector<Vector<Object>> getMetaData(Connection connection, String schemaName, String tableName) {
        Vector<Vector<Object>> metaData = new Vector<Vector<Object>>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet metadataColumns = metadata.getColumns(null, schemaName, tableName, null);
            ResultSet primaryKey = metadata.getPrimaryKeys(null, schemaName, tableName);
            while (metadataColumns.next()) {
                Vector<Object> temp = new Vector<Object>();
                temp.add(metadataColumns.getString(4));    //0
                temp.add(metadataColumns.getString(6));    //1
                temp.add(metadataColumns.getString(7));     //2
                temp.add(false);                            //3
                //TODO is primary key not working
                /*if (primaryKey.getString(5).equals(metadataColumns.getString(4))) {
                    temp.add(true);
                } else
                    temp.add(false);*/
                metaData.add(temp);
            }
            int pkIndex = 0;
            while (primaryKey.next()) {
                String colName = primaryKey.getString(6);
                System.out.println("name = " + colName);
                String pkcolIndex = primaryKey.getString(5);
                System.out.println("index = " + pkcolIndex);
                System.out.println("------------------------------------------------");
                if (pkcolIndex != null || pkcolIndex.isEmpty())
                    pkIndex = Integer.parseInt(pkcolIndex);
            }
            if (pkIndex != 0) {
                Vector<Object> meta = metaData.elementAt(pkIndex - 1);
                meta.set(3, true);
                metaData.set(pkIndex - 1, meta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metaData;
    }

    public static HashMap<String, String> getTableMetaData(Connection connection, String schemaName, String tableName) {
        HashMap<String, String> tableMetaData = new HashMap<String, String>();
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

    public static Vector getData(Connection connection, String schemaName, String tableName) throws SQLException {
        String sql = "select * from \"" + schemaName + "\".\"" + tableName + "\"";
        Vector vector = executeQuery(connection, sql);
        vector.remove(0);
        return vector;
    }

    public static int updateQuery(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        int i = statement.executeUpdate(sql);
        return i;
    }

    public static Vector executeQuery(Connection connection, String sql) throws SQLException {
        Vector<Vector<Object>> vector = new Vector<Vector<Object>>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        final int countColumns = resultSetMetaData.getColumnCount();
        Vector<Object> columns = new Vector<Object>(countColumns);
        for (int i = 1; i <= countColumns; i++) {
            columns.add(resultSetMetaData.getColumnName(i));
        }
        vector.add(columns);
        while (resultSet.next()) {
            Vector<Object> temp = new Vector<Object>();
            for (int i = 1; i <= countColumns; i++) {
                temp.add(resultSet.getObject(i));
            }
            vector.add(temp);
        }
        return vector;
    }


    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = getConnection("192.168.33.163", "derby", "1530", "demo1", "demo1", "demo1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getSchemas(connection);
    }
}
