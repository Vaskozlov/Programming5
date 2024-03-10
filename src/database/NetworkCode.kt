package database

enum class NetworkCode(val value: Int) {
    SUCCESS(200),
    NOT_SUPPOERTED_COMMAND(300),
    NOT_A_MAXIMUM_ORGANIZATION(400),
    ORGANIZATION_ALREADY_EXISTS(401),
    NOT_FOUND(404),
    FAILURE(500),
}
