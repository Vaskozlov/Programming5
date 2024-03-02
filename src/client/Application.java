package client;

import commands.core.Command;
import lib.BufferedReaderWithQueueOfStreams;
import lib.ExecutionStatus;
import lib.Localization;
import lib.collections.CircledStorage;
import lib.collections.ImmutablePair;
import organization.OrganizationManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Application {
    private boolean needToStop = false;
    private final OrganizationManager organizationManager = new OrganizationManager();
    private final BufferedReaderWithQueueOfStreams bufferedReaderWithQueueOfStreams = new BufferedReaderWithQueueOfStreams(new InputStreamReader(System.in));
    private final CircledStorage<String> commandsHistory = new CircledStorage<>(11);
    private HashMap<String, Command> commandsWithLocaleNames;

    private final List<ImmutablePair<String, Command>> commandList = ApplicationCommandInitializer.getCommand(this);

    public BufferedReaderWithQueueOfStreams getBufferedReaderWithQueueOfStreams() {
        return bufferedReaderWithQueueOfStreams;
    }

    public CircledStorage<String> getCommandsHistory() {
        return commandsHistory;
    }

    public OrganizationManager getOrganizationManager() {
        return organizationManager;
    }

    public void start(String database) throws IOException {
        if (database != null) {
            organizationManager.loadFromFile(database);
        }

        localize();
        printIntroductionMessage();

        while (!needToStop) {
            processCommand(bufferedReaderWithQueueOfStreams.readLine().strip());
        }
    }

    public void stop() {
        needToStop = true;
    }

    private void printCommandResult(ExecutionStatus status, String successMessage, String failureMessage) {
        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(successMessage);
        } else {
            System.out.println(failureMessage);
        }
    }

    private void printCommandResult(ExecutionStatus status, String successMessage) {
        printCommandResult(status, successMessage, Localization.get("message.command.failed"));
    }

    void exitCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.exit")
        );
    }

    void showCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        printCommandResult(
                status,
                (String) result[0]
        );
    }

    void helpCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        showCommandCallback(status, error, result); // info, help and show commands have the same callback
    }

    void infoCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        showCommandCallback(status, error, result); // info, help and show commands have the same callback
    }

    void clearCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.collection_cleared")
        );
    }

    void saveCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        String filename = (String) result[0];
        printCommandResult(
                status,
                String.format("%s %s.", Localization.get("message.collection.saved_to_file"), filename),
                String.format("%s %s.", Localization.get("message.collection.unable_to_save_to_file"), filename)
        );
    }

    void readCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        String filename = (String) result[0];

        printCommandResult(
                status,
                String.format("%s.", Localization.get("message.collection.load.succeed")),
                String.format("%s %s.", Localization.get("message.collection.load.failed"), filename)
        );
    }

    void addCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.collection.add.succeed"),
                ApplicationErrorMessages.addCommandGetErrorMessage(error)
        );
    }

    void addMaxCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.collection.add.succeed"),
                ApplicationErrorMessages.addMaxCommandGetErrorMessage(error)
        );
    }

    void removeCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.organization_removed"),
                ApplicationErrorMessages.removeByIdCommandGetErrorMessage(error)
        );
    }

    void removeHeadCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        printCommandResult(
                status,
                (String) result[0],
                ApplicationErrorMessages.removeHeadCommandGetErrorMessage(error)
        );
    }

    void removeAllByPostalAddressCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.organizations_by_postal_address_removed"),
                ApplicationErrorMessages.removeAllByPostalAddressCommandGetErrorMessage(error)
        );
    }

    void modifyOrganizationCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.organization_modified"),
                ApplicationErrorMessages.modifyOrganizationGetErrorMessage(error)
        );
    }

    void maxByFullNameCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        printCommandResult(
                status,
                (String) result[0]
        );
    }

    void sumOfAnnualTurnoverCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof Float;

        printCommandResult(
                status,
                String.format("%s: %f.", Localization.get("message.sum_of_annual_turnover"), (Float) result[0])
        );
    }

    void executeScriptCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        String filename = (String) result[0];

        printCommandResult(
                status,
                Localization.get("message.script_execution.started"),
                ApplicationErrorMessages.executeScriptCommandGetErrorMessage(error, filename)
        );
    }

    void showHistoryCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof CircledStorage;

        CircledStorage<String> history = (CircledStorage<String>) result[0];
        StringBuilder stringBuilder = new StringBuilder();
        history.applyFunctionOnAllElements((String s) -> stringBuilder.append(s).append("\n"));

        printCommandResult(
                status,
                stringBuilder.toString()
        );
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
        String[] allArguments = command.split(" ");
        String commandName = allArguments[0];
        String[] args = Arrays.copyOfRange(allArguments, 1, allArguments.length);

        if (commandsWithLocaleNames.containsKey(commandName)) {
            commandsWithLocaleNames.get(commandName).execute(args);
            addCommandToHistory(commandName);
        } else {
            System.out.println(Localization.get("message.command_not_found"));
        }
    }

    private static void printIntroductionMessage() {
        System.out.println(Localization.get("message.introduction"));
    }

    public void addCommandToHistory(String command) {
        commandsHistory.add(command);
    }
}
