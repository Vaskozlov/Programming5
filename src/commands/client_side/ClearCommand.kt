package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface

class ClearCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        organizationDatabase.clear()
        return Result.success(null)
    }
}
