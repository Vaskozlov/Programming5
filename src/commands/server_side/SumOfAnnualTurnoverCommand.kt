package commands.server_side

import database.OrganizationManagerInterface
import network.client.udp.User

class SumOfAnnualTurnoverCommand : ServerSideCommand() {
    override suspend fun executeImplementation(
        user: User?,
        organizationManager: OrganizationManagerInterface,
        argument: Any?
    ): Result<Double> {
        assert(argument == null)
        return Result.success(organizationManager.sumOfAnnualTurnover)
    }
}
