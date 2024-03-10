package commands.client_side

import application.Application
import application.OrganizationBuilder
import commands.client_side.core.ServerAndClientSideCommand
import database.OrganizationManagerInterface

class AddCommand(
    application: Application,
    organizationDatabase: OrganizationManagerInterface
) : ServerAndClientSideCommand(application, organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        organizationDatabase.add(
            OrganizationBuilder.constructOrganization(
                application.bufferedReaderWithQueueOfStreams,
                false
            )
        )

        return Result.success(null);
    }
}
