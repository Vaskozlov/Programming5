package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public class SaveCommand extends ServerSideCommand {

    public SaveCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) {
        String filename = args[0];
        boolean isSuccessfullySaved = organizationManager.saveToFile(filename);
        callback.invoke(isSuccessfullySaved ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILURE, null, filename);
    }
}
