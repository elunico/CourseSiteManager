package csm.exceptions;

/**
 * @author Thomas Povinelli
 *         Created 5/24/17
 *         In CourseSiteManager
 */
public class InvalidDateException extends Exception {
    public InvalidDateException(String format) {
        super(format);
    }
}
