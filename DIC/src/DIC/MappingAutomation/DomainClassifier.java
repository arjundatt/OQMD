package DIC.MappingAutomation;

import DIC.MappingAutomation.Model.Link;
import DIC.MappingAutomation.Model.Regex;
import DIC.util.database.DatabaseUtility;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * Created by arjundatt.16 on 11/3/2015.
 */
abstract public class DomainClassifier {
    protected Map<String, Regex> regexMap;
    protected Map<String, ArrayList<String>> columnMap;     //columnId->column Data
    //use only this for each mapping
    protected static ArrayList<AttributeIdentityModel> bucketClassifier;

    protected void getDomains() {
        String regexSQL = "SELECT dic_regex_id, \n" +
                "       dic_regex_type, \n" +
                "       dic_regex_regularexp, \n" +
                "       dic_regex_linkid, \n" +
                "       dic_regex_order, \n" +
                "       dic_link_id, \n" +
                "       dic_link_value, \n" +
                "       dic_link_linkid \n" +
                "FROM   ngarg.dic_regex \n" +
                "       FULL JOIN ngarg.dic_link \n" +
                "              ON dic_link_linkid = dic_regex_linkid \n" +
                "ORDER  BY dic_regex_order ";
        try {
            Vector<Vector<String>> domainDetails = (Vector<Vector<String>>) DatabaseUtility.executeQueryOnMetaDatabase(regexSQL);
            domainDetails.remove(0);
            regexMap = new HashMap<String, Regex>();
            for (Vector<String> domainDetail : domainDetails) {
                String regexId = domainDetail.get(0);
                Regex regex = regexMap.get(regexId);
                if (regex == null) {
                    //create new regex
                    regex = new Regex(regexId, domainDetail.get(1), domainDetail.get(2), domainDetail.get(3) == null ? null : Integer.parseInt(domainDetail.get(3)), Integer.parseInt(String.valueOf(domainDetail.get(4))));
                    regexMap.put(regexId, regex);
                }
                if (regex.getLinkId() != null) {
                    Link link = new Link(domainDetail.get(5), domainDetail.get(6), domainDetail.get(7));
                    regex.addLink(link);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Map<String, ArrayList<String>> generateColumnMap(Vector<Vector<String>> data, Vector<Vector<String>> columns) {
        data.remove(0);
        HashMap<String, ArrayList<String>> columnMap = new HashMap<String, ArrayList<String>>();
        for (int i = 0; i < data.get(0).size(); i++) {
            String columnId = columns.get(i).get(1);
            for (Vector<String> row : data) {
                if (columnMap.get(columnId) == null) {
                    ArrayList<String> col = new ArrayList<String>();
                    col.add(row.get(i));
                    columnMap.put(columnId, col);
                } else {
                    ArrayList<String> col = columnMap.get(columnId);
                    col.add(row.get(i));
                }
            }

        }
        return columnMap;
    }

    //Classify each attribute into n (priority)buckets(classifications like name, id, phone number, country, etc.)
    /* 0. Create n empty buckets: each corresponds to each classification
         * 1. Retrieve all column names in RDBMS
         * 2. Iterate through each column
         * 3. efficiency = 0
         * 4. Iterate through first 10 entries per column -> match regex/database from regex table for one entry.
         * 5. If efficiency<50%(random value -- need to test) -> match regex/database from regex table for next entry.
         * 6. If efficiency>90%(random value -- need to test) -> put this column in ith bucket with
         *    **** CL(confidence level) = efficiency and <AttributeIdentityModel value> and move to next column
         * 7. If efficiency>50%(random value -- need to test) -> pickup next 10 entries and repeat the process.
         * 8. If after 200(random value -- need to test) cells in a column -> 50% < efficiency < 100% then put in
         *    **** the ith bucket with CL(confidence level) = efficiency and <AttributeIdentityModel value> but
         *    **** check for other classifications.
         * 9. Insert unclassified columns into LIST_UNCLASSIFIED<AttributeIdentityModel>
         */
    //Implement this method for each database type
    abstract public void phaseI();

    //executed after phase I
    //mapping is done here -> but this is NOT the final result
    private void phaseII() {
        /* 1. Iterate through each bucket
         * 2. pick the 2 highest priority(efficiency) values, belonging to different db_types, from the same bucket
         *    ****(need to think about efficiency values(of the same db_type) which are the same)
         * 3. Map the 2 values(add them to HashMap or some other structure) and remove them from the bucket.
         * 4. Remove the mapped values from the bucket.
         * 5. repeat the process for all the values in the same bucket.
         * 6. add unclassified columns to LIST_UNCLASSIFIED<AttributeIdentityModel>
        * */
        for (Map.Entry<String, ArrayList<String>> idToValuesEntry : columnMap.entrySet()) {
            String columnId = idToValuesEntry.getKey();
            ArrayList<String> values = idToValuesEntry.getValue();
            //match the values to the regex pattern of regexMap
            for (Map.Entry<String, Regex> stringRegexEntry : regexMap.entrySet()) {
                String regexId = stringRegexEntry.getKey();
                Regex regex = stringRegexEntry.getValue();
                if (regex.getRegex() == null) {
                    //check the values of the links
                    ArrayList<Link> links = regex.getValues();
                    // iterate through the values to check if it matches with the links
                    for (String value : values) {
                        //write code for percentage matching blah blah
                    }
                }
            }
        }
    }

    //executed after phase II
    //mapping is done here -> this is perhaps the final result
    private void phaseIII() {
        /* Need to compare the matched columns once again to find similar patterns. Not sure if this is necessary.
        * */
    }
}
