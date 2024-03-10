package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

class RemoveByIdCommand : ServerSideCommand {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        organizationManager.removeById(argument as Int)
        return Result.success(null)
    }
}
