package geowrench.tools;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 *
 * @author Tero Rantanen
 */
public class FacesMessageUtil {
    public static void addFacesMessage(String message, Severity severity) {
        FacesMessage msg = new FacesMessage(message);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage("GeoWrench", msg);
    }
}
