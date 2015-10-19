package DIC.Test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by arjundatt,16 on 10/17/15.
 */
public class HBaseTest {
    public static void main(String[] args) throws IOException, Exception{

        // Instantiating Configuration class
        Configuration config = HBaseConfiguration.create();

        // Instantiating HTable class
        HTable table = new HTable(config, "employee");

        // Instantiating Get class
        Get g = new Get(Bytes.toBytes("row1"));

        // Reading the data
        Result result = table.get(g);

        // Reading values from Result class object
        byte [] value = result.getValue(Bytes.toBytes("professional_data"),Bytes.toBytes("id"));

        byte [] value1 = result.getValue(Bytes.toBytes("professional_data"),Bytes.toBytes("name"));

        // Printing the values
        String id = Bytes.toString(value);
        String name = Bytes.toString(value1);

        System.out.println("id: " + id + " name: " + name);
    }
}
