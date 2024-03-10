package commands.server_side;

import OrganizationDatabase.Address;
import OrganizationDatabase.OrganizationManagerInterface;
import lib.ExecutionStatus;
import network.client.udp.User;

public class RemoveAllByPostalAddressCommand extends ServerSideCommand {

    public RemoveAllByPostalAddressCommand(ServerCallbackFunction callback) {
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
        assert args[0] instanceof Address;

        Address address = (Address) args[0];

        organizationManager.removeAllByPostalAddress(address);
        callback.invoke(
                user,
                organizationManager,
                ExecutionStatus.SUCCESS,
                null
        );
    }
}
