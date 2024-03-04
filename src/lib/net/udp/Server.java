package lib.net.udp;

import OrganizationDatabase.NetworkCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lib.ExecutionStatus;
import network.client.udp.ResultFrame;
import network.client.udp.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public abstract class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private final int bufferSize = 2048;

    private final DatagramSocket socket;
    private final int port;
    private boolean running = false;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected Server(int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(this.port);
    }

    public static User getUserFromPacket(DatagramPacket packet) {
        return new User(packet.getAddress(), packet.getPort());
    }

    public void send(User user, ResultFrame frame) throws IOException {
        String json = objectMapper.writeValueAsString(frame);
        byte[] bytesToSend = json.getBytes();
        DatagramPacket packet = new DatagramPacket(bytesToSend, bytesToSend.length, user.address(), user.port());
        socket.send(packet);
    }

    public void send(User user, NetworkCode code, String str) throws IOException {
        send(user, new ResultFrame(code, str));
    }

    protected abstract void handlePacket(DatagramPacket packet) throws Exception;

    public void run() {
        running = true;
        logger.trace("Server is running");

        while (running) {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
                handlePacket(packet);
            } catch (Exception e) {
                logger.error("Error while receiving packet: {}", e);
            }
        }

        socket.close();
    }

    public void stop() {
        running = false;
    }
}
