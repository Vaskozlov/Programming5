package client

import kotlinx.coroutines.runBlocking
import lib.net.udp.Client
import lib.net.udp.convertToString
import network.client.DatabaseCommand
import network.client.udp.Frame
import java.net.InetAddress

class CommandSender(address: InetAddress, port: Int) : Client(address, port) {
    private var connected: Boolean = false

    suspend fun sendCommand(command: DatabaseCommand, value: Any?) {
        connect()

        val frame = Frame(command, value)
        send(frame)
    }

    private suspend fun connect() {
        data class ConnectionFrame(val command: String)

        if (!connected) {
            val frame = ConnectionFrame("Connect")
            send(frame)
        }

        connected = true
    }
}

fun main() = runBlocking {
    val sender = CommandSender(InetAddress.getLocalHost(), 6789)
    sender.sendCommand(DatabaseCommand.SHOW, null)
    println(sender.receive().convertToString())

    sender.sendCommand(DatabaseCommand.CLEAR, null)
    println(sender.receive().convertToString())

    sender.sendCommand(DatabaseCommand.SHOW, null)
    println(sender.receive().convertToString())
}