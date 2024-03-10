package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

class ShowCommand : ServerSideCommand {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<String?> {
        assert(argument == null)
        return Result.success(organizationManager.toYaml())
    }
}
