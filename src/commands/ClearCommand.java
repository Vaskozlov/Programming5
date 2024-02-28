package commands;

import lib.ExecutionStatus;
import lib.FunctionWithVoidReturnAndOneArgument;
import organization.OrganizationManager;

import java.util.List;

public class ClearCommand extends ServerSideCommand {

    public ClearCommand(OrganizationManager organizationManager) {
        super(organizationManager);
    }

    @Override
    public void execute(List<String> args, FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback) {
        System.out.println(organizationManager.getInfo());
        callback.invoke(ExecutionStatus.SUCCESS);
    }
}
