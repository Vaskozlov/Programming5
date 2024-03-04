package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lib.net.udp.Server;

import java.net.DatagramPacket;
import java.net.SocketException;

public abstract class ServerWithCommands extends Server {
    private final String commandFieldName;

    protected ServerWithCommands(int port, String commandFieldName) throws SocketException {
        super(port);
        objectMapper.findAndRegisterModules();

        this.commandFieldName = commandFieldName;
    }

    protected String getCommandFromJson(DatagramPacket packet) throws JsonProcessingException {
        String result = new String(packet.getData(), 0, packet.getLength());
        JsonNode jsonNodeRoot = objectMapper.readTree(result);
        JsonNode commandNode = jsonNodeRoot.get(commandFieldName);

        return commandNode.asText();
    }
}
