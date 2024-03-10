package commands.client_side.core;

import lib.ExecutionStatus;

public abstract class Command {
    private final ClientCallbackFunction clientCallbackFunction;

    protected Command(ClientCallbackFunction callback) {
        this.clientCallbackFunction = callback;
    }

    public void execute() {
        execute(new String[]{}, clientCallbackFunction);
    }

    public void execute(String[] args) {
        execute(args, clientCallbackFunction);
    }

    public void execute(ClientCallbackFunction callback) {
        execute(new String[]{}, callback);
    }

    public void execute(String[] args, ClientCallbackFunction callback) {
        try {
            executeImplementation(args, callback);
        } catch (Exception e) {
            callback.invoke(ExecutionStatus.FAILURE, e);
        }
    }

    protected abstract void executeImplementation(String[] args, ClientCallbackFunction callback) throws Exception;
}
