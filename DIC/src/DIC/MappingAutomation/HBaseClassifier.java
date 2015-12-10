package DIC.MappingAutomation;

import DIC.util.database.DatabaseUtility;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by arjundatt.16 on 11/25/2015.
 */
public class HBaseClassifier extends DomainClassifier{
    String tableId;
    Vector<Vector<String>> data;
    public static final String TABLE_NAME = "Citizens";
    public static final String COLUMN_FAMILY = "social_security_info";

    //todo:temporary variable, remove it and retrieve columns from DIC_COLUMNS instead
    String[] mHbaseColumns = {"Citizen_Name","Social_security_no","birthPlace","DOB","Citizenship","Gender","Mothername","Contact_info","Street_add","Region","Postal","Ethinicity","Annual_income"};

    public static final String IDENTITY = "HBase";

    @Override
    public void initClassification(String tableID) {
        this.tableId = tableID;
        getDomains(IDENTITY);
        phaseI();
        super.phaseIII();
    }

    @Override
    public void phaseI() {
        HashMap<String, ArrayList<String>> columnMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> columnIds = new ArrayList<String>();
        ArrayList<String> columnNames = new ArrayList<String>();
        String columnSQL = "select DIC_COLUMN_NAME, DIC_COLUMN_ID from NGARG.DIC_COLUMN where DIC_COLUMN_TABLE_ID = '" + tableId + "'";
        try {
            Vector<Vector<String>> columns = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(columnSQL);
            columns.remove(0); //removing column name
            for (Vector<String> column : columns) {
                columnNames.add(column.get(0));
                columnIds.add(column.get(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //columnID -> Arraylist(column data)
        //First retrieve the columns names/ids from DIC_COLUMN instead of using mHbaseColumns
        // Instantiating Configuration class
        Configuration config = HBaseConfiguration.create();
//        config.set("hbase.zookeeper.quorum", "localhost");
//        config.set("hbase.zookeeper.property.clientport", "2181");
        config.set("hbase.master","localhost:60000");
        System.out.println("step 2");
        try {
            // Instantiating HTable class
            HTable hTable = new HTable(config, TABLE_NAME);
            int i=1;
            while(true){
                // Instantiating Get class
                Get g = new Get(Bytes.toBytes("row"+i));

                // Reading the data
                Result result = hTable.get(g);
                if(result.isEmpty())
                    break;

                for(int k=0;k<columnNames.size();k++){
                    // Reading values from Result class object
                    String columnName = columnNames.get(k);
                    String columnId = columnIds.get(k);
                    String value = Bytes.toString(result.getValue(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(columnName)));
                    ArrayList<String> columnData;
                    if(columnMap.get(columnId)==null){
                        columnData = new ArrayList<String>();
                        columnMap.put(columnId,columnData);
                    }
                    else{
                        columnData = columnMap.get(columnId);
                    }
                    columnData.add(value);
                    columnMap.put(columnId,columnData);
                }
                i++;
            }
            super.phaseII(columnMap, IDENTITY,tableId);

        } catch (IOException e) {
            e.printStackTrace();
        }
        allColumnIds.addAll(columnIds);
    }

    public static void main(String[] a){

    }
}
