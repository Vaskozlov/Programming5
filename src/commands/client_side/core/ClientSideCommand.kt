package commands.client_side.core

import application.Application

abstract class ClientSideCommand protected constructor(
    protected var application: Application
) : Command()
