package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.Organization
import database.OrganizationManagerInterface
import exceptions.OrganizationNotFoundException

class RemoveHeadCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Organization> {
        assert(argument == null)

        val removedOrganization = organizationDatabase.removeHead()

        if (removedOrganization == null) {
            return Result.failure(OrganizationNotFoundException())
        }

        return Result.success(removedOrganization)
    }
}
