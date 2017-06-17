package csm.exceptions;

/**
 * @author Thomas Povinelli
 * Created 6/1/17
 * In CourseSiteManager
 */

public class InvalidDateException extends Exception {

    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(Throwable cause) {
        super(cause);
    }
}
