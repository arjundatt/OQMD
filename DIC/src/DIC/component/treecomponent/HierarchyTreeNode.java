package DIC.component.treecomponent;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arjundatt.16 on 10/11/15.
 */
public class HierarchyTreeNode extends DefaultMutableTreeNode {
    private Map<String, String> attributes;

    public HierarchyTreeNode(Object nodeName) {
        super(nodeName);
        attributes = new HashMap<String, String>();
    }

    /**
     * @param attributes Attribute Map
     */
    public HierarchyTreeNode(Object nodeName, HashMap<String, String> attributes) {
        super(nodeName);
        if (attributes != null) {
            this.attributes = attributes;
        }
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * This method set a new attribute value for the Tree Node
     *
     * @param attributeName  Attribute Key
     * @param attributeValue Attribute Value
     */
    public void setAttribute(String attributeName, String attributeValue) {
        attributes.put(attributeName, attributeValue);
    }
}
