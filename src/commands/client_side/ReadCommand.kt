package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface
import exceptions.UnableToReadFromFileException
import lib.ExecutionStatus

class ReadCommand(organizationDatabase: OrganizationManagerInterface) :
    ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        val filename = argument as String

        if (organizationDatabase.loadFromFile(filename) == ExecutionStatus.FAILURE) {
            return Result.failure(UnableToReadFromFileException(filename))
        }

        return Result.success(null)
    }
}
