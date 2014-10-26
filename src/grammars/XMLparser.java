package grammars;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLparser {
    public XMLparser() {
        try {
            File XMLfile = new File("src/grammars/testGrammar01.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(XMLfile);
            doc.getDocumentElement().normalize();      
            NodeList root = doc.getChildNodes();
            Node shapeRuleNode = getNode("shape_rule", root);            
            String shapeRuleName = getNodeAttr("name", shapeRuleNode);
            
            System.out.println("shapeRuleName: " + shapeRuleName);
        }
        catch(ParserConfigurationException | SAXException | IOException pce) {}
    }
    
    public Node getNode(String tagName, NodeList nodes) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }
        return null;
    }
    
    protected String getNodeAttr(String attrName, Node node) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y < attrs.getLength(); y++ ) {
            Node attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();
            }
        }
        return "";
    }
}
