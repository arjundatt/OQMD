package DIC.MappingAutomation;

import DIC.util.database.DatabaseUtility;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by arjundatt.16 on 11/3/2015.
 */
public class RDBMSClassifier extends DomainClassifier {
    String tableId;
    Vector<Vector<String>> data;


    public void initClassification(String tableId) {
        this.tableId = tableId;
        getDomains();
    }


    //Classify each attribute into n (priority)buckets(classifications like name, id, phone number, country, etc.)
    /* 0. Create n empty buckets: each corresponds to each classification
         * 1. Retrieve all column names in RDBMS
         * 2. Iterate through each column
         * 3. efficiency = 0
         * 4. Iterate through first 10 entries per column -> match regex/database from regex table for one entry.
         * 5. If efficiency<50%(random value -- need to test) -> match regex/database from regex table for next entry.
         * 6. If efficiency>90%(random value -- need to test) -> put this column in ith bucket with
         *    **** CL(confidence level) = efficiency and <AttributeIdentityModel value> and move to next column
         * 7. If efficiency>50%(random value -- need to test) -> pickup next 10 entries and repeat the process.
         * 8. If after 200(random value -- need to test) cells in a column -> 50% < efficiency < 100% then put in
         *    **** the ith bucket with CL(confidence level) = efficiency and <AttributeIdentityModel value> but
         *    **** check for other classifications.
         * 9. Insert unclassified columns into LIST_UNCLASSIFIED<AttributeIdentityModel>
         */

    /**
     * Initialization of the columnMap and regexMap
     */

    @Override
    public void phaseI() {
        String columnSQL = "select DIC_COLUMN_NAME, DIC_COLUMN_ID from NGARG.DIC_COLUMN where DIC_COLUMN_TABLE_ID = '" + tableId + "'";
        try {
            Vector<Vector<String>> columns = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(columnSQL);
            columns.remove(0); //removing column name
            String dataSQL = "select ";
            for (Vector<String> column : columns) {
                dataSQL += column.get(0) + ", ";
            }
            dataSQL = dataSQL.substring(0, dataSQL.length() - 2) +
                    " from NGARG.";
            String tableSQL = "select DIC_TABLE_NAME from NGARG.DIC_TABLE where DIC_TABLE_ID = '" + tableId + "'";
            Vector<Vector<String>> tableName = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(tableSQL);
            dataSQL += tableName.get(1).get(0);
            data = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(dataSQL);
            columnMap = generateColumnMap(data, columns);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //after this call phase II of the super class
    }



    public static void main(String[] args) {
        RDBMSClassifier rdbmsClassifier = new RDBMSClassifier();
        rdbmsClassifier.initClassification("10011888");
        rdbmsClassifier.phaseI();

    }
}
