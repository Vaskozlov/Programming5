package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public class ClearCommand extends ServerSideCommand {

    public ClearCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) {
        organizationManager.clear();
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
