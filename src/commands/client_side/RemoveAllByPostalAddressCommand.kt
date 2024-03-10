package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.Address
import database.OrganizationManagerInterface

class RemoveAllByPostalAddressCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override suspend fun executeImplementation(argument: Any?): Result<Unit?> {
        organizationDatabase.removeAllByPostalAddress(argument as Address)
        return Result.success(null)
    }
}
