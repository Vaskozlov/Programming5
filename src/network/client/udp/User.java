package network.client.udp;

import java.net.InetAddress;

public record User(InetAddress address, int port) {
}
