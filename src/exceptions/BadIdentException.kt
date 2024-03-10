package exceptions;

public class BadIdentException extends Error {
    public BadIdentException() {
        super();
    }

    public BadIdentException(String description) {
        super(description);
    }

    public BadIdentException(String description, Throwable cause) {
        super(description, cause);
    }

    public BadIdentException(Throwable cause) {
        super(cause);
    }
}
