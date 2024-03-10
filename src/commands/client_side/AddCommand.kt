package commands.client_side;

import application.Application;
import application.OrganizationBuilder;
import commands.client_side.core.ServerAndClientSideCommand;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.OrganizationManagerInterface;

public class AddCommand extends ServerAndClientSideCommand {

    public AddCommand(ClientCallbackFunction clientCallbackFunction, Application application, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, application, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) throws Exception {
        organizationDatabase.add(OrganizationBuilder.constructOrganization(application.getBufferedReaderWithQueueOfStreams(), false));
        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
