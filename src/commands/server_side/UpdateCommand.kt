package commands.server_side

import database.Organization
import database.OrganizationManagerInterface
import network.client.udp.User

class UpdateCommand : ServerSideCommand {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?,
    ): Result<Unit?> {
        organizationManager.modifyOrganization(argument as Organization)
        return Result.success(null)
    }
}
