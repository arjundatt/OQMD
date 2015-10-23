package DIC.component.lefthierarchy;

import DIC.util.database.DatabaseUtility;

import java.sql.SQLException;

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
    public void makeTree() {
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
                "       JOIN dic_schema \n" +
                "         ON dic_schema_instance_id = dic_instance_id \n" +
                "       JOIN dic_table \n" +
                "         ON dic_schema_id = dic_table_schema_id \n" +
                "       JOIN dic_column \n" +
                "         ON dic_column_table_id = dic_table_id ";

        try {
            DatabaseUtility.executeQueryOnMetaDatabase(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
