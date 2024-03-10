package commands.client_side.core;

import application.Application;
import OrganizationDatabase.OrganizationManagerInterface;

public abstract class ServerAndClientSideCommand extends Command {
    protected Application application;
    protected OrganizationManagerInterface organizationDatabase;

    protected ServerAndClientSideCommand(ClientCallbackFunction clientCallbackFunction, Application application, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction);

        this.application = application;
        this.organizationDatabase = organizationDatabase;
    }
}
