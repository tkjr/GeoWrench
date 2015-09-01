package geowrench.beans;

import com.mongodb.MongoException;
import geowrench.OperationStatus;
import geowrench.db.MongoDbClient;
import geowrench.logger.LoggerProvider;
import geowrench.tools.FacesMessageUtil;
import geowrench.tools.MD5Crypter;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author Tero Rantanen
 */
@ManagedBean(name = "adminBean")
@SessionScoped
public class AdminBean {
    
    @ManagedProperty(value = "#{localeBean}")
    private LocaleBean localeBean;
    
    private String newUsername;
    private String newPassword;
    private OperationStatus status = OperationStatus.UNKNOWN;
    
    Logger log = LoggerProvider.getInstance().getLogger();

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public LocaleBean getLocaleBean() {
        return localeBean;
    }

    public void setLocaleBean(LocaleBean localeBean) {
        this.localeBean = localeBean;
    }
    
    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }
    
    public void storeUserToDB() {
        try {
            System.out.println("storeUserToDB start...");
            String crypted = MD5Crypter.crypt(newPassword);
            
            MongoDbClient.getInstance().createUserCredentials(newUsername, crypted);
            setStatus(OperationStatus.SUCCESS);
            FacesMessageUtil.addFacesMessage(localeBean.getLocalizedStr("mongodb_user_create_success"), FacesMessage.SEVERITY_INFO);
        } catch (MongoException | IllegalArgumentException e) {
            e.printStackTrace();
            setStatus(OperationStatus.FAILED);
            FacesMessageUtil.addFacesMessage(localeBean.getLocalizedStr("mongodb_user_create_error") + ": " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    
    
    
}
