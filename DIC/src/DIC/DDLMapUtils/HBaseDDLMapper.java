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
        return false;
    }

    @Override
    public String query(String mQuery) {

        String selectReg = "SELECT \\S+ FROM \\S+(.*)";
        StringBuilder val = new StringBuilder();
        if(mQuery.matches(selectReg)) {
            String[] components = mQuery.split("((\\s*SELECT\\s+)|(\\s*FROM\\s*))");
            String mColVals = "";
            String mTableName = "";
            String[] mColValsList={""};

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

            //read all columns
            if("*".equals(mColVals)){

            }
            //read specific columns
            else{
                mColValsList = mColVals.split("\\s*,\\s*");
            }
            Configuration config = HBaseConfiguration.create();
            try {
                HTable table = new HTable(config, mTableName);

                //Test: fetching data from single row only for now
                Get get = new Get(Bytes.toBytes("row1"));

                Result result = table.get(get);

//test code starts
                // Reading values from Result class object
                for(String cols : mColValsList) {
                    byte[] value = result.getValue(Bytes.toBytes("colFamily"), Bytes.toBytes(cols));
                    val.append(Bytes.toString(value) + "\n");
                }
//test code ends
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return val.toString();
    }

    @Override
    public int count(String mQuery) {
        return 0;
    }
}
