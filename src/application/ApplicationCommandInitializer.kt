package application

import commands.client_side.*
import commands.client_side.core.Command
import lib.collections.ImmutablePair
import network.client.DatabaseCommand

object ApplicationCommandInitializer {
    fun getCommand(application: Application): Map<DatabaseCommand, Command> {
        return mapOf(
            DatabaseCommand.HELP to HelpCommand(application),
            DatabaseCommand.INFO to InfoCommand(application.organizationManager),
            DatabaseCommand.SHOW to ShowCommand(application.organizationManager),
            DatabaseCommand.ADD to AddCommand(application, application.organizationManager),
            DatabaseCommand.UPDATE to UpdateCommand(application, application.organizationManager),
            DatabaseCommand.REMOVE_BY_ID to RemoveByIdCommand(application.organizationManager),
            DatabaseCommand.CLEAR to ClearCommand(application.organizationManager),
            DatabaseCommand.SAVE to SaveCommand(application.organizationManager),
            DatabaseCommand.READ to ReadCommand(application.organizationManager),
            DatabaseCommand.EXECUTE_SCRIPT to ExecuteScriptCommand(application),
            DatabaseCommand.EXIT to ExitCommand(application),
            DatabaseCommand.REMOVE_HEAD to RemoveHeadCommand(application.organizationManager),
            DatabaseCommand.ADD_IF_MAX to AddIfMaxCommand(application, application.organizationManager),
            DatabaseCommand.HISTORY to PrintHistoryCommand(application),
            DatabaseCommand.MAX_BY_FULL_NAME to MaxByFullNameCommand(application.organizationManager),
            DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS to RemoveAllByPostalAddressCommand(
                application,
                application.organizationManager
            ),
            DatabaseCommand.SUM_OF_ANNUAL_TURNOVER to SumOfAnnualTurnoverCommand(application.organizationManager)
        )
    }
}
