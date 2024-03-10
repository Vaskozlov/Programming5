package commands.client_side;

import commands.client_side.core.ServerSideCommand;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.Organization;
import OrganizationDatabase.OrganizationManagerInterface;

public class RemoveHeadCommand extends ServerSideCommand {

    public RemoveHeadCommand(ClientCallbackFunction clientCallbackFunction, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, organizationDatabase);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) {
        Organization removedOrganization = organizationDatabase.removeHead();

        callback.invoke(
                removedOrganization != null ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILURE,
                null,
                removedOrganization
        );
    }
}
