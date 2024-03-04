package commands.client_side;

import application.Application;
import commands.client_side.core.ClientSideCommand;
import lib.ExecutionStatus;
import lib.Localization;
import commands.client_side.core.ClientCallbackFunction;

public class HelpCommand extends ClientSideCommand {
    public HelpCommand(ClientCallbackFunction clientCallbackFunction, Application application) {
        super(clientCallbackFunction, application);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) {
        callback.invoke(ExecutionStatus.SUCCESS, null, Localization.get("message.help"));
    }
}
