package exceptions;

public class NoBundleLoadedException extends Error {
    public NoBundleLoadedException() {
        super();
    }

    public NoBundleLoadedException(String description) {
        super(description);
    }

    public NoBundleLoadedException(String description, Throwable cause) {
        super(description, cause);
    }

    public NoBundleLoadedException(Throwable cause) {
        super(cause);
    }
}
