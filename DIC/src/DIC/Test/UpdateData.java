package DIC.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class UpdateData{

    static final String[] columnNames ={"Citizen_Name","Social_security_no","birthPlace","DOB","Citizenship","Gender","Mothername","Contact_info","Street_add","Region","Postal","Ethinicity","Annual_income"};
    static final String columnFamily = "social_security_info";
    static final String table_name = "Citizens";
    public static void main(String[] args) throws IOException {

        System.out.println("step 1");
        // Instantiating Configuration class
        Configuration config = HBaseConfiguration.create();
//        config.set("hbase.zookeeper.quorum", "localhost");
//        config.set("hbase.zookeeper.property.clientport", "2181");
        config.set("hbase.master","localhost:60000");
        System.out.println("step 2");
        // Instantiating HTable class
        HTable hTable = new HTable(config, table_name);
        System.out.println("step 3");

        //Create object of FileReader
        FileReader inputFile = new FileReader("/home/nishtha/Downloads/hbasedata.txt");

        //Instantiate the BufferedReader Class
        BufferedReader bufferReader = new BufferedReader(inputFile);

        //Variable to hold the one line data
        String line;
        int i=1;

        // Read file line by line and print on the console
        while ((line = bufferReader.readLine()) != null)   {

            String[] part = line.split("\t");

            // Instantiating Put class
            //accepts a row name
            Put p = new Put(Bytes.toBytes("row"+i));
            System.out.println("step 4");
            // Updating a cell value
            int j=0;
            for(String column : columnNames){
                p.add(Bytes.toBytes(columnFamily),
                        Bytes.toBytes(column),Bytes.toBytes(part[j]));
                j++;
            }
//            p.add(Bytes.toBytes("professional_data"),
//                    Bytes.toBytes("id"),Bytes.toBytes(part[0]));
//            p.add(Bytes.toBytes("professional_data"),
//                    Bytes.toBytes("name"), Bytes.toBytes(part[1]));

            // Saving the put Instance to the HTable.
            hTable.put(p);
            i++;

        }
        System.out.println("data Updated");

        // closing HTable
        hTable.close();
    }
}