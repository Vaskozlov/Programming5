package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface
import exceptions.OrganizationKeyError
import lib.ExecutionStatus

class RemoveByIdCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        if (organizationDatabase.removeById(argument as Int) == ExecutionStatus.FAILURE) {
            return Result.failure(OrganizationKeyError("$argument"))
        }

        return Result.success(null)
    }
}
