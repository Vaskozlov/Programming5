package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

class SaveCommand : ServerSideCommand {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        organizationManager.saveToFile(argument as String)
        return Result.success(null)
    }
}
