package application;

import commands.client_side.*;
import commands.client_side.core.Command;
import lib.collections.ImmutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationCommandInitializer {
    private static ImmutablePair<String, Command> getCommandPair(String localeName, Command command) {
        return new ImmutablePair<>(localeName, command);
    }

    public static List<ImmutablePair<String, Command>> getCommand(Application application) {
        return new ArrayList<>(
                Arrays.asList(
                        getCommandPair(
                                "command.help",
                                new HelpCommand(
                                        ApplicationCallbacks::defaultPrintCallback,
                                        application
                                )
                        ),
                        getCommandPair(
                                "command.info",
                                new InfoCommand(
                                        ApplicationCallbacks::defaultPrintCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.show",
                                new ShowCommand(
                                        (ApplicationCallbacks::defaultPrintCallback),
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.add",
                                new AddCommand(
                                        ApplicationCallbacks::addCommandCallback,
                                        application,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.update",
                                new UpdateCommand(
                                        ApplicationCallbacks::modifyOrganizationCommandCallback,
                                        application,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.remove_by_id",
                                new RemoveByIdCommand(
                                        ApplicationCallbacks::removeCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.clear",
                                new ClearCommand(
                                        ApplicationCallbacks::clearCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.save",
                                new SaveCommand(
                                        ApplicationCallbacks::saveCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.read",
                                new ReadCommand(
                                        ApplicationCallbacks::readCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.execute_script",
                                new ExecuteScriptCommand(
                                        ApplicationCallbacks::executeScriptCommandCallback,
                                        application
                                )
                        ),
                        getCommandPair(
                                "command.exit",
                                new ExitCommand(
                                        ApplicationCallbacks::exitCommandCallback,
                                        application
                                )
                        ),
                        getCommandPair(
                                "command.remove_head",
                                new RemoveHeadCommand(
                                        ApplicationCallbacks::removeHeadCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.add_if_max",
                                new AddIfMaxCommand(
                                        ApplicationCallbacks::addMaxCommandCallback,
                                        application,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.history",
                                new PrintHistoryCommand(
                                        ApplicationCallbacks::showHistoryCommandCallback,
                                        application)
                        ),
                        getCommandPair(
                                "command.max_by_full_name",
                                new MaxByFullNameCommand(
                                        ApplicationCallbacks::maxByFullNameCommandCallback,
                                        application.getOrganizationManager())
                        ),
                        getCommandPair(
                                "command.remove_all_by_postal_address",
                                new RemoveAllByPostalAddressCommand(
                                        ApplicationCallbacks::removeAllByPostalAddressCommandCallback,
                                        application,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.sum_of_annual_turnover",
                                new SumOfAnnualTurnoverCommand(
                                        ApplicationCallbacks::sumOfAnnualTurnoverCommandCallback,
                                        application.getOrganizationManager()
                                )
                        )
                )
        );
    }
}
