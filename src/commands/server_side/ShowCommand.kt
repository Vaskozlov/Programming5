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
            null, "yaml" -> Result.success(organizationManager.toYaml())
            "json" -> Result.success(organizationManager.toJson())
            "csv" -> Result.success(organizationManager.toCSV())
            else -> Result.failure(InvalidOutputFormatException())
        }
    }
}
