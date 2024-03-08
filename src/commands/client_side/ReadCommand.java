package commands.client_side;

import commands.client_side.core.ServerSideCommand;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.OrganizationManagerInterface;

public class ReadCommand extends ServerSideCommand {

    public ReadCommand(ClientCallbackFunction clientCallbackFunction, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) {
        String filename = args[0];
        callback.invoke(organizationDatabase.loadFromFile(filename), null, filename);
    }
}