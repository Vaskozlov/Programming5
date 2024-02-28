package commands;

import client.Application;
import lib.ExecutionStatus;
import lib.FunctionWithVoidReturnAndOneArgument;

import java.util.List;

public class ExitCommand extends ClientSideCommand {
    public ExitCommand(Application application) {
        super(application);
    }

    @Override
    public void execute(List<String> args, FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback) {
        application.stop();
        callback.invoke(ExecutionStatus.SUCCESS);
    }
}
