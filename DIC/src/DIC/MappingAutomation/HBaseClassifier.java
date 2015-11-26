package DIC.MappingAutomation;

import java.util.Vector;

/**
 * Created by arjundatt.16 on 11/25/2015.
 */
public class HBaseClassifier extends DomainClassifier{
    String tableId;
    Vector<Vector<String>> data;
    public static final String IDENTITY = "HBase";

    @Override
    public void initClassification(String tableID) {
        this.tableId = tableID;
        getDomains(IDENTITY);
    }

    @Override
    public void phaseI() {


    }
}
