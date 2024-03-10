package commands.server_side;

import OrganizationDatabase.OrganizationManagerInterface;
import lib.ExecutionStatus;
import network.client.udp.User;

public class RemoveByIdCommand extends ServerSideCommand {

    public RemoveByIdCommand(ServerCallbackFunction callback) {
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
        assert args[0] instanceof Integer;

        Integer id = (Integer) args[0];

        organizationManager.removeById(id);
        callback.invoke(
                user,
                organizationManager,
                ExecutionStatus.SUCCESS,
                null
        );
    }
}
