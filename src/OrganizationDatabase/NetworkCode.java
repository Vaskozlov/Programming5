package OrganizationDatabase;

public enum NetworkCode {
    SUCCESS(200),
    FAILURE(500),
    NOT_FOUND(404),
    NOT_A_MAXIMUM_ORGANIZATION(400),
    ORGANIZATION_ALREADY_EXISTS(401),
    ;

    final int value;

    NetworkCode(int code) {
        this.value = code;
    }
}
