package Server.Exceptions;

/**
 * Exception being thrown if a header was send using illegal characters like in setCookie("cookie=name", "value")
 *
 * @author Joscha Henningsen
 */
public class InvalidNameException extends RuntimeException {
    @Override
    public String toString() {
        return "A assignment with illegal characters was made";
    }
}
