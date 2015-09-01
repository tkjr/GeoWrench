package geowrench.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Navigation bean
 *
 * @author Tero Rantanen
 *
 */
@ManagedBean(name = "navigationBean")
@SessionScoped
public class NavigationBean implements Serializable {

    private static final long serialVersionUID = 1520318172495977648L;

    /**
     * Redirect to login page.
     *
     * @return Login page name.
     */
    public String redirectToLogin() {
        return "/login.xhtml?faces-redirect=true";
    }

    /**
     * Go to login page.
     *
     * @return Login page name.
     */
    public String toLogin() {
        return "/login.xhtml";
    }

    /**
     * Redirect to info page.
     *
     * @return Info page name.
     */
    public String redirectToInfo() {
        return "/info.xhtml?faces-redirect=true";
    }

    /**
     * Go to info page.
     *
     * @return Info page name.
     */
    public String toInfo() {
        return "/info.xhtml";
    }

    /**
     * Redirect to welcome page.
     *
     * @return Welcome page name.
     */
    public String redirectToWelcome() {
        return "/member/welcome.xhtml?faces-redirect=true";
    }

    /**
     * Redirect to home page.
     *
     * @return Home page name.
     */
    public String redirectToHome() {
        return "/member/home.xhtml?faces-redirect=true";
    }

    /**
     * Go to welcome page.
     *
     * @return Welcome page name.
     */
    public String toWelcome() {
        return "/member/welcome.xhtml";
    }

}
