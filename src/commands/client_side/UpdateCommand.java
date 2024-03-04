package commands.client_side;

import OrganizationDatabase.Organization;
import OrganizationDatabase.OrganizationManagerInterface;
import application.Application;
import application.OrganizationBuilder;
import commands.client_side.core.ClientCallbackFunction;
import commands.client_side.core.ServerAndClientSideCommand;
import lib.ExecutionStatus;

public class UpdateCommand extends ServerAndClientSideCommand {

    public UpdateCommand(ClientCallbackFunction clientCallbackFunction, Application application, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, application, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) throws Exception {
        int id = Integer.parseInt(args[0]);
        Organization organizationToUpdate = OrganizationBuilder.constructOrganization(
                application.getBufferedReaderWithQueueOfStreams(),
                true
        );

        organizationToUpdate.setId(id);

        organizationDatabase.modifyOrganization(
                organizationToUpdate
        );

        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
