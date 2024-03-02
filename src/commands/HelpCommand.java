package commands;

import client.Application;
import commands.core.ClientSideCommand;
import lib.ExecutionStatus;
import lib.Localization;
import lib.functions.CallbackFunction;

public class HelpCommand extends ClientSideCommand {
    public HelpCommand(CallbackFunction callbackFunction, Application application) {
        super(callbackFunction, application);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) {
        callback.invoke(ExecutionStatus.SUCCESS, null, Localization.get("message.help"));
    }
}
