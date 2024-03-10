package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.Organization
import database.OrganizationManagerInterface
import exceptions.OrganizationNotFoundException

class MaxByFullNameCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Organization> {
        val maxOrganization = organizationDatabase.maxByFullName()

        if (maxOrganization == null) {
            return Result.failure(OrganizationNotFoundException())
        }

        return Result.success(maxOrganization)
    }
}
