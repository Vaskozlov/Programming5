package commands.server_side

import database.Organization
import database.OrganizationManagerInterface
import exceptions.OrganizationNotFoundException
import network.client.udp.User

class MaxByFullNameCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Organization?> {
        assert(argument == null)
        val maxOrganization = organizationManager.maxByFullName()

        if (maxOrganization == null) {
            return Result.failure(OrganizationNotFoundException())
        }

        return Result.success(maxOrganization)
    }
}
