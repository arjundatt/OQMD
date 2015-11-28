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
    public static final String IDENTITY = "rdbms";

    @Override
    public void initClassification(String tableID) {
        this.tableId = tableID;
        getDomains(IDENTITY);
        phaseI();
    }

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
                allColumnIds.add(column.get(1));
            }
            dataSQL = dataSQL.substring(0, dataSQL.length() - 2) +
                    " from NGARG.";
            String tableSQL = "select DIC_TABLE_NAME from NGARG.DIC_TABLE where DIC_TABLE_ID = '" + tableId + "'";
            Vector<Vector<String>> tableName = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(tableSQL);
            dataSQL += tableName.get(1).get(0);
            data = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(dataSQL);
            HashMap<String, ArrayList<String>> columnMap;
            columnMap = generateColumnMap(data, columns);
            super.phaseII(columnMap,IDENTITY,tableId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //after this call phase II of the super class
    }



    public static void main(String[] args) {
        RDBMSClassifier rdbmsClassifier = new RDBMSClassifier();
        rdbmsClassifier.initClassification("10012100");
        rdbmsClassifier.phaseI();

    }
}
