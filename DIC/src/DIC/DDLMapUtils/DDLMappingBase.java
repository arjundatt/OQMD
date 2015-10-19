package DIC.DDLMapUtils;

/**
 * Created by arjundatt.16 on 10/14/2015.
 */

/* All the classes corresponding to every database(RDBMS, HBase, etc.)
 * which receive queries from the Common Query Engine and send them to
 * the respective databases, must implement this interface.
 * For now, only adding the basic methods. Add more if need be.
 */

public interface DDLMappingBase {

    boolean insert(String mQuery);
    String query(String mQuery);
    int count(String mQuery);

}
