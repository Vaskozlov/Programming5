package commands;

import client.Application;
import commands.core.ClientSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;

public class PrintHistoryCommand extends ClientSideCommand {
    public PrintHistoryCommand(CallbackFunction callbackFunction, Application application) {
        super(callbackFunction, application);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) {
        callback.invoke(ExecutionStatus.SUCCESS, null, application.getCommandsHistory());
    }
}
