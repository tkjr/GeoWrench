package geowrench.tools;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Tero Rantanen
 */
public class XPathHelper {
    
    private String xml;

    public XPathHelper(String xml) {
        this.xml = xml;
    }
    
    
    
    public static String parseTime(String xml) throws Exception {
        String retValue;

        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

//        String expression = "/gpx/metadata/time";
//        String time = xpath.evaluate(expression, document);
//        System.out.println("time: " + time);

        XPathExpression expr = xpath.compile("//gpx/metadata/time/text()");
        Object result = expr.evaluate(document, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }
        
        Object result2 = expr.evaluate(document, XPathConstants.NODE);
        Node node = (Node) result2;
        System.out.println(node.getParentNode().getNodeName() + ", " + node.getNodeName() + ", " + node.getNodeValue());
        
        retValue = node.getNodeValue();

        return retValue;
    }
    
    
}
