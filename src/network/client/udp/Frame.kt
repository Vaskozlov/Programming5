package network.client.udp

import network.client.DatabaseCommand

data class Frame(var command: DatabaseCommand, var value: Any?)
