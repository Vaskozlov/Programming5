package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface
import exceptions.InvalidOutputFormatException

class ShowCommand(organizationDatabase: OrganizationManagerInterface) :
    ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<String> {
        val mode = argument as String?

        return when (mode) {
            null, "yaml" -> Result.success(organizationDatabase.toYaml())
            "json" -> Result.success(organizationDatabase.toJson())
            "csv" -> Result.success(organizationDatabase.toCSV())
            else -> Result.failure(InvalidOutputFormatException())
        }
    }
}
