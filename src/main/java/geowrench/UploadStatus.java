package geowrench;

/**
 *
 * @author Tero Rantanen
 */
public enum UploadStatus {
    UNKNOWN("unknown"), SUCCESS("success"), FAILED("failed");
    
    private final String name;
    
    private UploadStatus(String s) {
        name = s;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
