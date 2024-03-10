package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.Organization
import database.OrganizationManagerInterface

class AddCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override suspend fun executeImplementation(argument: Any?): Result<Unit?> {
        organizationDatabase.add(argument as Organization)
        return Result.success(null);
    }
}
