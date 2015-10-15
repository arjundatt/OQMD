package DIC.DDLMapUtils;

//Use this library to access HBase Java Apis
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


/**
 * Created by arjundatt.16 on 10/15/2015.
 */


public class HBaseDDLMapper implements DDLMappingBase{

    /*  mQuery of of the form : SELECT \\S+ FROM \\S+(.*)
     */
    @Override
    public boolean insert(String mQuery) {
        String selectReg = "SELECT \\S+ FROM \\S+(.*)";
        if(mQuery.matches(selectReg)) {
            String[] components = mQuery.split("((\\s*SELECT\\s+)|(\\s*FROM\\s*))");
            String mColVals = "";
            String mTableName = "";

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

            Configuration config = HBaseConfiguration.create();
            try {
                HTable table = new HTable(config, mTableName);
                Get get = new Get();
                Result result = table.get(get);

//demo code starts
                // Reading values from Result class object
                byte [] value = result.getValue(Bytes.toBytes("colFamily1"),Bytes.toBytes("colName1"));

                byte [] value1 = result.getValue(Bytes.toBytes("colFamily2"),Bytes.toBytes("colName2"));

                String colValue1 = Bytes.toString(value);
                String colValue2 = Bytes.toString(value1);
//demo code ends
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String query(String mQuery) {
        return null;
    }

    @Override
    public int count(String mQuery) {
        return 0;
    }
}
