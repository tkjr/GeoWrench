package geowrench.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

/**
 *
 * @author Tero Rantanen
 */
public class GWUtilities {
    
    public static String getFilename(Part part) {
        if (part != null) {
            for (String content : part.getHeader("content-disposition").split(";")) {  
                if (content.trim().startsWith("filename")) {  
                    String filename = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");  
                    return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); 
                }  
            }  
        }
        return null;  
    }
    
    public static String getPath() {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
        return servletContext.getRealPath("");        
    }
    
    public static String readFile( String file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }
}
