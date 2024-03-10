package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

abstract class ServerSideCommand {
    suspend fun execute(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any? = null
    ): Result<Any?> {
        return try {
            executeImplementation(user, organizationManager, argument)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    protected abstract suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Any?>
}
