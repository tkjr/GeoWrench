package geowrench.beans;

import geowrench.UploadStatus;
import geowrench.db.MongoDbClient;
import geowrench.logger.LoggerProvider;
import geowrench.tools.GWUtilities;
import geowrench.tools.XPathHelper;
import geowrench.tools.XmlToJson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

/**
 *
 * @author rantbter
 */
@ManagedBean(name = "uploadBean")
@SessionScoped
public class UploadBean {
    
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    
    @ManagedProperty(value = "#{localeBean}")
    private LocaleBean localeBean;
    
    Logger log = LoggerProvider.getInstance().getLogger();
    
    private Part file;
    private String activeGPX;
    private String selectedFilename;
    private String time;
    private UploadStatus status = UploadStatus.UNKNOWN;
    
    private List<String> testiLista = new ArrayList<>();

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public LocaleBean getLocaleBean() {
        return localeBean;
    }

    public void setLocaleBean(LocaleBean localeBean) {
        this.localeBean = localeBean;
    }
    
    public List<String> getTestiLista() {
        if (testiLista.isEmpty()){
            testiLista.add("{name:\'string 1\'},{name:\'string 2\'}");
        }
        return testiLista;
    }

    public void setTestiLista(List<String> testiLista) {
        this.testiLista = testiLista;
    }
    
    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getSelectedFilename() {
        return selectedFilename;
    }

    public void setSelectedFilename(String selectedFilename) {
        this.selectedFilename = selectedFilename;
    }

    public String getActiveGPX() {
        return activeGPX;
    }

    public void setActiveGPX(String activeGPX) {
        this.activeGPX = activeGPX;
    }

    public UploadStatus getStatus() {
        return status;
    }

    public void setStatus(UploadStatus status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
        
//    public String getFileName(){
//        return "gpx/lumooja.gpx";
//    }
    
    public void upload() {
        try {
            // --> mongoDB
            //MongoDbClient.getInstance().queryDocuments();
            // <-- mongoDB
            System.out.println("LOKALISOITU: " + localeBean.getLocalizedStr("login"));
            
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String path = GWUtilities.getPath();
            String filename = GWUtilities.getFilename(file);
            if (filename != null) {
                setSelectedFilename(filename);
                setActiveGPX("gpx/" + filename);
                File outputFile = new File(path + File.separator + "member" + File.separator
                        + "gpx" + File.separator + filename);
                inputStream = file.getInputStream();                                                                
                
                outputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                
                //System.out.println("UPLOADED FILE CONTENT: " + readFile(outputFile.getAbsolutePath()));
                
                // --> XML to JSON
                //System.out.println("KONVERTOITU: " + XmlToJson.toJSON(readFile(outputFile.getAbsolutePath())));
                // <-- XML to JSON
                //mdb.insertDocument(XmlToJson.toJSON(readFile(outputFile.getAbsolutePath())));
                
                MongoDbClient.getInstance().insertGPXFile(outputFile, loginBean.getUsername());
                
                setTime(fetchTime(GWUtilities.readFile(outputFile.getAbsolutePath())));
                
                setStatus(UploadStatus.SUCCESS);
                FacesMessage msg = new FacesMessage("File upload success!");
                msg.setSeverity(FacesMessage.SEVERITY_INFO);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                setStatus(UploadStatus.FAILED);            
                FacesMessage msg = new FacesMessage("File upload failed: could not parse filename.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }            
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(UploadStatus.FAILED);            
            FacesMessage msg = new FacesMessage("File upload failed: " + e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }       
    }
    
    private String fetchTime(String filecontent) throws Exception {
        //System.out.println("Time: " + XPathHelper.parseTime(is));
        //System.out.println("Time: " + XPathHelper.parseTime(filecontent));
        return XPathHelper.parseTime(filecontent);
    }
}
