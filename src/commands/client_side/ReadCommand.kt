package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface
import exceptions.FileReadException
import lib.ExecutionStatus

class ReadCommand(organizationDatabase: OrganizationManagerInterface) :
    ServerSideCommand(organizationDatabase) {
    override suspend fun executeImplementation(argument: Any?): Result<Unit?> {
        val filename = argument as String

        if (organizationDatabase.loadFromFile(filename) == ExecutionStatus.FAILURE) {
            return Result.failure(FileReadException(filename))
        }

        return Result.success(null)
    }
}
