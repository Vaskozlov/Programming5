package commands;

import client.Application;
import lib.ExecutionStatus;
import lib.FunctionWithVoidReturnAndOneArgument;
import lib.Localization;
import java.util.List;

public class HelpCommand extends ClientSideCommand {
    public HelpCommand(Application application) {
        super(application);
    }

    @Override
    public void execute(List<String> args, FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback) {
        System.out.printf(Localization.get("message.help"));
        callback.invoke(ExecutionStatus.SUCCESS);
    }
}
