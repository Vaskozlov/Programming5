package server

import lib.net.udp.Server
import java.net.DatagramPacket

abstract class ServerWithCommands protected constructor(port: Int, commandFieldName: String) : Server(port) {
    private val commandFieldName: String

    init {
        objectMapper.findAndRegisterModules()
        this.commandFieldName = commandFieldName
    }

    protected fun getCommandFromJson(packet: DatagramPacket): String {
        val result = String(packet.data, 0, packet.length)
        val jsonNodeRoot = objectMapper.readTree(result)
        val commandNode = jsonNodeRoot[commandFieldName]

        return commandNode.asText()
    }
}
