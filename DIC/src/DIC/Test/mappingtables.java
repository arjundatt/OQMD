package DIC.Test;
import DIC.util.database.DatabaseUtility;
import DIC.util.commons.GenerateID;
import java.sql.Connection;
import java.io.*;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by Nishtha Garg on 10/20/15.
 */
public class mappingtables {
    static int id=100;

    public static void Insertfunction(int id2, Connection connection, String line3) throws SQLException {
        try {
            //Connection connection = DatabaseUtility.getConnection("ora.csc.ncsu.edu", "Oracle", "1521", "orcl", "ngarg", "200104701");
            int count=0;
            int i=0;
            Statement st = connection.createStatement();
            File fin3 = new File("/home/nishtha/Downloads/dicdata/"+line3+".txt");
            FileInputStream fis3 = new FileInputStream(fin3);
            BufferedReader br3 = new BufferedReader(new InputStreamReader(fis3));
            String linecount = null;
            while ((linecount = br3.readLine()) != null) {
                count++;
            }
            br3.close();
            File fin4 = new File("/home/nishtha/Downloads/dicdata/"+line3+".txt");
            FileInputStream fis4 = new FileInputStream(fin4);
            BufferedReader br4 = new BufferedReader(new InputStreamReader(fis4));
            ArrayList<String> ids = GenerateID.generateMultipleIds(count);
            String line4 = null;
            /*String sql = "Insert all\n";
            //int count = 0;
            //sample comment


            while ((line4 = br4.readLine()) != null) {
                sql = sql + "into Dic_Link("+"Dic_Link_ID,"+"Dic_Link_Value,"+"Dic_Link_linkID)"+"values('" + ids.get(i) + "','" + line4 + "','" + id2 + "')\n";
                i++;
            }
            sql = sql + "select * from dual";*/
            String sql = "Insert into Dic_link (" +
                    " Dic_Link_ID," +
                    " Dic_Link_Value," +
                    " Dic_Link_LinkID)" +
                    " values";
//int count = 0;
//sample comment


            while ((line4 = br4.readLine()) != null) {
//sql = sql + "into Dic_Link("+"Dic_Link_ID,"+"Dic_Link_Value,"+"Dic_Link_linkID)"+"values('" + ids.get(i) + "','" + line4 + "','" + id2 + "')\n";
                sql = sql + "('" + ids.get(i) + "','" + line4 + "','" + id2 + "'),\n";

                i++;
            }
            sql = sql.substring(0, sql.length() - 2);
            System.out.println(sql);
            st.executeUpdate(sql);

            br3.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void Insertfunction2(String line3, Connection connection) throws SQLException {

        try {
            //Connection connection = DatabaseUtility.getConnection("ora.csc.ncsu.edu", "Oracle", "1521", "orcl", "ngarg", "200104701");
            Statement st = connection.createStatement();
            String sql = "Insert into Dic_Regex values('" + GenerateID.generateID() + "','" + line3 + "','" + "" + "','" + id +"',0)";
            System.out.println(sql);
            st.executeUpdate(sql);
            Insertfunction(id, connection,line3);
            id++;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void Insertfunction1(String line1, String line2, Connection connection) throws SQLException {
        try {

            Statement st = connection.createStatement();
            String sql = "Insert into Dic_Regex(DIC_REGEX_ID, " +
                    "                Dic_Regex_Type, " +
                    "                Dic_Regex_RegularExp," +
                    "                Dic_Regex_linkID, \n" +
                    "                Dic_Regex_Order) values(" +
                    "               '" + GenerateID.generateID() + "'," +
                    "               '" + line1 + "'," +
                    "               '" + line2 + "'," +
                    "               '',"+
                    "                  0)";
            System.out.println(sql);
            st.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void createTableData2(Connection connection) {

        try {

            File fin2 = new File("/home/nishtha/Downloads/dicdata/regexdata2.txt");
            FileInputStream fis2 = new FileInputStream(fin2);
            BufferedReader br1 = new BufferedReader(new InputStreamReader(fis2));
            String line3 = null;
            while ((line3 = br1.readLine()) != null) {
                Insertfunction2(line3, connection);
            }

            System.out.println("ok2");

            br1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createTableData1(Connection connection) {

        try {

            File fin = new File("/home/nishtha/Downloads/dicdata/regexdata.txt");
            FileInputStream fis = new FileInputStream(fin);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            String line1 = null;
            String line2 = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                if (count % 2 != 0)
                    line1 = line;
                if (count % 2 == 0)
                    line2 = line;
                if (count % 2 == 0)
                    Insertfunction1(line1, line2, connection);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws SQLException {
        try {
            Connection connection = DatabaseUtility.metadataConnection;
            String delete1= "Delete from Dic_Link";
            String delete2= "Delete from Dic_Regex";
            Statement st = connection.createStatement();
            st.execute(delete1);
            st.execute(delete2);
            createTableData1(connection);
            createTableData2(connection);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

