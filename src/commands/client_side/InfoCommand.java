package commands.client_side;

import commands.client_side.core.ServerSideCommand;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.OrganizationManagerInterface;

public class InfoCommand extends ServerSideCommand {
    public InfoCommand(ClientCallbackFunction clientCallbackFunction, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) {
        callback.invoke(ExecutionStatus.SUCCESS, null, organizationDatabase.getInfo());
    }
}
