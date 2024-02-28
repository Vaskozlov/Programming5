package commands;

import lib.ExecutionStatus;
import lib.FunctionWithVoidReturnAndOneArgument;
import lib.Localization;

import java.util.List;

public class AddToHistoryCommand implements Command {
    @Override
    public void execute(List<String> args, FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback) {
        System.out.printf(Localization.get("message.help"));
        callback.invoke(ExecutionStatus.SUCCESS);
    }
}
