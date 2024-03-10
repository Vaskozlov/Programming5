package commands.server_side

import database.Address
import database.OrganizationManagerInterface
import network.client.udp.User

class RemoveAllByPostalAddressCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Unit?> {
        organizationManager.removeAllByPostalAddress(argument as Address)
        return Result.success(null)
    }
}
