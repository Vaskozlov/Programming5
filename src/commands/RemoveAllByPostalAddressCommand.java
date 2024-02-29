package commands;

import client.Application;
import commands.core.ServerAndClientSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.Address;
import organization.OrganizationManager;
import organization.UserInteractiveOrganizationBuilder;

public class RemoveAllByPostalAddressCommand extends ServerAndClientSideCommand {
    public RemoveAllByPostalAddressCommand(CallbackFunction callbackFunction, Application application, OrganizationManager organizationManager) {
        super(callbackFunction, application, organizationManager);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) throws Exception {
        var organizationBuilder = new UserInteractiveOrganizationBuilder(application.getBufferedReaderWithQueueOfStreams(), false);

        Address address = organizationBuilder.getAddress();
        organizationManager.removeAllByPostalAddress(address);

        callback.invoke(ExecutionStatus.SUCCESS, null);
    }
}
