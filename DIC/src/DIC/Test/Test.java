package DIC.Test;

import DIC.util.database.DatabaseUtility;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by nisthagarg on 10/14/15.
 */
public class Test {
    public static void createTable() {
        String query1 ="CREATE TABLE Dic_Instance (" + "Dic_Instance_ID VARCHAR(400) NOT NULL,"
                + "Dic_Instance_DatabaseType VARCHAR(400)  NOT NULL, "
                + "Dic_Instance_InstanceName VARCHAR(400) NOT NULL, "
                + "Dic_Instance_ConnectionName VARCHAR(400) NOT NULL, "
                + "Dic_Instance_Password VARCHAR(400) NOT NULL, "
                + "Dic_Instance_PortNumber INTEGER NOT NULL, "
                + "Dic_Instance_SystemName VARCHAR(400) NOT NULL, "
                + "Dic_Instance_UserName VARCHAR(400) NOT NULL, "
                + "PRIMARY KEY (Dic_Instance_ID))";

        String query2 = "CREATE TABLE Dic_Schema (" + "Dic_Schema_ID VARCHAR(400) NOT NULL,"
                + "Dic_Schema_Name VARCHAR(400) NOT NULL,"
                + "Dic_Schema_HbaseTable INTEGER NOT NULL ,"
                + "Dic_Schema_Instance_ID VARCHAR(400) NOT NULL,"
                + "PRIMARY KEY (Dic_Schema_ID))";

        String query3 = "CREATE TABLE Dic_Table (" +
                "Dic_Table_ID VARCHAR(400) NOT NULL,"
                + "Dic_Table_ColumnCount INTEGER  NOT NULL, "
                + "Dic_Table_IsColumnFamily INTEGER  NOT NULL, "
                + "Dic_Table_ColumnDiscovered INTEGER NOT NULL, "
                + "Dic_Table_MetadataDiscovered INTEGER NOT NULL, "
                + "Dic_Table_name VARCHAR(400) NOT NULL, "
                + "Dic_Table_record INTEGER NOT NULL, "
                + "Dic_Table_TimeStamp INTEGER NOT NULL, "
                + "Dic_Table_Schema_ID VARCHAR(400) NOT NULL,"
                + "PRIMARY KEY (Dic_Table_ID))";

        String query4 = "CREATE TABLE Dic_Column (" + "Dic_Column_ID VARCHAR(400) NOT NULL,"
                + "Dic_Column_Length INTEGER  NOT NULL, "
                + "Dic_Column_Name VARCHAR(400) NOT NULL, "
                + "Dic_Column_type VARCHAR(400) NOT NULL, "
                + "Dic_Column_Table_ID VARCHAR(400) NOT NULL,"
                + "PRIMARY KEY (Dic_Column_ID))";

        //string insert = "INSERT INTO Dic_Instance VALUES (1,'a','b','c','d',2,'g','h')";*/
        //String query = "CREATE TABLE Dic_ID(Dic_ID INTEGER)";
        //String insert = "INSERT INTO Dic_ID VALUES (100000)";
        //String delete= "DROP TABLE Dic_Instance";

        //Statement st = null;
        try {
            /*Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl", "ngarg", "200104701");
            */
            Connection connection = DatabaseUtility.getConnection("ora.csc.ncsu.edu", "Oracle", "1521", "orcl", "ngarg", "200104701");
            Statement st = connection.createStatement();
            //DatabaseUtility.deleteTable(connection,"NGARG","DIC_INSTANCE");
            //st.execute(query1);
            //st.execute(query2);
            //st.execute(query3);
            st.execute(query4);
            System.out.println("ok");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*finally
            {
            if (st != null) { st.close(); }
            }*/
    }

    public static void main(String[] args) {
        createTable();
    }
}
