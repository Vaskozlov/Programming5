package commands.client_side

import commands.client_side.core.Command
import lib.Localization

class HelpCommand : Command() {
    override suspend fun executeImplementation(argument: Any?): Result<String> {
        assert(argument == null)

        return Result.success(Localization.get("message.help"))
    }
}
