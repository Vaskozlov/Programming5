package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

class ClearCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        assert(argument == null)
        organizationManager.clear()
        return Result.success(null)
    }
}
