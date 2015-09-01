package geowrench.logger;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;

/**
 *
 * @author Tero Rantanen
 */
@ManagedBean
@ApplicationScoped
public class LoggerProvider {

    private static final Logger log = Logger.getLogger(GWLogger.class);
    private static LoggerProvider instance;
    
    // Private constructor, jotta luokasta ei voi luoda omaa instanssia
    // ilman että kutsuu getInstance() -metodia.
    private LoggerProvider(){}
    
    public static synchronized LoggerProvider getInstance() {
        if (instance == null) {
            instance = new LoggerProvider();
        }
        return instance;
    }
    
    public synchronized Logger getLogger() {
        return log;
    }
}
