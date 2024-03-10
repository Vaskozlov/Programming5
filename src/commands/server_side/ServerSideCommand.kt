package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

interface ServerSideCommand {
    suspend fun execute(
        user: User?,
        organizationManager: OrganizationManagerInterface,
    ): Result<Any?> {
        return execute(user, organizationManager, null)
    }

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

    suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Any?>
}
