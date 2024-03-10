package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface

class ClearCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override suspend fun executeImplementation(argument: Any?): Result<Unit?> {
        assert(argument == null)

        organizationDatabase.clear()
        return Result.success(null)
    }
}
