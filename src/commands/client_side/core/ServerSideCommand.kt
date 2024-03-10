package commands.client_side.core;

import OrganizationDatabase.OrganizationManagerInterface;

public abstract class ServerSideCommand extends Command {
    protected OrganizationManagerInterface organizationDatabase;

    protected ServerSideCommand(ClientCallbackFunction clientCallbackFunction, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction);
        this.organizationDatabase = organizationDatabase;
    }
}
