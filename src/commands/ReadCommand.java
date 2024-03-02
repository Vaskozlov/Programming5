package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public class ReadCommand extends ServerSideCommand {

    public ReadCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) {
        String filename = args[0];
        boolean isSuccessfullyLoaded = organizationManager.loadFromFile(filename);
        callback.invoke(isSuccessfullyLoaded ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILURE, null, filename);
    }
}
