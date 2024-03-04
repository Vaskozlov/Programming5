package commands.server_side;

import OrganizationDatabase.OrganizationManagerInterface;
import network.client.udp.User;

public class SaveCommand extends ServerSideCommand {

    public SaveCommand(ServerCallbackFunction callback) {
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
        assert args[0] instanceof String;

        String filename = (String) args[0];
        callback.invoke(user, organizationManager, organizationManager.saveToFile(filename), null);
    }
}
