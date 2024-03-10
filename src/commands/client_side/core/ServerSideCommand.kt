package commands.client_side.core

import database.OrganizationManagerInterface

abstract class ServerSideCommand protected constructor(
    protected var organizationDatabase: OrganizationManagerInterface
) : Command()
