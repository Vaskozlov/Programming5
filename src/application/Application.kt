package application;

import OrganizationDatabase.OrganizationDatabase;
import OrganizationDatabase.OrganizationManagerInterface;
import commands.client_side.core.Command;
import lib.BufferedReaderWithQueueOfStreams;
import lib.Localization;
import lib.collections.CircledStorage;
import lib.collections.ImmutablePair;
import server.DatabaseCommandsReceiver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Application {
    private final OrganizationManagerInterface organizationDatabase = new OrganizationDatabase();
    private final CircledStorage<String> commandsHistory = new CircledStorage<>(11);
    private final BufferedReaderWithQueueOfStreams bufferedReaderWithQueueOfStreams =
            new BufferedReaderWithQueueOfStreams(new InputStreamReader(System.in));

    private boolean needToStop = false;
    private HashMap<String, Command> commandsWithLocaleNames;

    private final List<ImmutablePair<String, Command>> commandList = ApplicationCommandInitializer.getCommand(this);

    public BufferedReaderWithQueueOfStreams getBufferedReaderWithQueueOfStreams() {
        return bufferedReaderWithQueueOfStreams;
    }

    public CircledStorage<String> getCommandsHistory() {
        return commandsHistory;
    }

    public OrganizationManagerInterface getOrganizationManager() {
        return organizationDatabase;
    }

    public void start(String database) throws IOException {
        if (database != null) {
            organizationDatabase.loadFromFile(database);
        }

        DatabaseCommandsReceiver databaseCommandsReceiver = new DatabaseCommandsReceiver(6789);
        databaseCommandsReceiver.run();

        localize();
        printIntroductionMessage();

        while (!needToStop) {
            processCommand(bufferedReaderWithQueueOfStreams.readLine().strip());
        }
    }

    public void stop() {
        needToStop = true;
    }

    private void loadCommands() {
        commandsWithLocaleNames = new HashMap<>();

        for (ImmutablePair<String, Command> command : commandList) {
            commandsWithLocaleNames.put(Localization.get(command.first()), command.second());
        }
    }

    private void localize() throws IOException {
        Localization.askUserForALanguage(bufferedReaderWithQueueOfStreams);
        loadCommands();
    }

    private void processCommand(String command) {
        String[] allArguments = command.split(" +");
        String commandName = allArguments[0];
        String[] args = Arrays.copyOfRange(allArguments, 1, allArguments.length);

        if (commandsWithLocaleNames.containsKey(commandName)) {
            commandsWithLocaleNames.get(commandName).execute(args);
            addCommandToHistory(commandName);
        } else {
            System.out.println(Localization.get("message.command.not_found"));
        }
    }

    private static void printIntroductionMessage() {
        System.out.println(Localization.get("message.introduction"));
    }

    public void addCommandToHistory(String command) {
        commandsHistory.add(command);
    }
}
