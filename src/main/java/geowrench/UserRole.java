package geowrench;

/**
 *
 * @author Tero
 */
public enum UserRole {
    VISITOR(0),
    MEMBER(1),
    ADMIN(2);
       
    private final int value;
    
    private UserRole(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
    //for(UserRole role: UserRole.values())
    //System.out.println(role.name());
}

