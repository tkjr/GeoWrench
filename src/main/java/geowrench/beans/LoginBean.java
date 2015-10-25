package geowrench.beans;

import geowrench.OperationStatus;
import geowrench.UserRole;
import geowrench.db.MongoDbClient;
import geowrench.logger.LoggerProvider;
import geowrench.tools.FacesMessageUtil;
import geowrench.tools.MD5Crypter;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Login bean
 *
 * @author Tero Rantanen
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 7765876811740798583L;
    private String username;
    private String password;
    private String newPassword;
    private boolean loggedIn;
    private OperationStatus status = OperationStatus.UNKNOWN;

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;
    
    @ManagedProperty(value = "#{localeBean}")
    private LocaleBean localeBean;
    
    @ManagedProperty(value = "#{adminBean}")
    private AdminBean adminBean;
    
    
    Logger log = LoggerProvider.getInstance().getLogger();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }
    
    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public LocaleBean getLocaleBean() {
        return localeBean;
    }

    public void setLocaleBean(LocaleBean localeBean) {
        this.localeBean = localeBean;
    }

    public AdminBean getAdminBean() {
        return adminBean;
    }

    public void setAdminBean(AdminBean adminBean) {
        this.adminBean = adminBean;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    /**
     * Login
     *
     * @return
     */
    public String doLogin() {        
        log.debug("Logging in as user '" + username + "'");
        
        if (loggedIn) {
            doLogout();
        }
        
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            setStatus(OperationStatus.FAILED);
            FacesMessageUtil.addFacesMessage(localeBean.getLocalizedStr("login_error"), FacesMessage.SEVERITY_ERROR);
            return navigationBean.toLogin();
        }

        
        
        String pwdFromDB = MongoDbClient.getInstance().queryUserCredentials(username);
        if(StringUtils.isNotEmpty(pwdFromDB)) {
            String cryptedPwd = MD5Crypter.crypt(password);
            if (cryptedPwd.equals(pwdFromDB)) {
                loggedIn = true;
                queryUserRole();
                return navigationBean.redirectToHome();
            }
        }
        
        
        setStatus(OperationStatus.FAILED);
        // Set login ERROR
        FacesMessageUtil.addFacesMessage(localeBean.getLocalizedStr("login_error"), FacesMessage.SEVERITY_ERROR);

        // To to login page
        return navigationBean.toLogin();
    }

    /**
     * Logout
     *
     * @return
     */
    public String doLogout() {
        localeBean.setDefaultLocale();
        // Set the paremeter indicating that user is logged in to false
        loggedIn = false;
        adminBean.setRole(null);

        setStatus(OperationStatus.SUCCESS);
        // Set logout message
        FacesMessageUtil.addFacesMessage(localeBean.getLocalizedStr("logout_success"), FacesMessage.SEVERITY_INFO);

        log.debug("User '" + username + "' logged out.");
        
        return navigationBean.redirectToLogin();
    }
    
    public void changePassword() {
        System.out.println("LoginBean changePassword()...");
        String cryptedOld = MD5Crypter.crypt(password);
        String pwdFromDB = MongoDbClient.getInstance().queryUserCredentials(username);
        if(StringUtils.isNotEmpty(pwdFromDB) && (pwdFromDB.equals(cryptedOld))) {
            String cryptedNew = MD5Crypter.crypt(newPassword);
            MongoDbClient.getInstance().updateUserCredentials(username, cryptedNew);
            password = newPassword;
        }
    }
    
    private void queryUserRole() {
        UserRole roleFromDB = MongoDbClient.getInstance().queryUserRole(username);
        adminBean.setRole(roleFromDB);
    }

}
