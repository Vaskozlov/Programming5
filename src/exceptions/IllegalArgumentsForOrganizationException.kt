package exceptions;

public class IllegalArgumentsForOrganizationException extends IllegalArgumentException {
    public IllegalArgumentsForOrganizationException() {
        super();
    }

    public IllegalArgumentsForOrganizationException(String description) {
        super(description);
    }

    public IllegalArgumentsForOrganizationException(String description, Throwable cause) {
        super(description, cause);
    }

    public IllegalArgumentsForOrganizationException(Throwable cause) {
        super(cause);
    }
}
