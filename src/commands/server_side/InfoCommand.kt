package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

class InfoCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<String> {
        assert(argument == null)
        return Result.success(organizationManager.info)
    }
}
