package commands.client_side;

import commands.client_side.core.ServerSideCommand;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.OrganizationManagerInterface;

public class RemoveByIdCommand extends ServerSideCommand {

    public RemoveByIdCommand(ClientCallbackFunction clientCallbackFunction, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) throws Exception {
        int id = Integer.parseInt(args[0]);
        organizationDatabase.removeById(id);
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
