package commands.client_side

import application.Application
import application.OrganizationBuilder
import commands.client_side.core.ServerAndClientSideCommand
import database.OrganizationManagerInterface

class UpdateCommand(
    application: Application,
    organizationDatabase: OrganizationManagerInterface
) : ServerAndClientSideCommand(application, organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        val organizationToUpdate = OrganizationBuilder.constructOrganization(
            application.bufferedReaderWithQueueOfStreams,
            true
        )

        organizationToUpdate.id = argument as Int
        organizationDatabase.modifyOrganization(organizationToUpdate)

        return Result.success(null)
    }
}
