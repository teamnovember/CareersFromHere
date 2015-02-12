package helpers;

import java.security.NoSuchAlgorithmException;

/**
 * Created by biko on 12/02/15.
 */
public class AppException extends NoSuchAlgorithmException {
    public AppException(){
        super();
    }

    public AppException(String message){
        super(message);
    }

    public AppException(String message, Throwable cause){
        super(message,cause);
    }

    public AppException(Throwable cause){
        super(cause);
    }


}
