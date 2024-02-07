package exceptions;

public class OrganizationNotFoundException extends Exception {
    public OrganizationNotFoundException() {
        super();
    }

    public OrganizationNotFoundException(String description) {
        super(description);
    }

    public OrganizationNotFoundException(String description, Throwable cause) {
        super(description, cause);
    }

    public OrganizationNotFoundException(Throwable cause) {
        super(cause);
    }
}
