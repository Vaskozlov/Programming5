package commands.core;

import lib.ExecutionStatus;
import lib.functions.CallbackFunction;

public abstract class Command {
    private CallbackFunction callbackFunction;

    protected Command(CallbackFunction callback) {
        this.callbackFunction = callback;
    }

    public void execute() {
        execute(new String[]{}, callbackFunction);
    }

    public void execute(String[] args) {
        execute(args, callbackFunction);
    }

    public void execute(CallbackFunction callback) {
        execute(new String[]{}, callback);
    }

    public void execute(String[] args, CallbackFunction callback) {
        try {
            executeThrowableCommand(args, callback);
        } catch (Exception e) {
            callback.invoke(ExecutionStatus.FAILURE, e);
        }
    }

    protected abstract void executeThrowableCommand(String[] args, CallbackFunction callback) throws Exception;
}
