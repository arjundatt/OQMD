package DIC.MappingAutomation;

/**
 * Created by arjundatt.16 on 11/3/2015.
 */
public class RDBMSClassifier extends DomainClassifier {

    public void initClassification(){}


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
    @Override
    public void phaseI() {

        //after this call phase II of the super class
    }
}
