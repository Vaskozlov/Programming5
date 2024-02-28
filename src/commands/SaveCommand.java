package commands;

import lib.ExecutionStatus;
import lib.FunctionWithVoidReturnAndOneArgument;
import organization.OrganizationManager;

import java.util.List;

public class SaveCommand extends ServerSideCommand {

    public SaveCommand(OrganizationManager organizationManager) {
        super(organizationManager);
    }

    @Override
    public void execute(List<String> args, FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback) {
        String filename = args.get(0);
        boolean isSuccessfullySaved = organizationManager.saveToFile(filename);
        callback.invoke(isSuccessfullySaved ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILURE);
    }
}
