package commands.server_side

import database.Organization
import database.OrganizationManagerInterface
import network.client.udp.User

class AddCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        organizationManager.add(argument as Organization)
        return Result.success(null)
    }
}
