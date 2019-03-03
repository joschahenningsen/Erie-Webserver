package Server.Exceptions;

public class InvalidNameException extends RuntimeException {
    @Override
    public String toString() {
        return "A assignment with illegal characters was made";
    }
}
