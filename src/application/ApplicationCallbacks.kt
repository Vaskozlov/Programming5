package application

import database.Organization
import commands.client_side.core.ClientCallbackFunction
import lib.ExecutionStatus
import lib.Localization
import lib.collections.CircledStorage

fun printCommandResult(
    status: ExecutionStatus,
    successMessage: String?,
    failureMessage: String? = Localization.get("message.command.failed")
) {
    if (status == ExecutionStatus.SUCCESS) {
        println(successMessage)
    } else {
        println(failureMessage)
    }
}


val exitCommandCallback = ClientCallbackFunction { status, _, argument ->
    assert(argument == null)

    printCommandResult(
        status,
        Localization.get("message.exit")
    )
}


val defaultPrintCallback = ClientCallbackFunction { status, error, result ->
    printCommandResult(
        status,
        result as String?,
        ApplicationErrorMessages.showCommandGetErrorMessage(error)
    )
}

val clearCommandCallback = ClientCallbackFunction { status, _, result ->
    printCommandResult(
        status,
        Localization.get("message.collection_cleared")
    )
}

val saveCommandCallback = ClientCallbackFunction { status, _, result ->
    val filename = result as String?
    printCommandResult(
        status,
        String.format("%s %s.", Localization.get("message.collection.saved_to_file"), filename),
        String.format("%s %s.", Localization.get("message.collection.unable_to_save_to_file"), filename)
    )
}

val readCommandCallback = ClientCallbackFunction { status, _, result ->
    val filename = result as String?

    printCommandResult(
        status,
        String.format("%s.", Localization.get("message.collection.load.succeed")),
        String.format("%s %s.", Localization.get("message.collection.load.failed"), filename)
    )
}

val addCommandCallback = ClientCallbackFunction { status, error, result ->
    assert(result == null)

    printCommandResult(
        status,
        Localization.get("message.collection.add.succeed"),
        ApplicationErrorMessages.addCommandGetErrorMessage(error)
    )
}

val addMaxCommandCallback = ClientCallbackFunction { status, error, result ->
    assert(result == null)

    printCommandResult(
        status,
        Localization.get("message.collection.add.succeed"),
        ApplicationErrorMessages.addMaxCommandGetErrorMessage(error)
    )
}

val removeCommandCallback = ClientCallbackFunction { status, error, result ->
    assert(result == null)

    printCommandResult(
        status,
        Localization.get("message.organization_removed"),
        ApplicationErrorMessages.removeByIdCommandGetErrorMessage(error)
    )
}

val removeHeadCommandCallback = ClientCallbackFunction { status, error, result ->
    printCommandResult(
        status,
        result as String?,
        ApplicationErrorMessages.removeHeadCommandGetErrorMessage(error)
    )
}


val removeAllByPostalAddressCommandCallback = ClientCallbackFunction { status, error, result ->
    assert(result == null)

    printCommandResult(
        status,
        Localization.get("message.organizations_by_postal_address_removed"),
        ApplicationErrorMessages.removeAllByPostalAddressCommandGetErrorMessage(error)
    )
}

val modifyOrganizationCommandCallback = ClientCallbackFunction { status, error, result ->
    assert(result == null)

    printCommandResult(
        status,
        Localization.get("message.organization_modified"),
        ApplicationErrorMessages.modifyOrganizationGetErrorMessage(error)
    )
}

val maxByFullNameCommandCallback = ClientCallbackFunction { status, _, result ->
    printCommandResult(
        status,
        if (status == ExecutionStatus.SUCCESS) (result as Organization).toYaml() else ""
    )
}

val sumOfAnnualTurnoverCommandCallback = ClientCallbackFunction { status, _, result ->
    printCommandResult(
        status,
        if (status == ExecutionStatus.SUCCESS) String.format(
            "%s: %f.",
            Localization.get("message.sum_of_annual_turnover"),
            result as Float?
        ) else ""
    )
}


val executeScriptCommandCallback = ClientCallbackFunction { status, error, result ->
    val filename = result as String

    printCommandResult(
        status,
        Localization.get("message.script_execution.started"),
        ApplicationErrorMessages.executeScriptCommandGetErrorMessage(error, filename)
    )
}


val showHistoryCommandCallback = ClientCallbackFunction { status, _, result ->
    val history = result as CircledStorage<String>
    val stringBuilder = StringBuilder()
    history.applyFunctionOnAllElements { s: String? -> stringBuilder.append(s).append("\n") }

    printCommandResult(
        status,
        stringBuilder.toString()
    )
}
