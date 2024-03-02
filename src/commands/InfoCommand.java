package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public class InfoCommand extends ServerSideCommand {
    public InfoCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) {
        callback.invoke(ExecutionStatus.SUCCESS, null, organizationManager.getInfo());
    }
}
