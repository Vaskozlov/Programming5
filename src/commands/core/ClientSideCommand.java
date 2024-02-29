package commands.core;

import client.Application;
import lib.functions.CallbackFunction;

public abstract class ClientSideCommand extends Command {
    protected Application application;

    protected ClientSideCommand(CallbackFunction callbackFunction, Application application) {
        super(callbackFunction);
        this.application = application;
    }
}
