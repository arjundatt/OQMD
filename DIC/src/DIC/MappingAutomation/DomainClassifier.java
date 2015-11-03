package DIC.MappingAutomation;

import java.util.ArrayList;

/**
 * Created by arjundatt.16 on 11/3/2015.
 */
abstract public class DomainClassifier {

    //use only this for each mapping
    protected static ArrayList<AttributeIdentityModel> bucketClassifier;


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
    private void phaseII(){
        /* 1. Iterate through each bucket
         * 2. pick the 2 highest priority(efficiency) values, belonging to different db_types, from the same bucket
         *    ****(need to think about efficiency values(of the same db_type) which are the same)
         * 3. Map the 2 values(add them to HashMap or some other structure) and remove them from the bucket.
         * 4. Remove the mapped values from the bucket.
         * 5. repeat the process for all the values in the same bucket.
         * 6. add unclassified columns to LIST_UNCLASSIFIED<AttributeIdentityModel>
        * */
    }

    //executed after phase II
    //mapping is done here -> this is perhaps the final result
    private void phaseIII(){
        /* Need to compare the matched columns once again to find similar patterns. Not sure if this is necessary.
        * */
    }
}
