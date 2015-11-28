package DIC.MappingAutomation;

import DIC.MappingAutomation.Model.Link;
import DIC.MappingAutomation.Model.Regex;
import DIC.util.database.DatabaseUtility;

import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by arjundatt.16 on 11/3/2015.
 */
abstract public class DomainClassifier {
    protected static Map<String, Regex> regexMap;                   //regexId->regex object
    //protected Map<String, ArrayList<String>> columnMap;     //columnId->column Data
    protected static HashSet<String> allColumnIds = new HashSet<String>();
    //use only bucketClassifier for each mapping
    //regexID -> (DBType -> priority queue of classified attributes)
    protected static LinkedHashMap<String,HashMap<String,PriorityQueue<AttributeIdentityModel>>> bucketClassifier;

    protected void getDomains(String sourceIdentity) {
        if(regexMap != null){
            initBucket(regexMap, sourceIdentity);
            return;
        }
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
            regexMap = new LinkedHashMap<String, Regex>();
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

            initBucket(regexMap, sourceIdentity);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class AttributesComparator implements Comparator<AttributeIdentityModel>{

        @Override
        public int compare(AttributeIdentityModel o1, AttributeIdentityModel o2) {
            if(o1.getEfficiency() < o2.getEfficiency()){
                return -1;
            }
            if(o1.getEfficiency() > o2.getEfficiency()){
                return 1;
            }
            return 0;
        }
    }

    private void initBucket(Map<String, Regex> mRegexMap, String sourceIdentity){
        if(bucketClassifier == null) {
            bucketClassifier = new LinkedHashMap<String, HashMap<String, PriorityQueue<AttributeIdentityModel>>>();
        }
        int i=0;
        for(Map.Entry<String,Regex> idToValuesEntry : mRegexMap.entrySet()){
            String columnId = idToValuesEntry.getKey();
            Comparator<AttributeIdentityModel> comparator = new AttributesComparator();
            HashMap<String, PriorityQueue<AttributeIdentityModel>> map;
            if(bucketClassifier.get(columnId)==null) {
                map = new HashMap<String, PriorityQueue<AttributeIdentityModel>>();
            }
            else{
                map = bucketClassifier.get(columnId);
            }
            PriorityQueue<AttributeIdentityModel> queue = new PriorityQueue<AttributeIdentityModel>(11,comparator);
            map.put(sourceIdentity,queue);
            bucketClassifier.put(columnId,map);
        }
    }

    protected HashMap<String, ArrayList<String>> generateColumnMap(Vector<Vector<String>> data, Vector<Vector<String>> columns) {
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
    //Implement this method for each database type
    abstract public void initClassification(String tableID);

    //Implement this method for each database type
    abstract public void phaseI();

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
    protected void phaseII(HashMap<String, ArrayList<String>> columnMap, String sourceIdentity,String tableId){
        for (Map.Entry<String, ArrayList<String>> idToValuesEntry : columnMap.entrySet()) {
            String columnId = idToValuesEntry.getKey();
            ArrayList<String> values = idToValuesEntry.getValue();
            //match the values to the regex pattern of regexMap
            for (Map.Entry<String, Regex> stringRegexEntry : regexMap.entrySet()) {
                AttributeIdentityModel attributeInstance = new AttributeIdentityModel(sourceIdentity,tableId,columnId,0.0f);
                String regexId = stringRegexEntry.getKey();
                Regex regex = stringRegexEntry.getValue();
                attributeInstance.setType(regex.getType());
                float efficiency=0.0f;
                if (regex.getRegex() == null) {
                    //check the values of the links
                    ArrayList<Link> links = regex.getValues();
                    // iterate through the values to check if it matches with the links
                    efficiency = sampleMatch(attributeInstance, values, regex.getValuesString(links));
                }
                else{
                    efficiency = sampleMatch(attributeInstance, values, regex);
                }
                HashMap<String,PriorityQueue<AttributeIdentityModel>> map= bucketClassifier.get(regexId);
                PriorityQueue<AttributeIdentityModel> q = map.get(sourceIdentity);
                q.add(attributeInstance);
                bucketClassifier.put(regexId,map);
            }
        }
        testCode(sourceIdentity);
        //phaseIII();
    }

    //todo:test code, remove it later
    private void testCode(String sourceIdentity){
        Iterator<Map.Entry<String,HashMap<String,PriorityQueue<AttributeIdentityModel>>>> itr= bucketClassifier.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry<String,HashMap<String,PriorityQueue<AttributeIdentityModel>>> idToValuesEntry = itr.next();
            String id = idToValuesEntry.getKey();
            Iterator<Map.Entry<String,PriorityQueue<AttributeIdentityModel>>> subItr = (idToValuesEntry.getValue()).entrySet().iterator();
            while(subItr.hasNext()){
                Map.Entry<String,PriorityQueue<AttributeIdentityModel>> child = subItr.next();
                System.out.print("DB: " + child.getKey());
                PriorityQueue<AttributeIdentityModel> p = child.getValue();
                Iterator pItr = p.iterator();
                while (pItr.hasNext()){
                    AttributeIdentityModel o = (AttributeIdentityModel) pItr.next();
                    System.out.println(" column id: " + o.getColumnId() + " type: " + o.getType() + " efficiency: " + o.getEfficiency());
                }
            }
        }
    }

    private float sampleMatch(AttributeIdentityModel attributeInstance, ArrayList<String> population, ArrayList<String> links){
        float sampleEfficiency = 0.0f;
        int matchCount =0;
        int i;
        for(i=0;(i<1500 && i<population.size());i++){
            if(links.contains(population.get(i))) {
                    matchCount++;
            }
        }
        sampleEfficiency = i>1 ? (float)matchCount/i: 0.0f;
        attributeInstance.setEfficiency(sampleEfficiency);

        return attributeInstance.getEfficiency();
    }

    private float sampleMatch(AttributeIdentityModel attributeInstance, ArrayList<String> population, Regex regex){
        float sampleEfficiency = 0.0f;
        int matchCount =0;
        int i;
        for(i=0;(i<500 && i<population.size());i++){
            if(population.get(i).matches(regex.getRegex())){
                    matchCount++;
            }
        }

        sampleEfficiency = i>1 ? (float)matchCount/i: 0.0f;
        attributeInstance.setEfficiency(sampleEfficiency);
        return attributeInstance.getEfficiency();
    }

    //executed after phase II
    //mapping is done here -> but this is NOT the final result
    protected void phaseIII() {

        /* 1. Iterate through each bucket
         * 2. pick the 2 highest priority(efficiency) values, belonging to different db_types, from the same bucket
         *    ****(need to think about efficiency values(of the same db_type) which are the same)
         * 3. Map the 2 values(add them to HashMap or some other structure) and remove them from the bucket.
         * 4. Remove the mapped values from the bucket.
         * 5. repeat the process for all the values in the same bucket.
         * 6. add unclassified columns to LIST_UNCLASSIFIED<AttributeIdentityModel>
        * */

        //regexID -> ArrayList(attributes in this bucket(domain))
        //The object AttributeIdentityModel <obj> contains the info about its ID and the dbType it belongs to
        //use obj.getType() and obj.getColumnId to retrieve this info
        LinkedHashMap<String,ArrayList<AttributeIdentityModel>> mMappings = new LinkedHashMap<String, ArrayList<AttributeIdentityModel>>();

        Iterator<Map.Entry<String,HashMap<String,PriorityQueue<AttributeIdentityModel>>>> bucketIterator = (bucketClassifier.entrySet()).iterator();
        int domainCount =0;
        while(bucketIterator.hasNext()){
            Map.Entry<String,HashMap<String,PriorityQueue<AttributeIdentityModel>>> child = bucketIterator.next();
            String regexID = child.getKey();
            domainCount++;
            Iterator<Map.Entry<String,PriorityQueue<AttributeIdentityModel>>> subIterator = child.getValue().entrySet().iterator();
            while(subIterator.hasNext()){
                Map.Entry<String,PriorityQueue<AttributeIdentityModel>> buckets = subIterator.next();
                String dbIdentity = buckets.getKey();
                PriorityQueue<AttributeIdentityModel> classificationQueue = buckets.getValue();
                int size = classificationQueue.size();
                while (size>0){
                    AttributeIdentityModel attributeInstance = classificationQueue.poll();
                    size--;
                    if(attributeInstance!=null) {
                        if (attributeInstance.getEfficiency() < 0.5f) {
                            continue;
                        }
                        ArrayList<AttributeIdentityModel> attributeBag;
                        if(mMappings.get(regexID)==null){
                            attributeBag = new ArrayList<AttributeIdentityModel>();
                            mMappings.put(regexID,attributeBag);
                        }
                        else{
                            attributeBag = mMappings.get(regexID);
                        }
                        attributeBag.add(attributeInstance);
                        //todo: test print, remove
                        System.out.println("Domain:" + domainCount + " db type: " + dbIdentity + "|Attribute: " + attributeInstance.getColumnId());
                    }
                }

            }
        }
        phaseIV(mMappings);
    }

    //executed after phase III
    //mapping is done here -> this is perhaps the final result
    private void phaseIV(LinkedHashMap<String,ArrayList<AttributeIdentityModel>> mMappings) {
        /* Need to compare the matched columns once again to find similar patterns. Not sure if this is necessary.
        * */
        insertIntoMetaDb(mMappings);

    }

    private void insertIntoMetaDb(LinkedHashMap<String,ArrayList<AttributeIdentityModel>> mMappings){
        String mappingSql = "UPDATE NGARG.DIC_COLUMN\n"+
                " SET DIC_COLUMN_REGEXID = CASE DIC_COLUMN_ID\n";
        Iterator<Map.Entry<String,ArrayList<AttributeIdentityModel>>> iterator = mMappings.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,ArrayList<AttributeIdentityModel>> child = iterator.next();
            String regexId = child.getKey();
            ArrayList<AttributeIdentityModel> attributeBag = child.getValue();
            for(AttributeIdentityModel attr : attributeBag){
                String cID = attr.getColumnId();
                if(allColumnIds.contains(cID)){
                    mappingSql += "WHEN "+cID+" THEN "+regexId+"\n";
                    allColumnIds.remove(cID);
                }
            }
        }
        mappingSql += "END";

        //todo: execute mappingSql to update the DIC_COLUMN table
        System.out.print(mappingSql);
    }
}
