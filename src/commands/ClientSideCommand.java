package commands;

import client.Application;

public abstract class ClientSideCommand implements Command {
    protected Application application;

    protected ClientSideCommand(Application application) {
        this.application = application;
    }
}
