package commands.client_side

import commands.client_side.core.ServerSideCommand
import database.OrganizationManagerInterface

class SumOfAnnualTurnoverCommand(
    organizationDatabase: OrganizationManagerInterface
) : ServerSideCommand(organizationDatabase) {
    override fun executeImplementation(argument: Any?): Result<Float> {
        assert(argument == null)

        return Result.success(organizationDatabase.sumOfAnnualTurnover)
    }
}
