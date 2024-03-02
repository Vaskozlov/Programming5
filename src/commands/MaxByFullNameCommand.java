package commands;

import commands.core.ServerSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;
import organization.Organization;
import organization.OrganizationManager;

import java.util.Collections;
import java.util.Comparator;

public class MaxByFullNameCommand extends ServerSideCommand {

    public MaxByFullNameCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction, organizationManager);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) {
        Organization maxOrganization = Collections.max(organizationManager.getOrganizations(),
                Comparator.comparing(Organization::getFullName));

        if (maxOrganization != null) {
            callback.invoke(ExecutionStatus.SUCCESS, null, maxOrganization);
        } else {
            callback.invoke(ExecutionStatus.FAILURE, null, (Organization) null);
        }
    }
}
