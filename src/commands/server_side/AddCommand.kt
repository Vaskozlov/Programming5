package commands.server_side;

import OrganizationDatabase.Organization;
import OrganizationDatabase.OrganizationManagerInterface;
import lib.ExecutionStatus;
import network.client.udp.User;

public class AddCommand extends ServerSideCommand {

    public AddCommand(ServerCallbackFunction callback) {
        super(callback);
    }

    @Override
    protected void executeImplementation(
            User user,
            OrganizationManagerInterface organizationManager,
            Object[] args,
            ServerCallbackFunction callback
    ) throws Exception {
        assert args.length == 1;
        assert args[0] instanceof Organization;

        Organization organization = (Organization) args[0];
        organizationManager.add(organization);
        callback.invoke(user, organizationManager, ExecutionStatus.SUCCESS, null);
    }
}
