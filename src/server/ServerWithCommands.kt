package server

import lib.net.udp.JsonHolder
import lib.net.udp.Server
import kotlin.coroutines.CoroutineContext

abstract class ServerWithCommands
protected constructor(port: Int, context: CoroutineContext, private val commandFieldName: String) :
    Server(port, context) {
    protected fun getCommandFromJson(jsonHolder: JsonHolder): String {
        val commandNode = jsonHolder.jsonNodeRoot[commandFieldName]
        return commandNode.asText()
    }
}
