package commands.client_side;

import OrganizationDatabase.Organization;
import OrganizationDatabase.OrganizationManagerInterface;
import commands.client_side.core.ServerSideCommand;
import exceptions.OrganizationNotFoundException;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;

public class MaxByFullNameCommand extends ServerSideCommand {

    public MaxByFullNameCommand(ClientCallbackFunction clientCallbackFunction, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) throws OrganizationNotFoundException {
        Organization maxOrganization = organizationDatabase.maxByFullName();

        if (maxOrganization != null) {
            callback.invoke(ExecutionStatus.SUCCESS, null, maxOrganization);
        } else {
            callback.invoke(ExecutionStatus.FAILURE, null);
        }
    }
}
