package DIC.MappingAutomation.Model;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 11/10/2015
 * Time: 9:21 PM
 */
public class Link {
    private String id;

    public String getValue() {
        return value;
    }

    private String value;
    private String linkId;

    public Link(String id, String value, String linkId) {
        this.id = id;
        this.value = value;
        this.linkId = linkId;
    }
}
