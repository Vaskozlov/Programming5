package commands;

import organization.OrganizationManager;

public abstract class ServerSideCommand implements Command {
    protected OrganizationManager organizationManager;

    protected ServerSideCommand(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }
}
