package application

import commands.client_side.*
import commands.client_side.core.Command
import database.OrganizationDatabase
import database.OrganizationManagerInterface
import kotlinx.coroutines.runBlocking
import lib.BufferedReaderWithQueueOfStreams
import lib.Localization
import lib.collections.CircledStorage
import network.client.DatabaseCommand
import server.DatabaseCommandsReceiver
import java.io.InputStreamReader

class Application {
    val organizationManager: OrganizationManagerInterface = OrganizationDatabase()
    val commandsHistory: CircledStorage<String> = CircledStorage(11)
    val bufferedReaderWithQueueOfStreams: BufferedReaderWithQueueOfStreams = BufferedReaderWithQueueOfStreams(
        InputStreamReader(System.`in`)
    )

    private var running = false
    private var localNameToDatabaseCommand: HashMap<String, DatabaseCommand> = HashMap()

    private val databaseCommandToExecutor: Map<DatabaseCommand, Command> = mapOf(
        DatabaseCommand.HELP to HelpCommand(),
        DatabaseCommand.INFO to InfoCommand(organizationManager),
        DatabaseCommand.SHOW to ShowCommand(organizationManager),
        DatabaseCommand.ADD to AddCommand(organizationManager),
        DatabaseCommand.UPDATE to UpdateCommand(organizationManager),
        DatabaseCommand.REMOVE_BY_ID to RemoveByIdCommand(organizationManager),
        DatabaseCommand.CLEAR to ClearCommand(organizationManager),
        DatabaseCommand.SAVE to SaveCommand(organizationManager),
        DatabaseCommand.READ to ReadCommand(organizationManager),
        DatabaseCommand.EXECUTE_SCRIPT to ExecuteScriptCommand(this),
        DatabaseCommand.EXIT to ExitCommand(this),
        DatabaseCommand.REMOVE_HEAD to RemoveHeadCommand(organizationManager),
        DatabaseCommand.ADD_IF_MAX to AddIfMaxCommand(organizationManager),
        DatabaseCommand.HISTORY to PrintHistoryCommand(this),
        DatabaseCommand.MAX_BY_FULL_NAME to MaxByFullNameCommand(organizationManager),
        DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS to RemoveAllByPostalAddressCommand(organizationManager),
        DatabaseCommand.SUM_OF_ANNUAL_TURNOVER to SumOfAnnualTurnoverCommand(organizationManager)
    )

    private fun loadCommands() {
        localNameToDatabaseCommand.clear()

        for ((key, value) in commandNameToDatabaseCommand) {
            localNameToDatabaseCommand[Localization.get(key)] = value
        }
    }

    private fun localize() {
        Localization.askUserForALanguage(bufferedReaderWithQueueOfStreams)
        loadCommands()
    }

    fun start(database: String?) = runBlocking {
        if (database != null) {
            organizationManager.loadFromFile(database)
        }

        localize()
        printIntroductionMessage()
        running = true;

        while (running) {
            processCommand(bufferedReaderWithQueueOfStreams.readLine().trim())
        }
    }

    fun stop() {
        running = false
    }

    private fun processCommand(command: String) {
        val allArguments: Array<String> = command.split(" +".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (allArguments.isEmpty()) {
            return
        }

        val commandName = allArguments[0]
        val argument = allArguments.getOrNull(1)
        val databaseCommand = localNameToDatabaseCommand[commandName]

        when (databaseCommand) {
            DatabaseCommand.EXIT, DatabaseCommand.HELP, DatabaseCommand.INFO,
            DatabaseCommand.HISTORY, DatabaseCommand.CLEAR, DatabaseCommand.SUM_OF_ANNUAL_TURNOVER,
            DatabaseCommand.MAX_BY_FULL_NAME, DatabaseCommand.REMOVE_HEAD ->
                executeCommand(databaseCommand, null);

            DatabaseCommand.ADD, DatabaseCommand.ADD_IF_MAX ->
                executeCommand(
                    databaseCommand,
                    OrganizationBuilder.constructOrganization(
                        bufferedReaderWithQueueOfStreams,
                        false
                    )
                )

            DatabaseCommand.UPDATE -> {
                val updatedOrganization = OrganizationBuilder.constructOrganization(
                    bufferedReaderWithQueueOfStreams,
                    true
                )

                updatedOrganization.id = argument?.toIntOrNull()
                executeCommand(databaseCommand, updatedOrganization)
            }

            DatabaseCommand.REMOVE_BY_ID ->
                executeCommand(databaseCommand, argument?.toIntOrNull())

            DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS ->
                executeCommand(
                    databaseCommand,
                    OrganizationBuilder.constructAddress(bufferedReaderWithQueueOfStreams, false)!!
                )

            DatabaseCommand.SAVE, DatabaseCommand.READ, DatabaseCommand.EXECUTE_SCRIPT, DatabaseCommand.SHOW ->
                executeCommand(databaseCommand, argument)

            null -> System.out.printf(Localization.get("message.command.not_found"), commandName)
        }
    }

    private fun executeCommand(databaseCommand: DatabaseCommand, argument: Any?) {
        val executor = databaseCommandToExecutor[databaseCommand]
        val result = executor!!.execute(argument)

        if (result.isSuccess) {
            val successMessage = commandSuccessMessage(databaseCommand, result.getOrNull())
            println(successMessage)
        } else {
            val exception = result.exceptionOrNull()!!
            val errorMessage = exceptionToMessage(exception)
            println(errorMessage)
        }

        addCommandToHistory(databaseCommand.name)
    }

    private fun addCommandToHistory(command: String) {
        commandsHistory.add(command)
    }

    private fun printIntroductionMessage() {
        println(Localization.get("message.introduction"))
    }
}
