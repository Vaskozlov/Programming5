package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface
import exceptions.FileWriteException
import lib.ExecutionStatus

class SaveCommand(organizationDatabase: OrganizationManagerInterface) :
    ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        val filename = argument as String

        if (organizationDatabase.saveToFile(filename) == ExecutionStatus.FAILURE) {
            return Result.failure(FileWriteException(filename))
        }

        return Result.success(null)
    }
}
