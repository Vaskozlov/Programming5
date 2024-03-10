package commands.client_side

import application.Application
import commands.client_side.core.ClientSideCommand

class ExitCommand(application: Application) :
    ClientSideCommand(application) {
    override fun executeImplementation(argument: Any?): Result<Unit?> {
        application.stop()
        return Result.success(null)
    }
}
