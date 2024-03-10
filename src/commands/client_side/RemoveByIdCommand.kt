package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface
import exceptions.OrganizationKeyException
import lib.ExecutionStatus

class RemoveByIdCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        if (organizationDatabase.removeById(argument as Int) == ExecutionStatus.FAILURE) {
            return Result.failure(OrganizationKeyException("$argument"))
        }

        return Result.success(null)
    }
}
