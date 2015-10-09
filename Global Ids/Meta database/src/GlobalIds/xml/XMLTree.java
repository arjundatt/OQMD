package DIC.xml;

import DIC.component.treecomponent.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/9/13
 * Time: 2:20 PM
 *
 */
public class XMLTree {


    private Element root;
    Transformer t;
    Document document;

    public void writeXml(JTree jTree) throws ParserConfigurationException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.newDocument();
            t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            root = document.createElement("Connections");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        HierarchyTreeNode rootNode = (HierarchyTreeNode) jTree.getModel().getRoot();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//        document = builder.newDocument();
        addNode(root, rootNode);
        document.appendChild(root);
        writeXMLFile(document, "test");
    }

    protected Element createXMLNode(HierarchyTreeNode node) {
        Element element = null;
        if (node.getLevel() == 1) {
            element = document.createElement("Instance");
        } else if (node.getLevel() == 2) {
            element = document.createElement("Schema");
        } else if (node.getLevel() == 3) {
            element = document.createElement("Table");
        } else if (node.getLevel() == 4) {
            element = document.createElement("Columns");
        }
        addAllAttributes(element, node);
        return element;
    }

    protected void addNode(Element parentNode, HierarchyTreeNode treeNode) {
        Enumeration children = treeNode.children();
        while (children.hasMoreElements()) {
            HierarchyTreeNode node = (HierarchyTreeNode) children.nextElement();
            Element element = createXMLNode(node);
            parentNode.appendChild(element);
            addNode(element, node);
        }
    }

    public Element getRoot() {
        return root;
    }

    private void addAttribute(Element element, String name, String value) {
        element.setAttribute(name, value);
    }

    private void addAllAttributes(Element element, HierarchyTreeNode node) {
        HashMap attributes = (HashMap) node.getAttributes();
        Set set = attributes.entrySet();
        for (Object aSet : set) {
            Map.Entry me = (Map.Entry) aSet;
            addAttribute(element, me.getKey().toString(), me.getValue().toString());
        }
    }

    public HierarchyTreeNode xmlToTree() {
        HierarchyTreeNode root;
        //Creating an empty XML Document
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        File xmlFile = new File("D:/test.xml");
        Element xmlRoot = null;

        try {
            if (xmlFile.isFile()) {
                document = docBuilder.parse("D:/test.xml");
            } else {
                document = docBuilder.newDocument();
            }
            xmlRoot = document.getDocumentElement();

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (xmlRoot != null)
                root = createTreeNode(xmlRoot);
            else
                root = new HierarchyTreeNode("root");
        }

        return root;
    }

    /**
     * Recursively calls itself to generate a JTree from a Node
     *
     * @param root Node
     * @return HierarchyTreeNode
     */
    private HierarchyTreeNode createTreeNode(Node root) {
        HierarchyTreeNode currentNode = null;
        if (root.getNodeName().equals("Connections")) {
            currentNode = new HierarchyTreeNode("root");
        } else if (root.getNodeType() == Node.ELEMENT_NODE) {
            currentNode = new HierarchyTreeNode(root.getAttributes().getNamedItem("name").getNodeValue());
        }
        if (root.hasAttributes()) {
            NamedNodeMap nodeMap = root.getAttributes();
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node node = nodeMap.item(i);
                currentNode.setAttribute(node.getNodeName(), node.getNodeValue());
            }
        }
        if (root.hasChildNodes()) {
            NodeList childNodes = root.getChildNodes();
            if (childNodes != null) {
                for (int k = 0; k < childNodes.getLength(); k++) {
                    Node node = childNodes.item(k);
                    if (node != null && node.getNodeType() == Node.ELEMENT_NODE)
                        currentNode.add(createTreeNode(node));
                }
            }
        }
        return currentNode;
    }

    public void writeXMLFile(Document xml, String name) {

        //set up a transformer
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        try {
            trans = transfac.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
        }
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(xml);
        try {
            trans.transform(source, result);
        } catch (TransformerException e1) {
            e1.printStackTrace();
        }
        String xmlString = sw.toString();

        try {
            File fsch = new File("D:/" + name + ".xml");
            FileWriter fstream = new FileWriter(fsch);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write(xmlString);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


