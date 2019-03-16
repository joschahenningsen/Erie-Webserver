package Server.Database;

public class QueryException extends RuntimeException {
    String message;

    public QueryException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
