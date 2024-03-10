package network.client.udp

import network.client.DatabaseCommand
import server.AuthorizationHeader

data class Frame(val authorization: AuthorizationHeader, val command: DatabaseCommand, val value: Any?)
