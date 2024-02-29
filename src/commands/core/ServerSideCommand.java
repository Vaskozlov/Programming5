package commands.core;

import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public abstract class ServerSideCommand extends Command {
    protected OrganizationManager organizationManager;

    protected ServerSideCommand(CallbackFunction callbackFunction, OrganizationManager organizationManager) {
        super(callbackFunction);
        this.organizationManager = organizationManager;
    }
}
