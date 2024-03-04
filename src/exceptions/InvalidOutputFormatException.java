package exceptions;

public class InvalidOutputFormatException extends Exception {
    public InvalidOutputFormatException() {
        super();
    }

    public InvalidOutputFormatException(String description) {
        super(description);
    }

    public InvalidOutputFormatException(String description, Throwable cause) {
        super(description, cause);
    }

    public InvalidOutputFormatException(Throwable cause) {
        super(cause);
    }
}