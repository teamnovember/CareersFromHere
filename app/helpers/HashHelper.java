package helpers;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by biko on 12/02/15.
 */
public class HashHelper {
    private static HashHelper instance = null;
    protected HashHelper(){
        //Exists only to defeat instantiation
    }

    public static HashHelper getInstance(){
        if(instance == null){
            instance = new HashHelper();
        }
        return instance;
    }

    public static String createPassword(String pass) throws AppException {
        if (pass == null) {
            throw new AppException("empty.password");
        }
        return BCrypt.hashpw(pass, BCrypt.gensalt());
    }

    public static boolean checkPassword(String candidate, String encryptedPassword){
        if(candidate == null){
            return false;
        }
        if (encryptedPassword == null){
            return false;
        }
        return BCrypt.checkpw(candidate,encryptedPassword);
    }
}
