package DIC.util.commons;

import DIC.util.database.DatabaseUtility;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by nishtha garg on 10/14/15.
 */
public class GenerateID {
    public static String generateID() {
        Integer id = null;
        try {
            Connection connection = DatabaseUtility.metadataConnection;
            Vector vector = DatabaseUtility.getData(connection, "DEMO1", "DIC_ID");
            id = Integer.parseInt(((Vector) vector.get(0)).get(0).toString());
            String updateSQL = "update DEMO1.DIC_ID set DIC_ID = '" + String.valueOf(id + 1) + "' where DIC_ID = '" + String.valueOf(id) + "'";
//            System.out.println(updateSQL);
            DatabaseUtility.updateQuery(connection, updateSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.valueOf(id);
    }

    public static ArrayList<String> generateMultipleIds(int noOfIdsRequired) {
        Integer id = null;
        ArrayList<String> ids = new ArrayList<String>();
        try {
            Connection connection = DatabaseUtility.metadataConnection;
            Vector vector = DatabaseUtility.getData(connection, "DEMO1", "DIC_ID");
            id = Integer.parseInt(((Vector) vector.get(0)).get(0).toString());
            String updateSQL = "update DEMO1.DIC_ID set DIC_ID = '" + (id + noOfIdsRequired) + "' where DIC_ID = '" + id + "'";
//            System.out.println(updateSQL);
            DatabaseUtility.updateQuery(connection, updateSQL);
            for (int i = id; i < id + noOfIdsRequired; i++) {
                ids.add(String.valueOf(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public static void main(String[] args) {
        System.out.println(generateID());
    }
}
