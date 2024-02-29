package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.Organization;
import organization.OrganizationManager;

import java.util.List;

public class RemoveHeadCommand extends ServerSideCommand {

    public RemoveHeadCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) {
        Organization removedOrganization = organizationManager.removeHead();

        callback.invoke(
                removedOrganization != null ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILURE,
                null,
                removedOrganization
        );
    }
}
