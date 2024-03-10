package commands.client_side.core;

import application.Application;

public abstract class ClientSideCommand extends Command {
    protected Application application;

    protected ClientSideCommand(ClientCallbackFunction clientCallbackFunction, Application application) {
        super(clientCallbackFunction);
        this.application = application;
    }
}
