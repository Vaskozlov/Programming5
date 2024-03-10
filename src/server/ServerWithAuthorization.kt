package server

import lib.json.fromJson
import lib.net.udp.JsonHolder
import network.client.udp.User

abstract class ServerWithAuthorization(
    port: Int,
    commandFieldName: String,
    private val authorizationManager: AuthorizationManager
) :
    ServerWithCommands(port, commandFieldName) {

    abstract suspend fun handleAuthorized(
        user: User,
        authorizationHeader: AuthorizationHeader,
        jsonHolder: JsonHolder
    )

    override suspend fun handlePacket(user: User, jsonHolder: JsonHolder) {
        val authorizationHeader = jsonMapper.fromJson<AuthorizationHeader>(jsonHolder.getNode("authorization"))

        if (!authorizationManager.isUserAuthorized(authorizationHeader)) {
            logger.warn("User $user is not authorized, it will be created")
            authorizationManager.addUser(authorizationHeader)
        } else {
            logger.info("Received packet from authorized user: ${authorizationHeader.login}")
        }

        handleAuthorized(user, authorizationHeader, jsonHolder)
    }
}