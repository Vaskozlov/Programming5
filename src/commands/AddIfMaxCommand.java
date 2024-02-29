package commands;

import client.Application;
import client.OrganizationBuilder;
import commands.core.ServerAndClientSideCommand;
import exceptions.KeyboardInterruptException;
import exceptions.OrganizationAlreadyPresentedException;
import lib.ExecutionStatus;
import lib.Localization;
import lib.functions.CallbackFunction;
import lib.functions.FunctionWithVoidReturnAndOneArgument;
import organization.Organization;
import organization.OrganizationManager;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddIfMaxCommand extends ServerAndClientSideCommand {

    public AddIfMaxCommand(CallbackFunction callbackFunction, Application application, OrganizationManager organizationManager) {
        super(callbackFunction, application, organizationManager);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) throws Exception {
        Organization newOrganization = OrganizationBuilder.constructOrganization(application.getBufferedReaderWithQueueOfStreams(), false);
        Organization maxOrganization = Collections.max(organizationManager.getOrganizations(),
                Comparator.comparing(Organization::getFullName));

        if (maxOrganization.getFullName().compareTo(newOrganization.getFullName()) < 0) {
            organizationManager.add(newOrganization);
            callback.invoke(ExecutionStatus.SUCCESS, null);
        } else {
            callback.invoke(ExecutionStatus.FAILURE, null);
        }
    }
}
