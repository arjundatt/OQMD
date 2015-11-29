package DIC.util.database;

import DIC.util.commons.GenerateID;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by arjundatt.16 on 10/08/15.
 */
public class DatabaseUtility {
    static final String JDBC_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DERBY = "Derby";
    static final String ORACLE = "Oracle";
    public static Connection metadataConnection = null;

    static {
        try {
            metadataConnection = DatabaseUtility.getConnection("localhost", DERBY, "1527", "demo1", "demo1", "demo1");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

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
            ex.printStackTrace();
            System.out.println("Error: unable to load driver class!");
        }
        return connection;
    }

    public static Vector<Vector<String>> addSchemasToMetadatabase(Connection con, ArrayList<String> schemaNames, String instanceId) throws SQLException {
        Vector<Vector<String>> schemaDetails = new Vector<Vector<String>>();    //schemaId, schemaName
        try {
            String query = "Insert into DEMO1.Dic_Schema (" +
                    "                        Dic_Schema_ID," +
                    "                        Dic_Schema_Name," +
                    "                        Dic_Schema_HbaseTable," +
                    "                        Dic_Schema_Instance_ID)" +
                    "                        values";
            int noOfIdsRequired = schemaNames.size();
            //get a list of ids
            ArrayList<String> ids = GenerateID.generateMultipleIds(noOfIdsRequired);
            int i = 0;
            for (String schemaName : schemaNames) {
                Vector<String> schema = new Vector<String>();
                query += "('"
                        + ids.get(i) + "','"
                        + schemaName + "',"
                        + "0,'"
                        + instanceId + "'), ";
                schema.add(schemaName);
                schema.add(ids.get(i++));
                schema.add(instanceId);
                schemaDetails.add(schema);
            }
            query = query.substring(0, query.length() - 2);
            System.out.println(query);
            if (!query.equals("Insert into DEMO1.Dic_Schema (" +
                    "                                            Dic_Schema_ID," +
                    "                                            Dic_Schema_Name," +
                    "                                            Dic_Schema_HbaseTable," +
                    "                                            Dic_Schema_Instance_ID)" +
                    "                                            values"))
                DatabaseUtility.updateQuery(con, query);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return schemaDetails;
        //jbjkb
    }

    public static String addConnectionToMetadatabase(Connection con, String connectionName, String dbIp, String dbType, String dbPort, String instanceName, String userName, String password) throws SQLException {
        String connectionId = null;
        try {
            Statement st = con.createStatement();
            connectionId = GenerateID.generateID();
            String query = "Insert into Dic_Instance (Dic_Instance_ID," +
                    "Dic_Instance_DatabaseType," +
                    "Dic_Instance_InstanceName," +
                    "Dic_Instance_ConnectionName," +
                    "Dic_Instance_Password," +
                    "Dic_Instance_PortNumber," +
                    "Dic_Instance_SystemName," +
                    "Dic_Instance_UserName) " +
                    "values('" + connectionId + "','"
                    + dbType + "','"
                    + instanceName + "','"
                    + connectionName + "','"
                    + password + "',"
                    + dbPort + ",'"
                    + dbIp + "','"
                    + userName + "')";
            System.out.println(query);
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectionId;
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
        if (dbIP.equals("localhost") && metadataConnection != null && !metadataConnection.isClosed())        //returns metadata connection quickly
            return metadataConnection;
        if (dbType.equals(DERBY))
            return getDerbyConnection(dbIP, port, instance, userName, pass);
        else if (dbType.equals(ORACLE))
            return getOracleConnection(dbIP, port, instance, userName, pass);
        return null;
    }

    public static Vector<Vector<String>> getSchemas(Connection connection, String instanceId) {
        ArrayList<String> schemaNames = new ArrayList<String>();
        Vector<Vector<String>> schemaDetails = null;
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet schema = metadata.getSchemas();
            while (schema.next()) {
                schemaNames.add(schema.getString(1));

            }
            schemaDetails = addSchemasToMetadatabase(connection, schemaNames, instanceId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schemaDetails;
    }

    public static Vector<Vector<String>> getTables(Connection connection, String schemaName, String schemaId) {
        ArrayList<String> tableNames = new ArrayList<String>();
        Vector<Vector<String>> tableDetails = null;
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, schemaName, null, null);
            while (tables.next()) {
                tableNames.add(tables.getString(3));
                getMetaData(connection, schemaName, tableNames.get(tableNames.size() - 1));
            }
            tableDetails = addTablesToMetadatabase(connection, tableNames, schemaId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableDetails;
    }

    private static Vector<Vector<String>> addTablesToMetadatabase(Connection connection, ArrayList<String> tableNames, String schemaId) {
        Vector<Vector<String>> tableDetails = new Vector<Vector<String>>();    //schemaId, schemaName
        try {
            String query = "Insert ";
            query += " into DEMO1.Dic_Table (" +
                    "DIC_TABLE_ID, " +                    //1
                    "DIC_TABLE_COLUMNCOUNT, " +           //2
                    "DIC_TABLE_ISCOLUMNFAMILY, " +        //3
                    "DIC_TABLE_COLUMNDISCOVERED, " +      //4
                    "DIC_TABLE_METADATADISCOVERED, " +    //5
                    "DIC_TABLE_NAME, " +                  //6
                    "DIC_TABLE_RECORD, " +                //7
                    "DIC_TABLE_TIMESTAMP, " +             //8
                    "DIC_TABLE_SCHEMA_ID) values";
            int noOfIdsRequired = tableNames.size();
            //get a list of ids
            ArrayList<String> ids = GenerateID.generateMultipleIds(noOfIdsRequired);
            int i = 0;
            for (String tableName : tableNames) {
                Vector<String> table = new Vector<String>();
                String id = ids.get(i);
                long time = System.nanoTime();
                query += "('"
                        + id + "',"                   //1
                        + "0,"                               //2
                        + "0,"                               //3
                        + "0,"                                 //4
                        + "0,'"                                 //5
                        + tableName + "',"                                 //6
                        + "0,"                                 //7
                        + time + ",'"                                 //8
                        + schemaId + "'), ";                  //9

                table.add(ids.get(i++));
                table.add("0");
                table.add("0");
                table.add("0");
                table.add("0");
                table.add(tableName);
                table.add("0");
                table.add(String.valueOf(time));
                table.add(schemaId);
                tableDetails.add(table);
            }
            query = query.substring(0, query.length() - 2);
            System.out.println(query);
            if (!query.equals("Insert all"))
                DatabaseUtility.updateQuery(connection, query);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableDetails;
    }

    private static Vector<Vector<String>> addColumnsToMetadatabase(Connection connection, ArrayList<String> columnNames, String tableId) {
        Vector<Vector<String>> columnDetails = new Vector<Vector<String>>();    //schemaId, schemaName
        try {
            String query = "Insert ";
            query += " into DEMO1.Dic_Column (" +
                    "DIC_COLUMN_ID, " +
                    "DIC_COLUMN_LENGTH, " +
                    "DIC_COLUMN_NAME, " +
                    "DIC_COLUMN_TYPE, " +
                    "DIC_COLUMN_TABLE_ID) values";
            int noOfIdsRequired = columnNames.size();
            //get a list of ids
            ArrayList<String> ids = GenerateID.generateMultipleIds(noOfIdsRequired);
            int i = 0;
            for (String columnName : columnNames) {
                Vector<String> column = new Vector<String>();
                String id = ids.get(i);
                query += "('"
                        + id + "',"                   //1
                        + "0,'"                                 //2
                        + columnName + "',"                                 //3
                        + "'null','"                                        //4
                        + tableId + "'), ";                  //5

                column.add(ids.get(i++));
                column.add("0");
                column.add(columnName);
                column.add("");
                column.add(tableId);
                columnDetails.add(column);
            }
            query = query.substring(0, query.length() - 2);
            if (!query.equals("Insert all"))
                DatabaseUtility.updateQuery(connection, query);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnDetails;
    }

    public static void updateColumnDiscovered(Connection connection, String tableId) {
        String query = "update DEMO1.DIC_TABLE set DIC_TABLE_COLUMNDISCOVERED = 1 where DIC_TABLE_ID = '" + tableId + "'";
        try {
            DatabaseUtility.updateQuery(connection, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Vector<Vector<String>> getColumns(Connection connection, String schemaName, String tableName, String tableId) {
        ArrayList<String> columnNames = new ArrayList<String>();
        Vector<Vector<String>> columnDetails = null;
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet col = metadata.getColumns(null, schemaName, tableName, null);
            while (col.next()) {
                columnNames.add(col.getString(4));
            }
            columnDetails = addColumnsToMetadatabase(connection, columnNames, tableId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnDetails;
    }

    public static ArrayList<String> getColumns(Connection connection, String schemaName, String tableName) {
        ArrayList<String> columnNames = new ArrayList<String>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet col = metadata.getColumns(null, schemaName, tableName, null);
            while (col.next()) {
                columnNames.add(col.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnNames;
    }

    public static void updateColumnParameters(Connection connection, String columnId, String type, String length) {
        String query = "update DEMO1.DIC_COLUMN set DIC_COLUMN_TYPE = '" + type + "', DIC_COLUMN_LENGTH = " + Integer.parseInt(length) + " where DIC_COLUMN_ID = '" + columnId + "'";

        try {
            DatabaseUtility.updateQuery(connection, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTableParameters(Connection connection, String tableId, String col_count, String record_count) {
        String query = "update DEMO1.DIC_TABLE set DIC_TABLE_COLUMNCOUNT = " + Integer.parseInt(col_count) + ", DIC_TABLE_RECORD = " + Integer.parseInt(record_count) + ", DIC_TABLE_METADATADISCOVERED = 1 where DIC_TABLE_ID='" + tableId + "'";
        try {
            DatabaseUtility.updateQuery(connection, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public static int deleteTable(Connection connection, String schemaName, String tableName) throws SQLException {
        String sql = "drop table \"" + schemaName + "\".\"" + tableName + "\"";
        int ret = updateQuery(connection, sql);
        return ret;
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

    public static Vector executeQueryOnMetaDatabase(String sql) throws SQLException {
        return executeQuery(metadataConnection, sql);
    }

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = getConnection("192.168.33.163", "derby", "1530", "demo1", "demo1", "demo1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        getSchemas(connection);
    }
}
