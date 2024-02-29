package lib.functions;

import lib.ExecutionStatus;

@FunctionalInterface
public interface CallbackFunction {
    void invoke(ExecutionStatus status, Exception error, Object... args);
}
