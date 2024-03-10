package commands.server_side

import database.Organization
import database.OrganizationManagerInterface
import exceptions.NotMaximumOrganizationException
import lib.ExecutionStatus
import network.client.udp.User

class AddIfMaxCommand : ServerSideCommand {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        if (organizationManager.addIfMax(argument as Organization) == ExecutionStatus.FAILURE) {
            throw NotMaximumOrganizationException()
        }

        return Result.success(null)
    }
}
