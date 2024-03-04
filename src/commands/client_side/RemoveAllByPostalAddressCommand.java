package commands.client_side;

import application.Application;
import commands.client_side.core.ServerAndClientSideCommand;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.Address;
import OrganizationDatabase.OrganizationManagerInterface;
import application.UserInteractiveOrganizationBuilder;

public class RemoveAllByPostalAddressCommand extends ServerAndClientSideCommand {
    public RemoveAllByPostalAddressCommand(ClientCallbackFunction clientCallbackFunction, Application application, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, application, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) throws Exception {
        var organizationBuilder = new UserInteractiveOrganizationBuilder(application.getBufferedReaderWithQueueOfStreams(), false);

        Address address = organizationBuilder.getAddress();
        organizationDatabase.removeAllByPostalAddress(address);

        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
