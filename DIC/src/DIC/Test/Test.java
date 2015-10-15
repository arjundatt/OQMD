package DIC.Test;

import DIC.util.database.DatabaseUtility;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by nisthagarg on 10/14/15.
 */
public class Test {
    public static void createTable() {
        /*String query = "CREATE TABLE" + "Dic_Instance" + "(" +
                "Dic_Instance_ID INTEGER NOT NULL,"
                + "Dic_Instance_DatabaseType VARCHAR(20)  NOT NULL, "
                + "Dic_Instance_InstanceName VARCHAR(20) NOT NULL, "
                + "Dic_Instance_ConnectionName VARCHAR(40) NOT NULL, "
                + "Dic_Instance_Password VARCHAR(20) NOT NULL, "
                + "Dic_Instance_PortNumber INTEGER NOT NULL, "
                + "Dic_Instance_SystemName VARCHAR(40) NOT NULL, "
                + "Dic_Instance_UserName VARCHAR(40) NOT NULL, "
                + "PRIMARY KEY (Dic_Instance_ID))";
        String insert = "INSERT INTO Dic_Instance VALUES (1,'a','b','c','d',2,'g','h')";*/
        String query = "CREATE TABLE Dic_ID(Dic_ID INTEGER)";
        String insert = "INSERT INTO Dic_ID VALUES (100000)";


        //Statement st = null;
        try {
            /*Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl", "ngarg", "200104701");
            */
            Connection connection = DatabaseUtility.getConnection("ora.csc.ncsu.edu", "Oracle", "1521", "orcl", "ngarg", "200104701");
            Statement st = connection.createStatement();
            st.execute(query);
            st.executeUpdate(insert);
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
