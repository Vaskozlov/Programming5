package commands;

import client.Application;
import client.OrganizationBuilder;
import commands.core.ServerAndClientSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public class UpdateCommand extends ServerAndClientSideCommand {

    public UpdateCommand(CallbackFunction callbackFunction, Application application, OrganizationManager organizationManager) {
        super(callbackFunction, application, organizationManager);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) throws Exception {
        int id = Integer.parseInt(args[0]);
        organizationManager.modifyOrganization(
                id,
                OrganizationBuilder.constructOrganization(
                        application.getBufferedReaderWithQueueOfStreams(),
                        true
                )
        );

        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
