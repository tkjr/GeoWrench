package geowrench.tools;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author Tero Rantanen
 */
public class XmlToJson {
    
    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static String TEST_XML_STRING =
        "<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";
    
    public static String toJSON(String xml) {
        String retVal = "";
        try {            
            //JSONObject xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);                        
            JSONObject xmlJSONObj = XML.toJSONObject(xml);                        
            retVal = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        } catch (JSONException ex) {
            ex.printStackTrace();
            Logger.getLogger(XmlToJson.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }
}
