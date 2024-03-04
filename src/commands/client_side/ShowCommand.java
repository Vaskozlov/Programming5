package commands.client_side;

import commands.client_side.core.ServerSideCommand;
import exceptions.InvalidOutputFormatException;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;
import OrganizationDatabase.OrganizationManagerInterface;

public class ShowCommand extends ServerSideCommand {

    public ShowCommand(ClientCallbackFunction clientCallbackFunction, OrganizationManagerInterface organizationDatabase) {
        super(clientCallbackFunction, organizationDatabase);
    }

    @Override
    public void executeImplementation(String[] args, ClientCallbackFunction callback) throws Exception {
        String mode = args.length == 0 ? "yaml" : args[0].toLowerCase();
        String result = switch (mode) {
            case "json" -> organizationDatabase.toJson();
            case "csv" -> organizationDatabase.toCSV();
            case "yaml" -> organizationDatabase.toYaml();
            default -> throw new InvalidOutputFormatException("Invalid output format");
        };

        callback.invoke(result == null ? ExecutionStatus.FAILURE : ExecutionStatus.SUCCESS, null, result);
    }
}
