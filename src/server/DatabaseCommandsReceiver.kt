package server

import com.fasterxml.jackson.databind.JsonNode
import commands.server_side.*
import database.*
import exceptions.NotMaximumOrganizationException
import exceptions.OrganizationAlreadyPresentedException
import exceptions.OrganizationNotFoundException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import network.client.DatabaseCommand
import network.client.udp.ConnectionStatus
import network.client.udp.User
import org.apache.logging.log4j.kotlin.Logging
import java.net.DatagramPacket
import java.util.*

class DatabaseCommandsReceiver(port: Int) : ServerWithConnectAndDisconnectCommand(port, "command"), Logging {
    var usersDatabases: MutableMap<User, OrganizationDatabase> = HashMap()
    var commandMap: MutableMap<DatabaseCommand, ServerSideCommand> = EnumMap(
        DatabaseCommand::class.java
    )

    init {
        objectMapper.findAndRegisterModules()
        commandMap[DatabaseCommand.ADD] = AddCommand()
        commandMap[DatabaseCommand.ADD_IF_MAX] = AddIfMaxCommand()
        commandMap[DatabaseCommand.SHOW] = ShowCommand()
        commandMap[DatabaseCommand.CLEAR] = ClearCommand()
        commandMap[DatabaseCommand.INFO] = InfoCommand()
        commandMap[DatabaseCommand.MAX_BY_FULL_NAME] = MaxByFullNameCommand()
        commandMap[DatabaseCommand.REMOVE_HEAD] = RemoveHeadCommand()
        commandMap[DatabaseCommand.REMOVE_BY_ID] = RemoveByIdCommand()
        commandMap[DatabaseCommand.SAVE] = SaveCommand()
        commandMap[DatabaseCommand.READ] = ReadCommand()
        commandMap[DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS] = RemoveAllByPostalAddressCommand()
        commandMap[DatabaseCommand.UPDATE] = UpdateCommand()
        commandMap[DatabaseCommand.EXIT] = ExitCommand()
    }

    private suspend fun sendStringCallback(
        command: DatabaseCommand,
        user: User,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ) {
        val result = commandMap[command]!!.execute(user, organizationManager, argument)
        if (result.isSuccess) {
            send(user, NetworkCode.SUCCESS, objectMapper.writeValueAsString(result.getOrNull()))
        } else {
            send(user, NetworkCode.FAILURE, null)
        }
    }

    private suspend fun statusOnlyCallback(
        command: DatabaseCommand,
        user: User,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ) {
        assert(argument == null)

        println("main runBlocking: I'm working in thread ${Thread.currentThread().threadId()}")

        val result = commandMap[command]!!.execute(user, organizationManager, argument)

        if (result.isSuccess) {
            send(user, NetworkCode.SUCCESS, null)
        } else {
            send(user, NetworkCode.FAILURE, null)
        }
    }

    override suspend fun handlePacket(packet: DatagramPacket) = coroutineScope {
        val status = handleConnectAndDisconnectCommands(packet)

        println("main runBlocking: I'm working in thread ${Thread.currentThread().threadId()}")

        if (status != ConnectionStatus.CONNECTED) {
            return@coroutineScope
        }

        val commandName = getCommandFromJson(packet)
        val user: User = getUserFromPacket(packet)
        val database: OrganizationManagerInterface = usersDatabases.get(user)!!

        if (!DatabaseCommand.entries.map { it.name }.contains(commandName)) {
            send(user, NetworkCode.NOT_SUPPOERTED_COMMAND, null)
            return@coroutineScope
        }

        val command = DatabaseCommand.valueOf(commandName)
        logger.trace("Received command $command, from $user")

        when (command) {
            DatabaseCommand.SHOW, DatabaseCommand.HISTORY, DatabaseCommand.INFO, DatabaseCommand.SUM_OF_ANNUAL_TURNOVER ->
                async { sendStringCallback(command, user, database, null) }

            DatabaseCommand.ADD, DatabaseCommand.ADD_IF_MAX, DatabaseCommand.UPDATE ->
                statusOnlyCallback(command, user, database, getOrganization(getObjectNode(packet)))

            DatabaseCommand.REMOVE_BY_ID ->
                statusOnlyCallback(command, user, database, getInt(getObjectNode(packet)))

            DatabaseCommand.CLEAR ->
                statusOnlyCallback(command, user, database, null)

            DatabaseCommand.REMOVE_HEAD, DatabaseCommand.MAX_BY_FULL_NAME ->
                sendStringCallback(command, user, database, null)

            DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS ->
                statusOnlyCallback(command, user, database, getAddress(getObjectNode(packet)))

            DatabaseCommand.SAVE, DatabaseCommand.READ ->
                statusOnlyCallback(command, user, database, getString(getObjectNode(packet)))

            DatabaseCommand.EXIT -> {
                statusOnlyCallback(command, user, database, getOrganization(getObjectNode(packet)))
                handleDisconnectCommand(user)
            }

            else -> send(user, NetworkCode.NOT_SUPPOERTED_COMMAND, null)
        }
    }

    fun errorToNetworkCode(error: Exception): NetworkCode {
        return when (error) {
            is OrganizationAlreadyPresentedException -> NetworkCode.ORGANIZATION_ALREADY_EXISTS

            is OrganizationNotFoundException -> NetworkCode.NOT_FOUND

            is NotMaximumOrganizationException -> NetworkCode.NOT_A_MAXIMUM_ORGANIZATION

            else -> NetworkCode.FAILURE
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

    private fun getOrganization(jsonNode: JsonNode): Organization {
        return objectMapper.readValue(jsonNode.toString(), Organization::class.java)
    }

    private fun getAddress(jsonNode: JsonNode): Address {
        return objectMapper.readValue(jsonNode.toString(), Address::class.java)
    }

    private fun getString(jsonNode: JsonNode): String {
        return objectMapper.readValue(jsonNode.toString(), String::class.java)
    }

    private fun getInt(jsonNode: JsonNode): Int {
        return objectMapper.readValue(jsonNode.toString(), Int::class.java)
    }

    protected fun getObjectNode(packet: DatagramPacket): JsonNode {
        val result = String(packet.data, 0, packet.length)
        val jsonNodeRoot = objectMapper.readTree(result)
        return jsonNodeRoot["object"]
    }
}
