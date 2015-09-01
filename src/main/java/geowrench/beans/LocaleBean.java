package geowrench.beans;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Tero Rantanen
 */
@ManagedBean(name = "localeBean")
@SessionScoped
public class LocaleBean implements Serializable {
   private static final long serialVersionUID = 1L;
   private static final String BUNDLE_BASE_NAME = "geowrench.messages";
   private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
   private final Locale defaultLocale = new Locale("en", "EN");
   
   public Locale getLocale() {
       return locale;
   }
   
   public String getLanguage() {
       return locale.getLanguage();
   }
   
   public void setDefaultLocale() {
       locale = defaultLocale;
       FacesContext.getCurrentInstance().getViewRoot().setLocale(defaultLocale);
   }
   
   public void changeLocale(String language, String country) {
       locale = new Locale(language, country);
       FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
   }
   
   public String getLocalizedStr(String key) {
       ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, getLocale());
       return bundle.getString(key);
   }
   
   
//   private String locale;
//   private static final Map<String,Object> locales;
//   
//   static{
//      locales = new LinkedHashMap<>();
//      locales.put("English", new Locale("en", "EN"));
//      locales.put("Finnish", new Locale("fi", "FI"));
//   }
//
//   public Map<String, Object> getLocales() {
//      return locales;
//   }
//
//   public String getLocale() {
//      return locale;
//   }
//
//   public void setLocale(String locale) {
//      this.locale = locale;
//   }
//
//   //value change event listener
//   public void localeChanged(ValueChangeEvent e){
//      String newLocaleValue = e.getNewValue().toString();
//      locales.entrySet().stream().filter((entry) -> (entry.getValue().toString().equals(newLocaleValue))).forEach((entry) -> {
//          FacesContext.getCurrentInstance()
//                  .getViewRoot().setLocale((Locale)entry.getValue());
//       });
//   }
   
   
    
    
}
