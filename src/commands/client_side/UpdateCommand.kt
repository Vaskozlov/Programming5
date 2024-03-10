package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.Organization
import database.OrganizationManagerInterface

class UpdateCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override suspend fun executeImplementation(argument: Any?): Result<Unit?> {
        organizationDatabase.modifyOrganization(argument as Organization)
        return Result.success(null)
    }
}
