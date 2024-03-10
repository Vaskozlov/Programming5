package commands.client_side

import application.*
import commands.client_side.core.ServerAndClientSideCommand
import database.Address
import database.OrganizationManagerInterface
import lib.ExecutionStatus

class RemoveAllByPostalAddressCommand(
    application: Application,
    organizationDatabase: OrganizationManagerInterface
) : ServerAndClientSideCommand(application, organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        val organizationBuilder =
            UserInteractiveOrganizationBuilder(application.bufferedReaderWithQueueOfStreams, false)

        val address: Address = organizationBuilder.address!!
        organizationDatabase.removeAllByPostalAddress(address)

        return Result.success(null)
    }
}
