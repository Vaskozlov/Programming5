package exceptions;

public class OrganizationAlreadyPresentedException extends Exception {
    public OrganizationAlreadyPresentedException() {
        super();
    }

    public OrganizationAlreadyPresentedException(String description) {
        super(description);
    }

    public OrganizationAlreadyPresentedException(String description, Throwable cause) {
        super(description, cause);
    }

    public OrganizationAlreadyPresentedException(Throwable cause) {
        super(cause);
    }
}
