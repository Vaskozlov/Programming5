package application

import commands.client_side.*
import commands.client_side.core.Command
import database.OrganizationDatabase
import database.OrganizationManagerInterface
import exceptions.*
import kotlinx.coroutines.runBlocking
import lib.BufferedReaderWithQueueOfStreams
import lib.Localization
import lib.collections.CircledStorage
import network.client.DatabaseCommand
import java.io.InputStreamReader

class Application {
    val organizationManager: OrganizationManagerInterface = OrganizationDatabase()
    val commandsHistory: CircledStorage<String> = CircledStorage(11)
    val bufferedReaderWithQueueOfStreams: BufferedReaderWithQueueOfStreams = BufferedReaderWithQueueOfStreams(
        InputStreamReader(System.`in`)
    )

    private var needToStop = false
    private var localNameToDatabaseCommand: HashMap<String, DatabaseCommand> = HashMap()
    private val commandNameToDatabaseCommand = mapOf(
        "command.help" to DatabaseCommand.HELP,
        "command.info" to DatabaseCommand.INFO,
        "command.show" to DatabaseCommand.SHOW,
        "command.add" to DatabaseCommand.ADD,
        "command.update" to DatabaseCommand.UPDATE,
        "command.remove_by_id" to DatabaseCommand.REMOVE_BY_ID,
        "command.clear" to DatabaseCommand.CLEAR,
        "command.save" to DatabaseCommand.SAVE,
        "command.read" to DatabaseCommand.READ,
        "command.execute_script" to DatabaseCommand.EXECUTE_SCRIPT,
        "command.exit" to DatabaseCommand.EXIT,
        "command.remove_head" to DatabaseCommand.REMOVE_HEAD,
        "command.add_if_max" to DatabaseCommand.ADD_IF_MAX,
        "command.history" to DatabaseCommand.HISTORY,
        "command.max_by_full_name" to DatabaseCommand.MAX_BY_FULL_NAME,
        "command.remove_all_by_postal_address" to DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS,
        "command.sum_of_annual_turnover" to DatabaseCommand.SUM_OF_ANNUAL_TURNOVER
    )

    private val databaseCommandToExecutor: Map<DatabaseCommand, Command> = mapOf(
        DatabaseCommand.HELP to HelpCommand(this),
        DatabaseCommand.INFO to InfoCommand(organizationManager),
        DatabaseCommand.SHOW to ShowCommand(organizationManager),
        DatabaseCommand.ADD to AddCommand(this, organizationManager),
        DatabaseCommand.UPDATE to UpdateCommand(this, organizationManager),
        DatabaseCommand.REMOVE_BY_ID to RemoveByIdCommand(organizationManager),
        DatabaseCommand.CLEAR to ClearCommand(organizationManager),
        DatabaseCommand.SAVE to SaveCommand(organizationManager),
        DatabaseCommand.READ to ReadCommand(organizationManager),
        DatabaseCommand.EXECUTE_SCRIPT to ExecuteScriptCommand(this),
        DatabaseCommand.EXIT to ExitCommand(this),
        DatabaseCommand.REMOVE_HEAD to RemoveHeadCommand(organizationManager),
        DatabaseCommand.ADD_IF_MAX to AddIfMaxCommand(this, organizationManager),
        DatabaseCommand.HISTORY to PrintHistoryCommand(this),
        DatabaseCommand.MAX_BY_FULL_NAME to MaxByFullNameCommand(organizationManager),
        DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS to RemoveAllByPostalAddressCommand(
            this, organizationManager
        ),
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

        //val databaseCommandsReceiver = DatabaseCommandsReceiver(6789)
        //databaseCommandsReceiver.run()

        localize()
        printIntroductionMessage()

        while (!needToStop) {
            processCommand(bufferedReaderWithQueueOfStreams.readLine().trim())
        }
    }

    fun stop() {
        needToStop = true
    }

    private fun processCommand(command: String) {
        val allArguments: Array<String> = command.split(" +".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val commandName = allArguments[0]
        val argument = allArguments.getOrNull(1)
        val databaseCommand = localNameToDatabaseCommand[commandName]

        if (databaseCommand != null) {
            executeCommand(databaseCommand, argument)
        } else {
            println(Localization.get("message.command.not_found"))
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

    fun addCommandToHistory(command: String) {
        commandsHistory.add(command)
    }

    private fun printIntroductionMessage() {
        println(Localization.get("message.introduction"))
    }

    private fun commandSuccessMessage(command: DatabaseCommand, argument: Any?): String {
        return when (command) {
            DatabaseCommand.ADD, DatabaseCommand.ADD_IF_MAX ->
                Localization.get("message.collection.add.succeed")

            DatabaseCommand.REMOVE_BY_ID ->
                Localization.get("message.organization_removed")

            DatabaseCommand.REMOVE_HEAD, DatabaseCommand.MAX_BY_FULL_NAME ->
                argument as String

            DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS ->
                Localization.get("message.organizations_by_postal_address_removed")

            DatabaseCommand.UPDATE ->
                Localization.get("message.organization_modified")

            DatabaseCommand.CLEAR ->
                Localization.get("message.collection_cleared")

            DatabaseCommand.SAVE ->
                String.format("%s %s.", Localization.get("message.collection.saved_to_file"), argument)

            DatabaseCommand.READ ->
                String.format("%s.", Localization.get("message.collection.load.succeed"))

            DatabaseCommand.EXIT ->
                Localization.get("message.exit")

            DatabaseCommand.HISTORY -> {
                val history = argument as CircledStorage<String>
                val stringBuilder = StringBuilder()
                history.applyFunctionOnAllElements { s: String? -> stringBuilder.append(s).append("\n") }
                stringBuilder.toString()
            }

            DatabaseCommand.EXECUTE_SCRIPT ->
                Localization.get("message.script_execution.started")

            DatabaseCommand.INFO, DatabaseCommand.SHOW, DatabaseCommand.HELP ->
                argument as String

            DatabaseCommand.SUM_OF_ANNUAL_TURNOVER ->
                String.format(
                    "%s: %f.",
                    Localization.get("message.sum_of_annual_turnover"),
                    argument as Float?
                )
        }
    }

    private fun exceptionToMessage(exception: Throwable): String {
        return when (exception) {
            is UnableToReadFromFileException -> String.format(
                "%s %s.",
                Localization.get("message.collection.load.failed"),
                exception.message!!
            )

            is UnableToWriteFromFileException -> String.format(
                "%s %s.",
                Localization.get("message.collection.unable_to_save_to_file"),
                exception.message!!
            )

            is NotMaximumOrganizationException -> Localization.get(
                "message.collection.add.max_check_failed"
            )

            is KeyboardInterruptException -> Localization.get(
                "message.operation.canceled"
            )

            is IllegalArgumentException -> Localization.get(
                "message.organization.modification_error"
            )

            is OrganizationAlreadyPresentedException -> Localization.get(
                "message.organization.error.already_presented"
            )

            is OrganizationKeyError -> Localization.get(
                "message.organization.error.key_error"
            )

            is OrganizationNotFoundException -> Localization.get(
                "message.organization.error.not_found"
            )

            else -> Localization.get("message.command.failed")
        }
    }
}
