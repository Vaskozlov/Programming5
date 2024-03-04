package commands.server_side;

import OrganizationDatabase.OrganizationManagerInterface;
import lib.ExecutionStatus;
import network.client.udp.User;

public class SumOfAnnualTurnoverCommand extends ServerSideCommand {

    public SumOfAnnualTurnoverCommand(ServerCallbackFunction callback) {
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

        callback.invoke(
                user,
                organizationManager,
                ExecutionStatus.SUCCESS,
                null,
                organizationManager.getSumOfAnnualTurnover()
        );
    }
}
