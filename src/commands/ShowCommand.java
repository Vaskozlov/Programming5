package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

import java.io.IOException;

public class ShowCommand extends ServerSideCommand {

    public ShowCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    public void executeImplementation(String[] args, CallbackFunction callback) throws IOException {
        String mode = args.length == 0 ? "yaml" : args[0].toLowerCase();
        String result = switch (mode) {
            case "json" -> organizationManager.toJson();
            case "csv" -> organizationManager.toCSV();
            case "yaml" -> organizationManager.toYaml();
            default -> null;
        };

        callback.invoke(result == null ? ExecutionStatus.FAILURE : ExecutionStatus.SUCCESS, null, result);
    }
}
