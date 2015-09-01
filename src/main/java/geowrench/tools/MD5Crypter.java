package geowrench.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Tero Rantanen
 */
public class MD5Crypter {
    private static MessageDigest digest;
    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            // TODO: logging 
            e.printStackTrace();
        }
    }
    
    public static String crypt(String str) {
        if (StringUtils.isEmpty(str)){
            throw new IllegalArgumentException("String to encrypt cannot be empty!");
        }
        digest.update(str.getBytes());
        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
            }
            else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }        
}
