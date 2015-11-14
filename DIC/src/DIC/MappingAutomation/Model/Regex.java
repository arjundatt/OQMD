package DIC.MappingAutomation.Model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 11/10/2015
 * Time: 9:21 PM
 */
public class Regex {
    private String regexId;
    private String type;
    private String regex;
    private Integer linkId;
    private int order;
    private ArrayList<Link> values;

    public Regex(String regexId, String type, String regex, Integer linkId, int order) {
        this.regexId = regexId;
        this.type = type;
        this.regex = regex;
        this.linkId = linkId;
        this.order = order;
        if (linkId != null && values == null) {
            values = new ArrayList<Link>();
        }
    }

    public void addLink(Link link) {
        values.add(link);
    }

    public String getRegexId() {
        return regexId;
    }

    public String getType() {
        return type;
    }

    public String getRegex() {
        return regex;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public int getOrder() {
        return order;
    }

    public ArrayList<Link> getValues() {
        return values;
    }
}
