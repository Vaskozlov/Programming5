package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface
import exceptions.FileWriteException
import lib.ExecutionStatus

class SaveCommand(organizationDatabase: OrganizationManagerInterface) :
    ServerSideCommand(organizationDatabase) {
    override suspend fun executeImplementation(argument: Any?): Result<Unit?> {
        assert(argument == null)

        if (organizationDatabase.save("").await() == ExecutionStatus.FAILURE) {
            return Result.failure(FileWriteException())
        }

        return Result.success(null)
    }
}
