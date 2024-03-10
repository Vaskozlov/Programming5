package commands.server_side

import database.OrganizationManagerInterface
import exceptions.FileWriteException
import lib.ExecutionStatus
import network.client.udp.User

class SaveCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        val filename = argument as String

        if (organizationManager.saveToFile(filename) == ExecutionStatus.FAILURE) {
            return Result.failure(FileWriteException(filename))
        }

        return Result.success(null)
    }
}
