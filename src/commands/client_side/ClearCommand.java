package commands.client_side;

import commands.client_side.core.ServerSideCommand;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.OrganizationManagerInterface;

public class ClearCommand extends ServerSideCommand {

    public ClearCommand(ClientCallbackFunction clientCallbackFunction, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) {
        organizationDatabase.clear();
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
