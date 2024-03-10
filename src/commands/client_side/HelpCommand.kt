package commands.client_side

import application.Application
import commands.client_side.core.ClientCallbackFunction
import commands.client_side.core.ClientSideCommand
import lib.ExecutionStatus
import lib.Localization

class HelpCommand(application: Application) :
    ClientSideCommand( application) {
    override fun executeImplementation(argument: Any?): Result<String> {
        return Result.success(Localization.get("message.help"))
    }
}
