package commands.server_side;

import OrganizationDatabase.OrganizationManagerInterface;
import lib.ExecutionStatus;
import network.client.udp.User;

@FunctionalInterface
public interface ServerCallbackFunction {
    void invoke(
            User user,
            OrganizationManagerInterface organizationManager,
            ExecutionStatus status,
            Exception error,
            Object... args
    ) throws Exception;
}
