package server

import lib.net.udp.Server
import network.client.udp.ConnectionStatus
import network.client.udp.User
import org.apache.logging.log4j.kotlin.Logging
import java.net.DatagramPacket

abstract class ServerWithConnectAndDisconnectCommand protected constructor(port: Int, commandFieldName: String) :
    ServerWithCommands(port, commandFieldName), Logging {
    protected abstract fun handleConnectCommand(user: User)

    protected abstract fun handleDisconnectCommand(user: User)

    protected fun handleConnectAndDisconnectCommands(packet: DatagramPacket): ConnectionStatus {
        val command = getCommandFromJson(packet)
        val user: User = Server.Companion.getUserFromPacket(packet)

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

}
