package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import lib.functions.FunctionWithVoidReturnAndOneArgument;
import organization.OrganizationManager;

import java.util.List;

public class ClearCommand extends ServerSideCommand {

    public ClearCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) {
        organizationManager.clear();
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
