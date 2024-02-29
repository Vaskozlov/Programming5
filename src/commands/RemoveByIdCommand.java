package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public class RemoveByIdCommand extends ServerSideCommand {

    public RemoveByIdCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) throws Exception {
        int id = Integer.parseInt(args[0]);
        organizationManager.removeOrganization(id);
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
