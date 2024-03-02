package commands;

import client.Application;
import client.OrganizationBuilder;
import commands.core.ServerAndClientSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.Organization;
import organization.OrganizationManager;

import java.util.Collections;
import java.util.Comparator;

public class AddIfMaxCommand extends ServerAndClientSideCommand {

    public AddIfMaxCommand(CallbackFunction callbackFunction, Application application, OrganizationManager organizationManager) {
        super(callbackFunction, application, organizationManager);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) throws Exception {
        Organization newOrganization = OrganizationBuilder.constructOrganization(application.getBufferedReaderWithQueueOfStreams(), false);
        Organization maxOrganization = Collections.max(organizationManager.getOrganizations(),
                Comparator.comparing(Organization::getFullName));
        ExecutionStatus executionStatus = ExecutionStatus.FAILURE;

        if (maxOrganization.getFullName().compareTo(newOrganization.getFullName()) < 0) {
            organizationManager.add(newOrganization);
            executionStatus = ExecutionStatus.SUCCESS;
        }

        callback.invoke(executionStatus, null);
    }
}
