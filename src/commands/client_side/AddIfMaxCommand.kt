package commands.client_side

import application.Application
import application.OrganizationBuilder
import commands.client_side.core.ServerAndClientSideCommand
import database.OrganizationManagerInterface
import exceptions.NotMaximumOrganizationException
import lib.ExecutionStatus

class AddIfMaxCommand(
    application: Application,
    organizationDatabase: OrganizationManagerInterface
) : ServerAndClientSideCommand(application, organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        val newOrganization = OrganizationBuilder.constructOrganization(
            application.bufferedReaderWithQueueOfStreams,
            false
        )

        if (organizationDatabase.addIfMax(newOrganization) == ExecutionStatus.FAILURE) {
            return Result.failure(NotMaximumOrganizationException());
        }

        return Result.success(null)
    }
}
