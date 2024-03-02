package commands;

import client.Application;
import client.OrganizationBuilder;
import commands.core.ServerAndClientSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public class AddCommand extends ServerAndClientSideCommand {

    public AddCommand(CallbackFunction callbackFunction, Application application, OrganizationManager organizationManager) {
        super(callbackFunction, application, organizationManager);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) throws Exception {
        organizationManager.add(OrganizationBuilder.constructOrganization(application.getBufferedReaderWithQueueOfStreams(), false));
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
