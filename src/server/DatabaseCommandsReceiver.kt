package server

import com.fasterxml.jackson.databind.JsonNode
import database.*
import lib.json.fromJson
import lib.net.udp.JsonHolder
import network.client.DatabaseCommand
import network.client.udp.User
import org.apache.logging.log4j.kotlin.Logging
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class DatabaseCommandsReceiver(
    port: Int,
    userStoragePath: Path,
    private val databaseStoragePath: Path
) : ServerWithAuthorization(port, "command", AuthorizationManager(userStoragePath)), Logging {
    private var usersDatabases: MutableMap<AuthorizationHeader, OrganizationDatabase> = HashMap()
    private val commandArguments: MutableMap<DatabaseCommand, (AuthorizationHeader, JsonHolder) -> Any?> = mutableMapOf(
        DatabaseCommand.ADD to { _, jsonHolder ->
            jsonMapper.fromJson<Organization>(
                getObjectNode(
                    jsonHolder
                )
            )
        },
        DatabaseCommand.REMOVE_BY_ID to { _, jsonHolder ->
            jsonMapper.fromJson<Int>(
                getObjectNode(
                    jsonHolder
                )
            )
        },
        DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS to { _, jsonHolder ->
            jsonMapper.fromJson<Address>(
                getObjectNode(
                    jsonHolder
                )
            )
        },
        DatabaseCommand.READ to { _, jsonHolder ->
            jsonMapper.fromJson<String>(
                getObjectNode(
                    jsonHolder
                )
            )
        },
        DatabaseCommand.SAVE to { authorizationHeader, _ ->
            getUserDatabaseFile(authorizationHeader).absolutePathString()
        },
        DatabaseCommand.EXIT to { _, _ -> null },
        DatabaseCommand.CLEAR to { _, _ -> null },
        DatabaseCommand.REMOVE_HEAD to { _, _ -> null },
        DatabaseCommand.MAX_BY_FULL_NAME to { _, _ -> null },
        DatabaseCommand.CLEAR to { _, _ -> null },
        DatabaseCommand.INFO to { _, _ -> null },
        DatabaseCommand.HISTORY to { _, _ -> null },
        DatabaseCommand.SUM_OF_ANNUAL_TURNOVER to { _, _ -> null },
    )

    init {
        commandArguments[DatabaseCommand.ADD_IF_MAX] = commandArguments[DatabaseCommand.ADD]!!
        commandArguments[DatabaseCommand.UPDATE] = commandArguments[DatabaseCommand.ADD]!!

        commandArguments[DatabaseCommand.SHOW] = commandArguments[DatabaseCommand.READ]!!

        val databaseDir = databaseStoragePath.toFile()
        databaseDir.mkdirs()
        require(databaseDir.isDirectory)
    }

    private suspend fun execute(
        command: DatabaseCommand,
        user: User,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Any?>? {
        return commandMap[command]?.execute(user, organizationManager, argument)
    }

    private fun getArgumentForTheCommand(
        command: DatabaseCommand,
        authorizationHeader: AuthorizationHeader,
        jsonHolder: JsonHolder
    ): Any? {
        return commandArguments[command]?.invoke(authorizationHeader, jsonHolder)
    }

    private suspend fun sendResult(
        user: User,
        result: Result<Any?>
    ) {
        val code = if (result.isSuccess) NetworkCode.SUCCESS else errorToNetworkCode(result.exceptionOrNull())
        logger.trace("Sending result to $user, code: $code")
        send(user, code, result.getOrNull())
    }

    override suspend fun handleAuthorized(
        user: User,
        authorizationHeader: AuthorizationHeader,
        jsonHolder: JsonHolder
    ) {
        val commandName = getCommandFromJson(jsonHolder)
        val database: OrganizationManagerInterface =
            usersDatabases.getOrPut(
                authorizationHeader
            ) { OrganizationDatabase(getUserDatabaseFile(authorizationHeader)) }

        if (!DatabaseCommand.entries.map { it.name }.contains(commandName)) {
            send(user, NetworkCode.NOT_SUPPOERTED_COMMAND, null)
            return
        }

        val command = DatabaseCommand.valueOf(commandName)
        logger.trace("Received command $command, from $user")

        if (!commandArguments.containsKey(command)) {
            logger.trace("Command: $command not found, from $user")
            send(user, NetworkCode.NOT_SUPPOERTED_COMMAND, null)
            return
        }

        logger.trace("Executing command: $command , from $user")
        val result =
            execute(command, user, database, getArgumentForTheCommand(command, authorizationHeader, jsonHolder))
        sendResult(user, result!!)
    }

    private fun getObjectNode(jsonHolder: JsonHolder): JsonNode {
        return jsonHolder.getNode("value")
    }

    private fun getUserDatabaseFile(authorizationHeader: AuthorizationHeader): Path {
        return databaseStoragePath.resolve("${authorizationHeader.login}.csv")
    }
}
