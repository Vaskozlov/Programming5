package server

import com.fasterxml.jackson.databind.JsonNode
import database.*
import exceptions.*
import lib.json.fromJson
import lib.json.toJson
import lib.net.udp.JsonHolder
import network.client.DatabaseCommand
import network.client.udp.ConnectionStatus
import network.client.udp.User
import org.apache.logging.log4j.kotlin.Logging

class DatabaseCommandsReceiver(port: Int) : ServerWithConnectAndDisconnectCommand(port, "command"), Logging {
    private var usersDatabases: MutableMap<User, OrganizationDatabase> = HashMap()

    private suspend fun executeAndSendResult(
        command: DatabaseCommand,
        user: User,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ) {
        if (!commandMap.containsKey(command)) {
            send(user, NetworkCode.NOT_SUPPOERTED_COMMAND, null)
            return
        }

        @SuppressWarnings("kotlin:S6611")
        val result = commandMap[command]!!.execute(user, organizationManager, argument)

        if (result.isSuccess) {
            send(user, NetworkCode.SUCCESS, jsonMapper.toJson(result.getOrNull()))
        } else {
            send(user, errorToNetworkCode(result.exceptionOrNull()), null)
        }
    }

    override suspend fun handleWhenConnected(jsonHolder: JsonHolder) {
        val user: User = jsonHolder.user
        val commandName = getCommandFromJson(jsonHolder)
        val database: OrganizationManagerInterface = usersDatabases.getOrDefault(user, OrganizationDatabase())

        if (!DatabaseCommand.entries.map { it.name }.contains(commandName)) {
            send(user, NetworkCode.NOT_SUPPOERTED_COMMAND, null)
            return
        }

        val command = DatabaseCommand.valueOf(commandName)
        logger.trace("Received command $command, from $user")

        when (command) {
            DatabaseCommand.SHOW, DatabaseCommand.HISTORY, DatabaseCommand.INFO, DatabaseCommand.SUM_OF_ANNUAL_TURNOVER ->
                executeAndSendResult(command, user, database, null)

            DatabaseCommand.ADD, DatabaseCommand.ADD_IF_MAX, DatabaseCommand.UPDATE ->
                executeAndSendResult(
                    command,
                    user,
                    database,
                    jsonMapper.fromJson<Organization>(getObjectNode(jsonHolder))
                )

            DatabaseCommand.REMOVE_BY_ID ->
                executeAndSendResult(command, user, database, jsonMapper.fromJson<Int>(getObjectNode(jsonHolder)))

            DatabaseCommand.CLEAR ->
                executeAndSendResult(command, user, database, null)

            DatabaseCommand.REMOVE_HEAD, DatabaseCommand.MAX_BY_FULL_NAME ->
                executeAndSendResult(command, user, database, null)

            DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS ->
                executeAndSendResult(command, user, database, jsonMapper.fromJson<Address>(getObjectNode(jsonHolder)))

            DatabaseCommand.SAVE, DatabaseCommand.READ ->
                executeAndSendResult(command, user, database, jsonMapper.fromJson<String>(getObjectNode(jsonHolder)))

            DatabaseCommand.EXIT -> {
                executeAndSendResult(command, user, database, null)
                handleDisconnectCommand(user)
            }

            else -> send(user, NetworkCode.NOT_SUPPOERTED_COMMAND, null)
        }
    }

    override fun handleConnectCommand(user: User) {
        val org = OrganizationDatabase()
        org.loadFromFile("/Users/vaskozlov/IdeaProjects/Programming5/test_database/test2.csv")
        usersDatabases[user] = org
    }

    override fun handleDisconnectCommand(user: User) {
        usersDatabases.remove(user)
    }

    private fun getObjectNode(jsonHolder: JsonHolder): JsonNode {
        return jsonHolder.getNode("object")
    }
}
