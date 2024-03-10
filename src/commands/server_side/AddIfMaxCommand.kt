package commands.server_side;

import OrganizationDatabase.Organization;
import OrganizationDatabase.OrganizationManagerInterface;
import network.client.udp.User;

public class AddIfMaxCommand extends ServerSideCommand {

    public AddIfMaxCommand(ServerCallbackFunction callback) {
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

        // TODO: in case of failure add error to the arguments
        callback.invoke(
                user,
                organizationManager,
                organizationManager.addIfMax(organization),
                null
        );
    }
}
