package geowrench.beans;

import com.mongodb.MongoException;
import geowrench.OperationStatus;
import geowrench.UserRole;
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
    private UserRole role;
    private UserRole[] roles = UserRole.values();
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserRole[] getRoles() {
        return roles;
    }

    
    
    
    
    
    public void storeUserToDB() {
        try {
            System.out.println("storeUserToDB start...");
            String crypted = MD5Crypter.crypt(newPassword);
            
            MongoDbClient.getInstance().createUserCredentials(newUsername, crypted, role);
            setStatus(OperationStatus.SUCCESS);
            FacesMessageUtil.addFacesMessage(localeBean.getLocalizedStr("mongodb_user_create_success"), FacesMessage.SEVERITY_INFO);
        } catch (MongoException | IllegalArgumentException e) {
            e.printStackTrace();
            setStatus(OperationStatus.FAILED);
            FacesMessageUtil.addFacesMessage(localeBean.getLocalizedStr("mongodb_user_create_error") + ": " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    
    
    
}
