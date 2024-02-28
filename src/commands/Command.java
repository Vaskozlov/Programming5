package commands;

import lib.ExecutionStatus;
import lib.FunctionWithVoidReturnAndOneArgument;

import java.util.Collections;
import java.util.List;

public interface Command {
    default void execute() {
        execute(Collections.emptyList(), (ExecutionStatus status) -> {
        });
    }

    default void execute(FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback) {
        execute(Collections.emptyList(), callback);
    }

    void execute(List<String> args, FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback);
}
