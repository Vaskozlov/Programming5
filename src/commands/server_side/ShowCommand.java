package commands.server_side;

import OrganizationDatabase.OrganizationManagerInterface;
import lib.ExecutionStatus;
import network.client.udp.User;

public class ShowCommand extends ServerSideCommand {

    public ShowCommand(ServerCallbackFunction callback) {
        super(callback);
    }

    @Override
    protected void executeImplementation(
            User user,
            OrganizationManagerInterface organizationManager,
            Object[] args,
            ServerCallbackFunction callback
    ) throws Exception {
        assert args.length == 0;

        // TODO: add argument to the show command
        callback.invoke(
                user,
                organizationManager,
                ExecutionStatus.SUCCESS,
                null,
                organizationManager.toYaml()
        );
    }
}
