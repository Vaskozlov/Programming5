package server;

import OrganizationDatabase.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import commands.server_side.*;
import lib.ExecutionStatus;
import network.client.DatabaseCommand;
import network.client.udp.ConnectionStatus;
import network.client.udp.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class DatabaseCommandsReceiver extends ServerWithConnectAndDisconnectCommand {
    private static final Logger logger = LogManager.getLogger(DatabaseCommandsReceiver.class);
    Map<User, OrganizationDatabase> usersDatabases = new HashMap<>();
    Map<DatabaseCommand, ServerSideCommand> commandMap = new EnumMap<>(DatabaseCommand.class);

    public DatabaseCommandsReceiver(int port) throws SocketException {
        super(port, "command");
        objectMapper.findAndRegisterModules();

        commandMap.put(DatabaseCommand.ADD, new AddCommand(this::statusOnlyCallback));
        commandMap.put(DatabaseCommand.ADD_IF_MAX, new AddIfMaxCommand(this::addIfMaxCommandCallback));
        commandMap.put(DatabaseCommand.SHOW, new ShowCommand(this::defaultDataSendCallback));
        commandMap.put(DatabaseCommand.CLEAR, new ClearCommand(this::statusOnlyCallback));
        commandMap.put(DatabaseCommand.INFO, new InfoCommand(this::defaultDataSendCallback));
        commandMap.put(DatabaseCommand.MAX_BY_FULL_NAME, new MaxByFullNameCommand(this::defaultDataSendCallback));
        commandMap.put(DatabaseCommand.REMOVE_HEAD, new RemoveHeadCommand(this::defaultDataSendCallback));
        commandMap.put(DatabaseCommand.REMOVE_BY_ID, new RemoveByIdCommand(this::statusOnlyCallback));
        commandMap.put(DatabaseCommand.SAVE, new SaveCommand(this::statusOnlyCallback));
        commandMap.put(DatabaseCommand.READ, new ReadCommand(this::statusOnlyCallback));
        commandMap.put(DatabaseCommand.REMOVE_ALL_BY_POSTAL_ADDRESS, new RemoveAllByPostalAddressCommand(this::statusOnlyCallback));
        commandMap.put(DatabaseCommand.UPDATE, new UpdateCommand(this::statusOnlyCallback));
    }

    private void addIfMaxCommandCallback(
            User user,
            OrganizationManagerInterface organizationManager,
            ExecutionStatus status,
            Exception error,
            Object... args
    ) throws IOException {
        assert args.length == 0;

        if (status == ExecutionStatus.FAILURE) {
            send(user, NetworkCode.NOT_A_MAXIMUM_ORGANIZATION, null);
        } else {
            send(user, NetworkCode.SUCCESS, null);
        }
    }

    private void defaultDataSendCallback(
            User user,
            OrganizationManagerInterface organizationManager,
            ExecutionStatus status,
            Exception error,
            Object... args
    ) throws IOException {
        assert (args.length == 1 && args[0] instanceof String) || status == ExecutionStatus.FAILURE;

        if (status == ExecutionStatus.FAILURE) {
            send(user, NetworkCode.FAILURE, null);
        } else {
            send(user, NetworkCode.SUCCESS, (String) args[0]);
        }
    }

    private void statusOnlyCallback(
            User user,
            OrganizationManagerInterface organizationManager,
            ExecutionStatus status,
            Exception error,
            Object... args
    ) throws IOException {
        assert args.length == 0;

        if (status == ExecutionStatus.FAILURE) {
            send(user, NetworkCode.FAILURE, null);
        } else {
            send(user, NetworkCode.SUCCESS, null);
        }
    }

    @Override
    protected void handlePacket(DatagramPacket packet) throws JsonProcessingException {
        ConnectionStatus status = handleConnectAndDisconnectCommands(packet);

        if (status != ConnectionStatus.CONNECTED) {
            return;
        }

        DatabaseCommand command = DatabaseCommand.valueOf(getCommandFromJson(packet));
        User user = getUserFromPacket(packet);

        logger.trace("Received command {}, from {}", command, user);

        commandMap.get(command).execute(
                getUserFromPacket(packet),
                usersDatabases.get(getUserFromPacket(packet))
        );

        Object[] arguments = switch (command) {
            case SHOW, INFO, CLEAR, MAX_BY_FULL_NAME, HISTORY, SUM_OF_ANNUAL_TURNOVER, REMOVE_HEAD -> new Object[]{};

            case ADD, ADD_IF_MAX, UPDATE -> new Object[]{getOrganization(getObjectNode(packet))};

            case REMOVE_BY_ID -> new Object[]{getInt(getObjectNode(packet))};

            case SAVE, READ -> new Object[]{getString(getObjectNode(packet))};

            case REMOVE_ALL_BY_POSTAL_ADDRESS -> new Object[]{getAddress(getObjectNode(packet))};

            default -> null; // can be join with the first case, but it might be used to treat null arguments as error
        };

        commandMap.get(command).execute(
                user,
                usersDatabases.get(getUserFromPacket(packet)),
                arguments
        );
    }

    @Override
    protected void handleConnectCommand(User user) {
        OrganizationDatabase org = new OrganizationDatabase();
        org.loadFromFile("/Users/vaskozlov/IdeaProjects/Programming5/test_database/test2.csv");
        usersDatabases.put(user, org);
    }

    @Override
    protected void handleDisconnectCommand(User user) {
        usersDatabases.remove(user);
    }

    private Organization getOrganization(JsonNode jsonNode) throws JsonProcessingException {
        return objectMapper.readValue(jsonNode.toString(), Organization.class);
    }

    private Address getAddress(JsonNode jsonNode) throws JsonProcessingException {
        return objectMapper.readValue(jsonNode.toString(), Address.class);
    }

    private String getString(JsonNode jsonNode) throws JsonProcessingException {
        return objectMapper.readValue(jsonNode.toString(), String.class);
    }

    private Integer getInt(JsonNode jsonNode) throws JsonProcessingException {
        return objectMapper.readValue(jsonNode.toString(), Integer.class);
    }

    protected JsonNode getObjectNode(DatagramPacket packet) throws JsonProcessingException {
        String result = new String(packet.getData(), 0, packet.getLength());
        JsonNode jsonNodeRoot = objectMapper.readTree(result);
        return jsonNodeRoot.get("object");
    }
}
