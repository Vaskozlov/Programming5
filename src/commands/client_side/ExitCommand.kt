package commands.client_side

import application.Application
import commands.client_side.core.ClientSideCommand

class ExitCommand(application: Application) :
    ClientSideCommand(application) {
    override suspend fun executeImplementation(argument: Any?): Result<Unit?> {
        assert(argument == null)

        application.stop()
        return Result.success(null)
    }
}
