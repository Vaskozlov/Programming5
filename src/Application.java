import exceptions.KeyboardInterruptException;
import exceptions.OrganizationAlreadyPresentedException;
import exceptions.OrganizationNotFoundException;
import lib.CircledStorage;
import lib.Localization;
import lib.BufferedReaderWithQueueOfStreams;
import organization.Organization;
import organization.OrganizationManager;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;

public class Application {
    private boolean needToStop = false;
    private final OrganizationManager organizationManager = new OrganizationManager();
    private final BufferedReaderWithQueueOfStreams bufferedReaderWithQueueOfStreams = new BufferedReaderWithQueueOfStreams(new InputStreamReader(System.in));
    private final CircledStorage<String> commandsHistory = new CircledStorage<>(11);

    public Application() {
        Localization.loadBundle("locale/locale", "en");
    }

    public void start(String database) throws IOException, OrganizationAlreadyPresentedException {
        if (database != null) {
            organizationManager.loadFromFile(database);
        }

        chooseLanguage();
        printIntroductionMessage();

        while (!needToStop) {
            processCommand(bufferedReaderWithQueueOfStreams.readLine().strip());
        }
    }

    private void chooseLanguage() throws IOException {
        System.out.println("""
                Choose language:
                0 en (default)
                1 ru""");

        String line = bufferedReaderWithQueueOfStreams.readLine();

        switch (line) {
            case "", "0", "en" -> Localization.loadBundle("locale/locale", "en");
            case "1", "ru" -> Localization.loadBundle("locale/locale", "ru");
            default -> {
                System.out.println("Invalid input. Try again.");
                chooseLanguage();
            }
        }
    }

    private void processCommand(String command) throws IOException, OrganizationAlreadyPresentedException {
        if (processCommandWithArguments(command)) {
            addCommandToHistory(command);
            return;
        }

        if (command.equals(Localization.get("command.exit"))) {
            needToStop = true;
        } else if (command.equals(Localization.get("command.help"))) {
            printHelpMessage();
        } else if (command.equals(Localization.get("command.info"))) {
            printInfo();
        } else if (command.equals(Localization.get("command.clear"))) {
            clearCollection();
        } else if (command.equals(Localization.get("command.save"))) {
            saveCollection();
        } else if (command.equals(Localization.get("command.read"))) {
            readCollection();
        } else if (command.equals(Localization.get("command.show"))) {
            System.out.println(organizationManager.toYaml());
        } else if (command.equals(Localization.get("command.add"))) {
            addOrganization();
        } else if (command.equals(Localization.get("command.add_if_max"))) {
            addOrganizationIfMax();
        } else if (command.equals(Localization.get("command.history"))) {
            printHistory();
        } else if (command.equals(Localization.get("command.remove_head"))) {
            removeHead();
        } else if (command.equals(Localization.get("command.sum_of_annual_turnover"))) {
            printSumOfAnnualTurnover();
        } else if (command.equals(Localization.get("command.remove_all_by_postal_address"))) {
            removeAllByPostalAddress();
        } else if (command.equals(Localization.get("command.max_by_full_name"))) {
            printByMaxValueOfFullName();
        } else if (command.equals(Localization.get("command.execute_script"))) {
            executeScript();
        } else if (command.equals(Localization.get("command.update")) || command.equals(Localization.get("command.remove_by_id"))) {
            System.out.printf(Localization.get("message.command.argument_one.need_argument"), command);
        } else {
            System.out.printf(Localization.get("message.command.not_recognized"), command);
            return;
        }

        addCommandToHistory(command);
    }

    private boolean processCommandWithArguments(String command) throws IOException {
        String updateCommandRegex = String.format("%s \\d+", Localization.get("command.update"));
        String removeByIdCommandRegex = String.format("%s \\d+", Localization.get("command.remove_by_id"));
        String showCommandRegex = String.format("%s \\w+", Localization.get("command.show"));

        if (command.matches(updateCommandRegex)) {
            int id = Integer.parseInt(getLastArgument(command));
            modifyOrganization(id);
        } else if (command.matches(removeByIdCommandRegex)) {
            int id = Integer.parseInt(getLastArgument(command));
            removeOrganization(id);
        } else if (command.matches(showCommandRegex)) {
            String mode = getLastArgument(command.toLowerCase());

            switch (mode) {
                case "json" -> System.out.println(organizationManager.toJson());
                case "csv" -> System.out.println(organizationManager.toCSV());
                case "yaml" -> System.out.println(organizationManager.toYaml());
                default ->
                        System.out.printf("%s: yaml, json, csv.%n", Localization.get("message.show.unrecognizable_format"));
            }
        } else {
            return false;
        }

        return true;
    }

    private static void printIntroductionMessage() {
        System.out.println(Localization.get("message.introduction"));
    }

    private static void printHelpMessage() {
        System.out.printf(Localization.get("message.help"));
    }

    private void printInfo() {
        System.out.println(organizationManager.getInfo());
    }

