package exceptions;

public class KeyboardInterruptException extends Exception {
    public KeyboardInterruptException() {
        super();
    }

    public KeyboardInterruptException(String description) {
        super(description);
    }

    public KeyboardInterruptException(String description, Throwable cause) {
        super(description, cause);
    }

    public KeyboardInterruptException(Throwable cause) {
        super(cause);
    }
}