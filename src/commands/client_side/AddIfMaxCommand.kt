package commands.client_side;

import application.Application;
import application.OrganizationBuilder;
import commands.client_side.core.ServerAndClientSideCommand;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.Organization;
import OrganizationDatabase.OrganizationManagerInterface;

public class AddIfMaxCommand extends ServerAndClientSideCommand {

    public AddIfMaxCommand(ClientCallbackFunction clientCallbackFunction, Application application, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, application, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) throws Exception {
        Organization newOrganization = OrganizationBuilder.constructOrganization(
                application.getBufferedReaderWithQueueOfStreams(),
                false
        );

        callback.invoke(organizationDatabase.addIfMax(newOrganization), null);
    }
}
