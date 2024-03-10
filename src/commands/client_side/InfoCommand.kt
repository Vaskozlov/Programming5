package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface

class InfoCommand(organizationDatabase: OrganizationManagerInterface) :
    ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<String> {
        return Result.success(organizationDatabase.info)
    }
}
