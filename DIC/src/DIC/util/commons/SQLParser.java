package DIC.util.commons;

import DIC.util.database.DatabaseUtility;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ESqlStatementType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TResultColumnList;
import gudusoft.gsqlparser.nodes.TTableList;
import gudusoft.gsqlparser.nodes.TWhereClause;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 10/27/2015
 * Time: 8:43 PM
 */
public class SQLParser {
    String sql;
    String schema;
    ArrayList<String> columns;
    ArrayList<String> columnIDs;
    ArrayList<String> tables;
    String where;

    public SQLParser(String sql) {
        this.sql = sql;
        columns = new ArrayList<String>();
        tables = new ArrayList<String>();
        columnIDs = new ArrayList<String>();
        schema = "";
        where = "";
    }

    public int parse() {
        //currently works only for oracle
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
        sqlparser.sqltext = sql;
        int ret = sqlparser.parse();
        if (ret == 0) {
            TCustomSqlStatement statement = sqlparser.sqlstatements.get(0);
            //if the query is a select query
            if (statement.sqlstatementtype == ESqlStatementType.sstselect) {
                TSelectSqlStatement sst = (TSelectSqlStatement) statement;
                TResultColumnList columnList = sst.getResultColumnList();
                for (int i = 0; i < columnList.size(); i++)
                    columns.add(columnList.getResultColumn(i).toString());
                TTableList tablesList = sst.tables;
                for (int i = 0; i < tablesList.size(); i++)
                    tables.add(tablesList.getTable(i).toString());
                TWhereClause whereClause = sst.getWhereClause();
                if (whereClause != null)
                    where = whereClause.toString();
                //assuming that all the tables are from the same schema
                String firstTable = tables.get(0);
                if (firstTable.contains(".")) {
                    schema = firstTable.substring(0, firstTable.indexOf("."));
                }
                String sql = "SELECT dic_column_id \n" +
                        "FROM   dic_column \n" +
                        "       JOIN dic_table \n" +
                        "         ON dic_column_table_id = dic_table_id \n" +
                        "       JOIN dic_schema \n" +
                        "         ON dic_table_schema_id = dic_schema_id " +
                        "WHERE (";
                for (String column : columns) {
                    sql += "DIC_COLUMN_NAME = '" + column.toUpperCase() + "' or ";
                }
                sql = sql.substring(0, sql.length() - 3) + ") and (";
                for (String table : tables) {
                    sql += "DIC_TABLE_NAME = '" + table.substring(table.indexOf(".") + 1, table.length()) + "' or ";
                }
                sql = sql.substring(0, sql.length() - 3) + ") and DIC_SCHEMA_NAME = '" + schema + "'";
                System.out.println(sql);
                try {
                    Vector<Vector<String>> vector = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(sql);
                    vector.remove(0); //removing column name
                    for (Vector<String> row : vector) {
                        columnIDs.add(row.get(0));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public ArrayList<String> getColumnIDs() {
        return columnIDs;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public ArrayList<String> getTables() {
        return tables;
    }

    public String getSchema() {
        return schema;
    }

    public static void main(String[] args) {
        SQLParser sqlParser = new SQLParser("SELECT dic_instance_id, \n" +
                "       dic_instance_databasetype, \n" +
                "       dic_instance_instancename, \n" +
                "       dic_instance_connectionname, \n" +
                "       dic_instance_password, \n" +
                "       dic_instance_portnumber, \n" +
                "       dic_instance_systemname, \n" +
                "       dic_instance_username, \n" +
                "       dic_schema_id, \n" +
                "       dic_schema_name, \n" +
                "       dic_schema_hbasetable, \n" +
                "       dic_schema_instance_id, \n" +
                "       dic_table_id, \n" +
                "       dic_table_columncount, \n" +
                "       dic_table_iscolumnfamily, \n" +
                "       dic_table_columndiscovered, \n" +
                "       dic_table_metadatadiscovered, \n" +
                "       dic_table_name, \n" +
                "       dic_table_record, \n" +
                "       dic_table_timestamp, \n" +
                "       dic_table_schema_id, \n" +
                "       dic_column_id, \n" +
                "       dic_column_length, \n" +
                "       dic_column_name, \n" +
                "       dic_column_type, \n" +
                "       dic_column_table_id \n" +
                "FROM   NGARG.dic_instance \n" +
                "       JOIN NGARG.dic_schema \n" +
                "         ON dic_schema_instance_id = dic_instance_id \n" +
                "       JOIN NGARG.dic_table \n" +
                "         ON dic_schema_id = dic_table_schema_id \n" +
                "       JOIN NGARG.dic_column \n" +
                "         ON dic_column_table_id = dic_table_id " +
                "WHERE  dic_column_table_id = '2001092'");
        sqlParser.parse();
        System.out.println("Columns are ");
        for (String column : sqlParser.getColumns()) {
            System.out.print(column + ", ");
        }
        System.out.println("\nTables are ");
        for (String table : sqlParser.getTables()) {
            System.out.print(table + ", ");
        }
        System.out.println("\nschema = " + sqlParser.getSchema());
        System.out.println("where clause = " + sqlParser.where);

        System.out.println("Columns Ids are ");
        for (String columnId : sqlParser.getColumnIDs()) {
            System.out.print(columnId + ", ");
        }
    }
}
