package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.Localization;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

import java.util.List;

public class SumOfAnnualTurnoverCommand extends ServerSideCommand {
    public SumOfAnnualTurnoverCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
protected void executeThrowableCommand(String[] args, CallbackFunction callback) {
        callback.invoke(ExecutionStatus.SUCCESS, null, organizationManager.getSumOfAnnualTurnover());
    }
}
