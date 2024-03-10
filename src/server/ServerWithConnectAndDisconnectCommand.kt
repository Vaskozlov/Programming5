package server

import lib.net.udp.JsonHolder
import network.client.udp.ConnectionStatus
import network.client.udp.User
import org.apache.logging.log4j.kotlin.Logging

abstract class ServerWithConnectAndDisconnectCommand protected constructor(port: Int, commandFieldName: String) :
    ServerWithCommands(port, commandFieldName), Logging {

    protected abstract suspend fun handleWhenConnected(jsonHolder: JsonHolder)
    protected abstract fun handleConnectCommand(user: User)
    protected abstract fun handleDisconnectCommand(user: User)

    protected fun handleConnectAndDisconnectCommands(jsonHolder: JsonHolder): ConnectionStatus {
        val command = getCommandFromJson(jsonHolder)
        val user = jsonHolder.user

        return when (command) {
            "Connect" -> {
                handleConnectCommand(user)
                logger.trace("User $user connected")
                ConnectionStatus.JUST_CONNECTED
            }

            "Disconnect" -> {
                handleDisconnectCommand(user)
                logger.trace("User $user disconnected")
                ConnectionStatus.DISCONNECTED
            }

            else -> ConnectionStatus.CONNECTED
        }
    }

    override suspend fun handlePacket(jsonHolder: JsonHolder) {
        val status = handleConnectAndDisconnectCommands(jsonHolder)

        if (status == ConnectionStatus.CONNECTED) {
            handleWhenConnected(jsonHolder)
        }
    }
}