    private String askAndReadFilename() throws IOException {
        System.out.printf("%s: ", Localization.get("message.ask_for_filename"));
        return bufferedReaderWithQueueOfStreams.readLine();
    }

    private void addCommandToHistory(String command) {
        commandsHistory.append(getFirstArgument(command));
    }

    private void printHistory() {
        commandsHistory.applyFunctionOnAllElements(System.out::println);
    }

    private void printByMaxValueOfFullName() {
        Organization maxOrganization = Collections.max(organizationManager.getOrganizations(),
                Comparator.comparing(Organization::fullName));

        if (maxOrganization != null) {
            System.out.println(maxOrganization.toYaml());
        }
    }

    private void clearCollection() {
        organizationManager.clear();
        System.out.println(Localization.get("message.collection_cleared"));
    }

    private void saveCollection() throws IOException {
        String filename = askAndReadFilename();

        if (!organizationManager.saveToFile(filename)) {
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

    private void readCollection() throws IOException, OrganizationAlreadyPresentedException {
        String filename = askAndReadFilename();

        if (!organizationManager.loadFromFile(filename)) {
            System.out.printf("%s %s.%n", Localization.get("message.collection.load.failed"), filename);
        } else {
            System.out.printf("%s.%n", Localization.get("message.collection.load.succeed"));
        }
    }

    private void addOrganization() throws IOException {
        try {
            organizationManager.add(organizationManager.constructOrganization(bufferedReaderWithQueueOfStreams));
            System.out.println(Localization.get("message.collection.add.succeed"));
        } catch (KeyboardInterruptException exception) {
            System.out.println(Localization.get("message.collection.add.canceled"));
        } catch (IllegalArgumentException illegalAccessException) {
            System.out.println(Localization.get("message.collection.add.failed"));
        } catch (OrganizationAlreadyPresentedException exception) {
            System.out.println(Localization.get("message.organization.error.already_presented"));
        }
    }

    private void addOrganizationIfMax() throws IOException {
        try {
            tryToAddMaxOrganization();
        } catch (KeyboardInterruptException exception) {
            System.out.println(Localization.get("message.collection.add.canceled"));
        } catch (IllegalArgumentException illegalAccessException) {
            System.out.println(Localization.get("message.collection.add.failed"));
        } catch (OrganizationAlreadyPresentedException exception) {
            System.out.println(Localization.get("message.organization.error.already_presented"));
        }
    }

    private void tryToAddMaxOrganization() throws IOException, KeyboardInterruptException, OrganizationAlreadyPresentedException {
        Organization newOrganization = organizationManager.constructOrganization(bufferedReaderWithQueueOfStreams);
        Organization maxOrganization = Collections.max(organizationManager.getOrganizations(),
                Comparator.comparing(Organization::fullName));

        if (maxOrganization.fullName().compareTo(newOrganization.fullName()) < 0) {
            organizationManager.add(newOrganization);
            System.out.println(Localization.get("message.collection.add.succeed"));
        } else {
            System.out.println(Localization.get("message.collection.add.max_check_failed"));
        }
    }

    private void removeOrganization(int id) {
        try {
            organizationManager.removeOrganization(id);
            System.out.println(Localization.get("message.organization_removed"));
        } catch (OrganizationNotFoundException exception) {
            System.out.println(Localization.get("message.unable_to_remove_organization"));
        }
    }

    private void removeHead() {
        Organization removedOrganization = organizationManager.removeHead();

        if (removedOrganization != null) {
            System.out.println(removedOrganization.toYaml());
        }
    }

    private void removeAllByPostalAddress() throws IOException {
        try {
            organizationManager.removeAllByPostalAddress(bufferedReaderWithQueueOfStreams);
        } catch (KeyboardInterruptException exception) {
            System.out.println(Localization.get("message.collection.remove.canceled"));
        }
    }


    private void modifyOrganization(int id) throws IOException {
        try {
            organizationManager.modifyOrganization(id, bufferedReaderWithQueueOfStreams);
        } catch (KeyboardInterruptException exception) {
            System.out.println(Localization.get("message.organization.modification_canceled"));
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println(Localization.get("message.organization.modification_error"));
        } catch (OrganizationAlreadyPresentedException exception) {
            System.out.println(Localization.get("message.organization.error.already_presented_after_modification"));
        }
    }

    private void printSumOfAnnualTurnover() {
        System.out.printf(
                "%s: %f.%n",
                Localization.get("message.sum_of_annual_turnover"),
                organizationManager.getSumOfAnnualTurnover()
        );
    }

    private void executeScript() throws IOException {
        String filename = askAndReadFilename();

        try {
            bufferedReaderWithQueueOfStreams.pushStream(new FileReader(filename));
        } catch (FileNotFoundException exception) {
            System.out.printf(Localization.get("message.file.not_found"), filename);
        }
    }

    private static String getFirstArgument(String command) {
        String[] stringParts = command.split(" ");
        return stringParts[0];
    }

    private static String getLastArgument(String command) {
        String[] stringParts = command.split(" ");
        return stringParts[stringParts.length - 1];
    }
}
