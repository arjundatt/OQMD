package DIC.util.commons;

import DIC.util.database.DatabaseUtility;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by nishtha garg on 10/14/15.
 */
public class GenerateID {
    public static String generateID() {
        Integer id = null;
        try {
            Connection connection = DatabaseUtility.getConnection("ora.csc.ncsu.edu", "Oracle", "1521", "orcl", "ngarg", "200104701");
            Vector vector = DatabaseUtility.getData(connection, "NGARG", "DIC_ID");
            id = Integer.parseInt(((Vector) vector.get(0)).get(0).toString());
            String updateSQL = "update NGARG.DIC_ID set DIC_ID = " + (id + 1) + " where DIC_ID = " + id;
//            System.out.println(updateSQL);
            DatabaseUtility.updateQuery(connection, updateSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.valueOf(id);
    }

    public static void main(String[] args) {
        System.out.println(generateID());
    }
}
