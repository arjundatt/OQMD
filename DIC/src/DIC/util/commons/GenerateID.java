package DIC.util.commons;

import DIC.util.database.DatabaseUtility;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * Created by nisthagarg on 10/14/15.
 */
public class GenerateID {
    public static String generateID(){
        try {
            Connection connection = DatabaseUtility.getConnection("ora.csc.ncsu.edu", "Oracle", "1521", "orcl", "ngarg", "200104701");
            Vector vector = DatabaseUtility.getData(connection, "ngarg", "Dic_ID");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        generateID();
    }
}
