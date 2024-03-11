package client

import lib.net.udp.Client
import network.client.DatabaseCommand
import network.client.udp.Frame
import server.AuthorizationHeader
import java.net.InetAddress

class CommandSender(address: InetAddress, port: Int) : Client(address, port) {
    private var connected: Boolean = false

    init {
        setTimeout(10000)
    }

    suspend fun sendCommand(command: DatabaseCommand, value: Any?) {
        val frame = Frame(AuthorizationHeader("vaskozlov", "123"), command, value)
        send(frame)
    }
}
