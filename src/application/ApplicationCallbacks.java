package application;

import OrganizationDatabase.Organization;
import lib.ExecutionStatus;
import lib.Localization;
import lib.collections.CircledStorage;

public class ApplicationCallbacks {
    public static void printCommandResult(ExecutionStatus status, String successMessage, String failureMessage) {
        if (status == ExecutionStatus.SUCCESS) {
            System.out.println(successMessage);
        } else {
            System.out.println(failureMessage);
        }
    }

    public static void printCommandResult(ExecutionStatus status, String successMessage) {
        printCommandResult(status, successMessage, Localization.get("message.command.failed"));
    }

    public static void exitCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.exit")
        );
    }

    public static void defaultPrintCallback(ExecutionStatus status, Exception error, Object... result) {
        assert (result.length == 1 && result[0] instanceof String) || error != null;

        printCommandResult(
                status,
                (String) result[0],
                ApplicationErrorMessages.showCommandGetErrorMessage(error)
        );
    }

    public static void clearCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.collection_cleared")
        );
    }

    public static void saveCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert (result.length == 1 && result[0] instanceof String) || error != null;

        String filename = (String) result[0];
        printCommandResult(
                status,
                String.format("%s %s.", Localization.get("message.collection.saved_to_file"), filename),
                String.format("%s %s.", Localization.get("message.collection.unable_to_save_to_file"), filename)
        );
    }

    public static void readCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert (result.length == 1 && result[0] instanceof String) || error != null;

        String filename = (String) result[0];

        printCommandResult(
                status,
                String.format("%s.", Localization.get("message.collection.load.succeed")),
                String.format("%s %s.", Localization.get("message.collection.load.failed"), filename)
        );
    }

    public static void addCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.collection.add.succeed"),
                ApplicationErrorMessages.addCommandGetErrorMessage(error)
        );
    }

    public static void addMaxCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.collection.add.succeed"),
                ApplicationErrorMessages.addMaxCommandGetErrorMessage(error)
        );
    }

    public static void removeCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.organization_removed"),
                ApplicationErrorMessages.removeByIdCommandGetErrorMessage(error)
        );
    }

    public static void removeHeadCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert (result.length == 1 && result[0] instanceof String) || error != null;

        printCommandResult(
                status,
                (String) result[0],
                ApplicationErrorMessages.removeHeadCommandGetErrorMessage(error)
        );
    }

    public static void removeAllByPostalAddressCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.organizations_by_postal_address_removed"),
                ApplicationErrorMessages.removeAllByPostalAddressCommandGetErrorMessage(error)
        );
    }

    public static void modifyOrganizationCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 0;

        printCommandResult(
                status,
                Localization.get("message.organization_modified"),
                ApplicationErrorMessages.modifyOrganizationGetErrorMessage(error)
        );
    }

    public static void maxByFullNameCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert (result.length == 1 && result[0] instanceof Organization) || error != null;

        printCommandResult(
                status,
                status == ExecutionStatus.SUCCESS ? ((Organization) result[0]).toYaml() : ""
        );
    }

    public static void sumOfAnnualTurnoverCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert (result.length == 1 && result[0] instanceof Float) || error != null;

        printCommandResult(
                status,
                status == ExecutionStatus.SUCCESS ?
                        String.format("%s: %f.", Localization.get("message.sum_of_annual_turnover"), (Float) result[0])
                        : ""
        );
    }

    public static void executeScriptCommandCallback(ExecutionStatus status, Exception error, Object... result) {
        assert result.length == 1;
        assert result[0] instanceof String;

        String filename = (String) result[0];

        printCommandResult(
                status,
                Localization.get("message.script_execution.started"),
                ApplicationErrorMessages.executeScriptCommandGetErrorMessage(error, filename)
        );
    }

    public static void showHistoryCommandCallback(ExecutionStatus status, Exception error, Object... result) {
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
}
