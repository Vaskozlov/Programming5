package server

import lib.net.udp.JsonHolder
import lib.net.udp.Server

abstract class ServerWithCommands
protected constructor(port: Int, private val commandFieldName: String) : Server(port) {
    protected fun getCommandFromJson(jsonHolder: JsonHolder): String {
        val commandNode = jsonHolder.jsonNodeRoot[commandFieldName]
        return commandNode.asText()
    }
}
