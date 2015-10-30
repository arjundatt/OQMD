package DIC.DDLMapUtils;

//Use this library to access HBase Java Apis
import gudusoft.gsqlparser.pp.utils.SourceTokenConstant;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.Bytes;

import javax.xml.bind.SchemaOutputResolver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by arjundatt.16 on 10/15/2015.
 */


public class HBaseDDLMapper implements DDLMappingBase{

    //TODO test main: remove it later!!!
    public static void main(String[] a){
        String mQuery = "SELECT id,name FROM EmployeeAmerica";
        HBaseDDLMapper obj = new HBaseDDLMapper();
        System.out.print(obj.query(mQuery));
    }


    //manage this to change dynamically latter!!!
    private static final int MAX_COLUMNS = 10;

    /*  mQuery of of the form : SELECT \\S+ FROM \\S+(.*)
     */
    @Override
    public boolean insert(String mQuery) {
        return false;
    }

    public Vector<Vector<Object>> query(ArrayList<String> columnNames, String columnFamily, String mTableName) {
        if ("".equals(columnFamily))
            columnFamily = "professional_data";
        if ("".equals(mTableName))
            mTableName = "EmployeeAmerica";
        Vector<Vector<Object>> data = new Vector<Vector<Object>>(columnNames.size());
        StringBuilder concatResult = new StringBuilder();
        Configuration config = HBaseConfiguration.create();
        try {
            HTable table = new HTable(config, mTableName);
            int counter = 1;

            do {
                boolean end = false;
                Get get = new Get(Bytes.toBytes("row" + counter));
                Result result = table.get(get);
                Vector<Object> rowData=new Vector<Object>();
                for (String name : columnNames) {
                    // Reading values from Result class object
                    byte[] value = result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(name));
                    if (value == null) {
                        end = true;
                        break;
                    }
                    rowData.add(Bytes.toString(value));
                    concatResult.append(Bytes.toString(value) + "  ");
                }
                if (end) {
                    break;
                }
                data.add(counter-1,rowData);
                concatResult.append("\n");
                counter++;

            } while (true);
        } catch (IOException e) {
            e.printStackTrace();

        }
        System.out.println(concatResult.toString());
        return data;
    }

    @Override
    public String query(String mQuery) {

        String selectReg = "SELECT \\S+ FROM \\S+(.*)";


        if(mQuery.matches(selectReg)) {
            String[] components = mQuery.split("((\\s*SELECT\\s+)|(\\s*FROM\\s*))");
            String mColVals = "";
            String mTableName = "";
            String[] mColValsArr = new String[MAX_COLUMNS];
            StringBuilder concatResult = new StringBuilder();

            int numberOfColumns=0;

            for(String str : components){
                if(str.matches("\\S+")){
                    if(mColVals.equals("")){
                        mColVals = str;
                    }
                    else if(mTableName.equals("")){
                        mTableName = str;
                    }
                }
            }
            if(mColVals.contains(",")){
                mColValsArr = mColVals.split("\\s*,\\s*");
                numberOfColumns=mColVals.split("\\s*,\\s*").length;
            }
            else{
                mColValsArr[0]=mColVals;
                numberOfColumns=1;
            }
            Configuration config = HBaseConfiguration.create();
            try {
                HTable table = new HTable(config, mTableName);
                int counter=1;
                do{
                    boolean end = false;
                    Get get = new Get(Bytes.toBytes("row"+counter));
                    counter++;
                    Result result = table.get(get);
                    for(int i=0;i<numberOfColumns;i++){
                        // Reading values from Result class object
                        byte [] value = result.getValue(Bytes.toBytes("professional_data"),Bytes.toBytes(mColValsArr[i]));
                        if(value==null){
                            end=true;
                            break;
                        }
                        concatResult.append(Bytes.toString(value)+"  ");
                    }
                    if(end){
                        break;
                    }
                    concatResult.append("\n");
                } while(true);
            } catch (IOException e) {
                e.printStackTrace();

            }
            return concatResult.toString();
        }
        return null;
    }

    @Override
    public int count(String mQuery) {
        return 0;
    }
}
