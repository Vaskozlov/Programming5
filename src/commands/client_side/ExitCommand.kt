package commands.client_side;

import application.Application;
import commands.client_side.core.ClientSideCommand;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;

public class ExitCommand extends ClientSideCommand {
    public ExitCommand(ClientCallbackFunction clientCallbackFunction, Application application) {
        super(clientCallbackFunction, application);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) {
        application.stop();
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
