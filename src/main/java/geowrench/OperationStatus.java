package geowrench;

/**
 *
 * @author Tero Rantanen
 */
public enum OperationStatus {
    UNKNOWN("unknown"), SUCCESS("success"), FAILED("failed");
    
    private final String name;
    
    private OperationStatus(String s) {
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
