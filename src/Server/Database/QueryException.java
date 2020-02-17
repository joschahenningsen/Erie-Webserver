package Server.Database;

/**
 * Exception throws if query is malformed
 *
 * @author Joscha Henningsen
 */
public class QueryException extends RuntimeException {
    private String message;

    QueryException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
