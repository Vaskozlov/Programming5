package commands.core;

import client.Application;
import lib.functions.CallbackFunction;
import organization.OrganizationManager;

public abstract class ServerAndClientSideCommand extends Command {
    protected Application application;
    protected OrganizationManager organizationManager;

    protected ServerAndClientSideCommand(CallbackFunction callbackFunction, Application application, OrganizationManager organizationManager) {
        super(callbackFunction);

        this.application = application;
        this.organizationManager = organizationManager;
    }
}
