package geowrench.logger;

import org.apache.log4j.Logger;
/**
 *
 * @author Tero Rantanen
 */
public class GWLogger {
    static Logger log = Logger.getLogger(GWLogger.class);
    
    public static Logger getLogger() {
        return log;
    }
}
