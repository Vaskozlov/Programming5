package commands;

import client.Application;
import commands.core.ClientSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;

public class ExitCommand extends ClientSideCommand {
    public ExitCommand(CallbackFunction callbackFunction, Application application) {
        super(callbackFunction, application);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) {
        application.stop();
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
