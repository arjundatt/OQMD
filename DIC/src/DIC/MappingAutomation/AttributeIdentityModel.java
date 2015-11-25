package DIC.MappingAutomation;

/**
 * Created by arjundatt.16 on 11/3/2015.
 */

// This is a model class used to add and hold different parameters to identify each column during mapping automation
// This represents each element of the ArrayList<AttributeIdentityModel> bucketClassifier;
public class AttributeIdentityModel {

    //Add more if need be
    public static final String TYPE = "DB_TYPE";  //rdbms, hbase, etc.
    public static final String TABLE_NAME = "tableName";
    public static final String COLUMN_NAME = "columnName";
    public static final String EFFICIENCY = "effciency";


    private String type;
    private String tableName;
    private String columnId;

    public void setType(String type) {
        this.type = type;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    private float efficiency;

    public AttributeIdentityModel(String type, String tableName, String columnId, float efficiency) {
        this.type = type;
        this.tableName = tableName;
        this.columnId = columnId;
        this.efficiency = efficiency;
    }

    public String getType() {
        return type;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnId() {
        return columnId;
    }



}
