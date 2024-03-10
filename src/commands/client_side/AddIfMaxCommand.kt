package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.Organization
import database.OrganizationManagerInterface
import exceptions.NotMaximumOrganizationException
import lib.ExecutionStatus

class AddIfMaxCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        if (organizationDatabase.addIfMax(argument as Organization) == ExecutionStatus.FAILURE) {
            return Result.failure(NotMaximumOrganizationException());
        }

        return Result.success(null)
    }
}
