package commands;

import lib.ExecutionStatus;
import lib.FunctionWithVoidReturnAndOneArgument;
import organization.OrganizationManager;

import java.util.List;

public class InfoCommand extends ServerSideCommand {

    public InfoCommand(OrganizationManager organizationManager) {
        super(organizationManager);
    }

    @Override
    public void execute(List<String> args, FunctionWithVoidReturnAndOneArgument<ExecutionStatus> callback) {
        System.out.println(organizationManager.getInfo());
        callback.invoke(ExecutionStatus.SUCCESS);
    }
}
