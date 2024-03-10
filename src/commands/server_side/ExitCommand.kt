package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

class ExitCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        return Result.success(null)
    }
}
