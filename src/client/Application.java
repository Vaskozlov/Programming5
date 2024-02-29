package client;

import commands.*;
import commands.core.Command;
import exceptions.*;
import lib.*;
import organization.OrganizationManager;

import java.io.*;
import java.util.*;

public class Application {
    private boolean needToStop = false;
    private final OrganizationManager organizationManager = new OrganizationManager();
    private final BufferedReaderWithQueueOfStreams bufferedReaderWithQueueOfStreams = new BufferedReaderWithQueueOfStreams(new InputStreamReader(System.in));
    private final CircledStorage<String> commandsHistory = new CircledStorage<>(11);
    private HashMap<String, Command> commandsWithLocaleNames;

    private ArrayList<ImmutablePair<String, Command>> commandList = new ArrayList<>(
            Arrays.asList(
                    new ImmutablePair<>(
                            "command.help",
                            new HelpCommand(this::helpCommandCallback, this)
                    ),
                    new ImmutablePair<>(
                            "command.info",
                            new InfoCommand(this::infoCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.show",
                            new ShowCommand(this::showCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.add",
                            new AddCommand(this::addCommandCallback, this, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.update",
                            new UpdateCommand(this::modifyOrganizationCommandCallback, this, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.remove_by_id",
                            new RemoveByIdCommand(this::removeCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.clear",
                            new ClearCommand(this::clearCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.save",
                            new SaveCommand(this::saveCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.read",
                            new ReadCommand(this::readCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.execute_script",
                            new ExecuteScriptCommand(this::executeScriptCommandCallback, this)
                    ),
                    new ImmutablePair<>(
                            "command.exit",
                            new ExitCommand(this::exitCommandCallback, this)
                    ),
                    new ImmutablePair<>(
                            "command.remove_head",
                            new RemoveHeadCommand(this::removeHeadCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.add_if_max",
                            new AddIfMaxCommand(this::addMaxCommandCallback, this, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.history",
                            new ShowCommand(this::showHistoryCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.max_by_full_name",
                            new MaxByFullNameCommand(this::maxByFullNameCommandCallback, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.remove_all_by_postal_address",
                            new RemoveAllByPostalAddressCommand(this::removeAllByPostalAddressCommandCallback, this, organizationManager)
                    ),
                    new ImmutablePair<>(
                            "command.sum_of_annual_turnover",
                            new SumOfAnnualTurnoverCommand(this::sumOfAnnualTurnoverCommandCallback, organizationManager)
                    )
            )
    );

    public BufferedReaderWithQueueOfStreams getBufferedReaderWithQueueOfStreams() {
        return bufferedReaderWithQueueOfStreams;
    }

    public CircledStorage<String> getCommandsHistory() {
        return commandsHistory;
    }

    private void showCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(result[0]);
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void exitCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(Localization.get("message.exit"));
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void helpCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(result[0]);
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void infoCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(result[0]);
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void clearCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(Localization.get("message.collection_cleared"));
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void saveCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        String filename = (String) result[0];

        if (status == ExecutionStatus.SUCCESS) {
            System.out.printf(
                    "%s %s.%n",
                    Localization.get("message.collection.unable_to_save_to_file"),
                    filename
            );
        } else {
            System.out.printf(
                    "%s %s.%n",
                    Localization.get("message.collection.saved_to_file"),
                    filename
            );
        }
    }

    private void readCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        String filename = (String) result[0];

        if (status == ExecutionStatus.SUCCESS) {
            System.out.printf("%s %s.%n", Localization.get("message.collection.load.failed"), filename);
        } else {
            System.out.printf("%s.%n", Localization.get("message.collection.load.succeed"));
        }
    }

    private void addCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(Localization.get("message.collection.add.succeed"));
        } else if (error instanceof KeyboardInterruptException) {
            System.out.println(Localization.get("message.collection.add.canceled"));
        } else if (error instanceof IllegalArgumentException) {
            System.out.println(Localization.get("message.collection.add.failed"));
        } else if (error instanceof OrganizationAlreadyPresentedException) {
            System.out.println(Localization.get("message.organization.error.already_presented"));
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void addMaxCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(Localization.get("message.collection.add.succeed"));
        } else if (error instanceof KeyboardInterruptException) {
            System.out.println(Localization.get("message.collection.add.canceled"));
        } else if (error instanceof IllegalArgumentException) {
            System.out.println(Localization.get("message.collection.add.failed"));
        } else if (error instanceof OrganizationAlreadyPresentedException) {
            System.out.println(Localization.get("message.organization.error.already_presented"));
        } else if (error == null) {
            System.out.println(Localization.get("message.collection.add.max_check_failed"));
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void removeCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(Localization.get("message.organization_removed"));
        } else if (error instanceof OrganizationNotFoundException) {
            System.out.println(Localization.get("message.unable_to_remove_organization"));
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void removeHeadCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(result[0]);
        } else if (error != null) {
            System.out.println(Localization.get("message.unable_to_remove_organization"));
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void removeAllByPostalAddressCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(Localization.get("message.organizations_by_postal_address_removed"));
        } else if (error instanceof KeyboardInterruptException) {
            System.out.println(Localization.get("message.collection.remove.canceled"));
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void modifyOrganizationCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(Localization.get("message.organization_modified"));
        } else if (error instanceof KeyboardInterruptException) {
            System.out.println(Localization.get("message.organization.modification_canceled"));
        } else if (error instanceof IllegalArgumentException) {
            System.out.println(Localization.get("message.organization.modification_error"));
        } else if (error instanceof OrganizationAlreadyPresentedException) {
            System.out.println(Localization.get("message.organization.error.already_presented_after_modification"));
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void maxByFullNameCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(result[0]);
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void sumOfAnnualTurnoverCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof Float;

        if (status == ExecutionStatus.SUCCESS) {
            Float sum = (Float) result[0];

            System.out.printf(
                    "%s: %f.%n",
                    Localization.get("message.sum_of_annual_turnover"),
                    sum
            );
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void executeScriptCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        String filename = (String) result[0];

        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(Localization.get("message.script_execution.started"));
        } else if (error instanceof FileNotFoundException) {
            System.out.printf(Localization.get("message.file.not_found"), filename);
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    private void showHistoryCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof CircledStorage;

        CircledStorage<String> history = (CircledStorage<String>) result[0];

        if (status == ExecutionStatus.SUCCESS) {
            history.applyFunctionOnAllElements(System.out::println);
        } else {
            System.out.println(Localization.get("message.command.failed"));
        }
    }

    public void stop() {
        needToStop = true;
    }

    public void start(String database) throws IOException {
        if (database != null) {
            organizationManager.loadFromFile(database);
        }

        chooseLanguage();
        printIntroductionMessage();

        while (!needToStop) {
            processCommand(bufferedReaderWithQueueOfStreams.readLine().strip());
        }
    }

    private void loadCommands() {
        commandsWithLocaleNames = new HashMap<>();

        for (ImmutablePair<String, Command> command : commandList) {
            commandsWithLocaleNames.put(Localization.get(command.first()), command.second());
        }
    }

    private void chooseLanguage() throws IOException {
        System.out.println("""
                Choose language:
                0 en (default)
                1 ru""");

        String line = bufferedReaderWithQueueOfStreams.readLine();

        switch (line) {
            case "", "0", "en" -> Localization.loadBundle("localization/localization", "en");
            case "1", "ru" -> Localization.loadBundle("localization/localization", "ru");
            default -> {
                System.out.println("Invalid input. Try again.");
                chooseLanguage();
            }
        }

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
