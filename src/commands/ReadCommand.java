package commands;

import lib.ExecutionStatus;
import lib.FunctionWithVoidReturnAndOneArgument;
import organization.OrganizationManager;

import java.util.List;

public class ReadCommand extends ServerSideCommand {

    public ReadCommand(OrganizationManager organizationManager) {
        super(organizationManager);
    }

    @Override
    public void execute(List<String> args, FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback) {
        String filename = args.get(0);
        boolean isSuccessfullyLoaded = organizationManager.loadFromFile(filename);
        callback.invoke(isSuccessfullyLoaded ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILURE);
    }
}
