package client

import com.fasterxml.jackson.databind.ObjectMapper
import lib.net.udp.Client
import network.client.DatabaseCommand
import network.client.udp.Frame
import java.io.IOException
import java.net.InetAddress

class CommandSender(address: InetAddress, port: Int) : Client(address, port) {
    var objectMapper: ObjectMapper = ObjectMapper()
    var connected: Boolean = false

    constructor(address: String, port: Int) : this(InetAddress.getByName(address), port)

    init {
        objectMapper.findAndRegisterModules()
    }

    fun sendCommand(command: DatabaseCommand, value: Any?) {
        connect()

        val frame = Frame(command, value)
        val json = objectMapper.writeValueAsString(frame)
        send(json.toByteArray())
    }

    private fun connect() {
        data class ConnectionFrame(val command: String)

        if (!connected) {
            val frame = ConnectionFrame("Connect")
            val json = objectMapper.writeValueAsString(frame)
            send(json.toByteArray())
        }

        connected = true
    }
}


fun main(args: Array<String>) {
    val sender = CommandSender(InetAddress.getLocalHost(), 6789)
    sender.sendCommand(DatabaseCommand.SHOW, null)
    println(sender.receive())

    sender.sendCommand(DatabaseCommand.CLEAR, null)
    println(sender.receive())

    sender.sendCommand(DatabaseCommand.SHOW, null)
    println(sender.receive())
}