package DIC.Test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * Created by root on 10/27/15.
 */
public class CreateHbaseTable {
    public static void main(String[] args) throws IOException {

        // Instantiating configuration class
        Configuration con = HBaseConfiguration.create();
//        con.set("hbase.zookeeper.quorum", "localhost");
//        con.set("hbase.zookeeper.property.clientport", "2181");
        con.set("hbase.master","localhost:60000");
        // Instantiating HbaseAdmin class
        HBaseAdmin admin = new HBaseAdmin(con);

        // Instantiating table descriptor class
        HTableDescriptor tableDescriptor = new HTableDescriptor("EmployeeAmerica");

        // Adding column families to table descriptor
        tableDescriptor.addFamily(new HColumnDescriptor("professional_data"));
        //tableDescriptor.addFamily(new HColumnDescriptor("professional"));

        // Execute the table through admin
        admin.createTable(tableDescriptor);
        System.out.println(" Table created ");
    }
}
