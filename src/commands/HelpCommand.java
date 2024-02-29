package commands;

import client.Application;
import commands.core.ClientSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import lib.Localization;

public class HelpCommand extends ClientSideCommand {
    public HelpCommand(CallbackFunction callbackFunction, Application application) {
        super(callbackFunction, application);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) {
        callback.invoke(ExecutionStatus.SUCCESS, null, Localization.get("message.help"));
    }
}
