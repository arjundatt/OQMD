package DIC.Test;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class UpdateData{

    public static void main(String[] args) throws IOException {

        System.out.println("step 1");
        // Instantiating Configuration class
        Configuration config = HBaseConfiguration.create();
        System.out.println("step 2");
        // Instantiating HTable class
        HTable hTable = new HTable(config, "employee");
        System.out.println("step 3");
        // Instantiating Put class
        //accepts a row name
        Put p = new Put(Bytes.toBytes("row1"));
        System.out.println("step 4");
        // Updating a cell value
        p.add(Bytes.toBytes("professional_data"),
                Bytes.toBytes("city"),Bytes.toBytes("Delhi"));
        System.out.println("step 5");
        // Saving the put Instance to the HTable.
        hTable.put(p);

        System.out.println("data Updated");

        // closing HTable
        hTable.close();
    }
}