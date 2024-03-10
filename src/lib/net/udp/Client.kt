package lib.net.udp;

import java.io.IOException;
import java.net.*;

public class Client {
    private static final int bufferSize = 2048;

    private DatagramSocket socket;
    private InetAddress address;
    private final int port;

    public Client(String address, int port) throws UnknownHostException, SocketException {
        this(InetAddress.getByName(address), port);
    }

    public Client(InetAddress address, int port) throws SocketException {
        this.address = address;
        this.port = port;
        this.socket = new DatagramSocket();
    }

    public String receive() throws IOException {
        byte[] buffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        return new String(buffer, 0, packet.getLength());
    }

    public void send(byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
    }
}
