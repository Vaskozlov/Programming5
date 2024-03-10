package commands.client_side.core

import database.OrganizationManagerInterface
import application.Application

abstract class ServerAndClientSideCommand protected constructor(
    protected var application: Application,
    protected var organizationDatabase: OrganizationManagerInterface
) : Command()
