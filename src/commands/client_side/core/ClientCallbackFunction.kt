package commands.client_side.core;

import lib.ExecutionStatus;

@FunctionalInterface
public interface ClientCallbackFunction {
    void invoke(ExecutionStatus status, Exception error, Object... args);
}
