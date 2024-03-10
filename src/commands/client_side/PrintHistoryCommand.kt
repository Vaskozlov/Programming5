package commands.client_side

import application.Application
import commands.client_side.core.ClientSideCommand
import lib.collections.CircledStorage

class PrintHistoryCommand(application: Application) :
    ClientSideCommand(application) {
    override fun executeImplementation(argument: Any?): Result<CircledStorage<String>?> {
        return Result.success(application.commandsHistory)
    }
}
