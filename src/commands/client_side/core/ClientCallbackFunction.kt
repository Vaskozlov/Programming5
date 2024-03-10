package commands.client_side.core

import lib.ExecutionStatus

fun interface ClientCallbackFunction {
    fun invoke(status: ExecutionStatus, error: Exception?, argument: Any?)
}
