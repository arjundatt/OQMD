package DIC.Test;

import DIC.util.database.DatabaseUtility;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 10/16/2015
 * Time: 3:55 PM
 */
public class Test2 {
    public static void createTable() {
        /*
        CREATE TABLE emp (
 empno INT PRIMARY KEY,
 ename VARCHAR(10),
 job VARCHAR(9),
 mgr INT NULL,
 hiredate DATETIME,
 sal NUMERIC(7,2),
 comm NUMERIC(7,2) NULL,
 dept INT)
         */
        String query = "CREATE TABLE employee \n" +
                "  ( \n" +
                "     emp_id   VARCHAR2(25) PRIMARY KEY, \n" +
                "     emp_name VARCHAR2(400) \n" +
                "  ) ";
//        String insert = "INSERT INTO Dic_ID VALUES (100000)";


        //Statement st = null;
        try {
            /*Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = (Connection) DriverManager.getConnection("jdbc:oracle:thin:@ora.csc.ncsu.edu:1521:orcl", "ngarg", "200104701");
            */
            Connection connection = DatabaseUtility.getConnection("ora.csc.ncsu.edu", "Oracle", "1521", "orcl", "ngarg", "200104701");
            System.out.println(DatabaseUtility.updateQuery(connection, query));
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createTable();
    }
}
