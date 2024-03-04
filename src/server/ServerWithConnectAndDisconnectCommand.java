package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import network.client.udp.ConnectionStatus;
import network.client.udp.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.net.SocketException;

public abstract class ServerWithConnectAndDisconnectCommand extends ServerWithCommands {
    private static final Logger logger = LogManager.getLogger(ServerWithConnectAndDisconnectCommand.class);

    protected ServerWithConnectAndDisconnectCommand(int port, String commandFieldName) throws SocketException {
        super(port, commandFieldName);
    }

    protected abstract void handleConnectCommand(User user);

    protected abstract void handleDisconnectCommand(User user);

    protected ConnectionStatus handleConnectAndDisconnectCommands(DatagramPacket packet) throws JsonProcessingException {
        String command = getCommandFromJson(packet);
        User user = getUserFromPacket(packet);

        switch (command) {
            case "Connect":
                handleConnectCommand(user);
                logger.trace("User {} connected", user);
                return ConnectionStatus.JUST_CONNECTED;

            case "Disconnect":
                handleDisconnectCommand(user);
                logger.trace("User {} disconnected", user);
                return ConnectionStatus.DISCONNECTED;

            default:
                return ConnectionStatus.CONNECTED;
        }
    }
}
