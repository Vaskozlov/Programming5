package commands.server_side

import database.OrganizationManagerInterface
import exceptions.FileReadException
import lib.ExecutionStatus
import network.client.udp.User

class ReadCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        val filename = argument as String

        if (organizationManager.loadFromFile(filename) == ExecutionStatus.FAILURE) {
            return Result.failure(FileReadException(filename))
        }

        return Result.success(null)
    }
}
