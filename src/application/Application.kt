package application

import commands.client_side.*
import commands.client_side.core.Command
import database.OrganizationManagerInterface
import kotlinx.coroutines.runBlocking
import lib.BufferedReaderWithQueueOfStreams
import lib.Localization
import lib.collections.CircledStorage
import network.client.DatabaseCommand
import java.io.InputStreamReader

class Application(val organizationManager: OrganizationManagerInterface) {
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

    private val argumentForCommand: Map<DatabaseCommand, (String?) -> Any?> = mapOf(
        DatabaseCommand.HELP to { null },
        DatabaseCommand.INFO to { null },
        DatabaseCommand.SHOW to { null },
        DatabaseCommand.ADD to {
            OrganizationBuilder.constructOrganization(
                bufferedReaderWithQueueOfStreams,
                false
            )
        },
        DatabaseCommand.UPDATE to {
            OrganizationBuilder.constructOrganization(
                bufferedReaderWithQueueOfStreams,
                true
            )
        },
        DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS to {
            OrganizationBuilder.constructAddress(
                bufferedReaderWithQueueOfStreams,
                false
            )
        },
        DatabaseCommand.ADD_IF_MAX to {
            OrganizationBuilder.constructOrganization(
                bufferedReaderWithQueueOfStreams,
                false
            )
        },
        DatabaseCommand.REMOVE_BY_ID to { it?.toIntOrNull() },
        DatabaseCommand.CLEAR to { null },
        DatabaseCommand.SAVE to { null },
        DatabaseCommand.READ to { it },
        DatabaseCommand.EXECUTE_SCRIPT to { it },
        DatabaseCommand.EXIT to { null },
        DatabaseCommand.REMOVE_HEAD to { null },
        DatabaseCommand.HISTORY to { null },
        DatabaseCommand.MAX_BY_FULL_NAME to { null }
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

    fun start(databasePath: String?) = runBlocking {
        if (databasePath != null) {
            organizationManager.loadFromFile(databasePath)
        }

        localize()
        printIntroductionMessage()
        running = true;

        while (running) {
            val line = bufferedReaderWithQueueOfStreams.readLine()
            processCommand(line.trim())
        }
    }

    fun stop() {
        running = false
    }

    private suspend fun processCommand(input: String) {
        val allArguments = splitInputIntoArguments(input)

        if (allArguments.isEmpty()) {
            return
        }

        val commandName = allArguments[0]
        val argument = allArguments.getOrNull(1)
        val databaseCommand = localNameToDatabaseCommand[commandName]

        if (databaseCommand == null) {
            System.out.printf(Localization.get("message.command.not_found"), commandName)
            return
        }

        executeCommand(databaseCommand, argumentForCommand[databaseCommand]!!.invoke(argument))
    }

    private suspend fun executeCommand(databaseCommand: DatabaseCommand, argument: Any?) {
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
