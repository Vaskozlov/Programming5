package commands.server_side

import database.OrganizationManagerInterface
import exceptions.InvalidOutputFormatException
import network.client.udp.User

class ShowCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<String> {
        return when (argument as String?) {
            null, "yaml" -> return Result.success(organizationManager.toYaml())
            "json" -> return Result.success(organizationManager.toJson())
            "csv" -> return Result.success(organizationManager.toCSV())
            else -> return Result.failure(InvalidOutputFormatException())
        }
    }
}
