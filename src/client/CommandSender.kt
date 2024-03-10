package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lib.net.udp.Client;
import network.client.DatabaseCommand;
import network.client.udp.Frame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class CommandSender extends Client {
    ObjectMapper objectMapper = new ObjectMapper();
    boolean connected = false;

    public CommandSender(String address, int port) throws SocketException, UnknownHostException {
        this(InetAddress.getByName(address), port);
    }

    public CommandSender(InetAddress address, int port) throws SocketException, UnknownHostException {
        super(address, port);
        objectMapper.findAndRegisterModules();
    }

    public void sendCommand(DatabaseCommand command, Object object) throws IOException {
        connect();

        Frame frame = new Frame(command, object);
        String json = objectMapper.writeValueAsString(frame);
        send(json.getBytes());
    }

    private void connect() throws IOException {
        record ConnectionFrame(String command) {
        }

        if (!connected) {
            ConnectionFrame frame = new ConnectionFrame("Connect");
            String json = objectMapper.writeValueAsString(frame);
            send(json.getBytes());
        }

        connected = true;
    }

    public static void main(String[] args) throws IOException {
        CommandSender sender = new CommandSender(InetAddress.getLocalHost(), 6789);
        sender.sendCommand(DatabaseCommand.SHOW, null);
        System.out.println(sender.receive());

        sender.sendCommand(DatabaseCommand.CLEAR, null);
        System.out.println(sender.receive());

        sender.sendCommand(DatabaseCommand.SHOW, null);
        System.out.println(sender.receive());
    }
}
