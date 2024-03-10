package commands.server_side

import database.Organization
import database.OrganizationManagerInterface
import network.client.udp.User

class RemoveHeadCommand : ServerSideCommand {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?,
    ): Result<Organization?> {
        assert(argument == null)
        return Result.success(organizationManager.removeHead());
    }
}
