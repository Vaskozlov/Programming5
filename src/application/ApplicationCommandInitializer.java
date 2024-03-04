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
                                        application::helpCommandCallback,
                                        application
                                )
                        ),
                        getCommandPair(
                                "command.info",
                                new InfoCommand(
                                        application::infoCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.show",
                                new ShowCommand(
                                        application::showCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.add",
                                new AddCommand(
                                        application::addCommandCallback,
                                        application,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.update",
                                new UpdateCommand(
                                        application::modifyOrganizationCommandCallback,
                                        application,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.remove_by_id",
                                new RemoveByIdCommand(
                                        application::removeCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.clear",
                                new ClearCommand(
                                        application::clearCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.save",
                                new SaveCommand(
                                        application::saveCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.read",
                                new ReadCommand(
                                        application::readCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.execute_script",
                                new ExecuteScriptCommand(
                                        application::executeScriptCommandCallback,
                                        application
                                )
                        ),
                        getCommandPair(
                                "command.exit",
                                new ExitCommand(
                                        application::exitCommandCallback,
                                        application
                                )
                        ),
                        getCommandPair(
                                "command.remove_head",
                                new RemoveHeadCommand(
                                        application::removeHeadCommandCallback,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.add_if_max",
                                new AddIfMaxCommand(
                                        application::addMaxCommandCallback,
                                        application,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.history",
                                new PrintHistoryCommand(
                                        application::showHistoryCommandCallback,
                                        application)
                        ),
                        getCommandPair(
                                "command.max_by_full_name",
                                new MaxByFullNameCommand(
                                        application::maxByFullNameCommandCallback,
                                        application.getOrganizationManager())
                        ),
                        getCommandPair(
                                "command.remove_all_by_postal_address",
                                new RemoveAllByPostalAddressCommand(
                                        application::removeAllByPostalAddressCommandCallback,
                                        application,
                                        application.getOrganizationManager()
                                )
                        ),
                        getCommandPair(
                                "command.sum_of_annual_turnover",
                                new SumOfAnnualTurnoverCommand(
                                        application::sumOfAnnualTurnoverCommandCallback,
                                        application.getOrganizationManager()
                                )
                        )
                )
        );
    }
}
