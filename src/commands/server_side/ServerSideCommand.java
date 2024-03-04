package commands.server_side;

import OrganizationDatabase.OrganizationManagerInterface;
import lib.ExecutionStatus;
import network.client.udp.User;

public abstract class ServerSideCommand {
    private final ServerCallbackFunction clientCallbackFunction;

    protected ServerSideCommand(ServerCallbackFunction callback) {
        this.clientCallbackFunction = callback;
    }

    public void execute(
            User user,
            OrganizationManagerInterface organizationManager
    ) {
        execute(user, organizationManager, new Object[]{}, clientCallbackFunction);
    }

    public void execute(
            User user,
            OrganizationManagerInterface organizationManager,
            Object[] args
    ) {
        execute(user, organizationManager, args, clientCallbackFunction);
    }

    public void execute(
            User user,
            OrganizationManagerInterface organizationManager,
            ServerCallbackFunction callback
    ) {
        execute(user, organizationManager, new String[]{}, callback);
    }

    public void execute(
            User user,
            OrganizationManagerInterface organizationManager,
            Object[] args,
            ServerCallbackFunction callback
    ) {
        try {
            executeImplementation(user, organizationManager, args, callback);
        } catch (Exception e) {
            try {
                callback.invoke(user, organizationManager, ExecutionStatus.FAILURE, e);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    protected abstract void executeImplementation(
            User user,
            OrganizationManagerInterface organizationManager,
            Object[] args,
            ServerCallbackFunction callback
    ) throws Exception;
}
