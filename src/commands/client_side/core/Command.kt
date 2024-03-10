package commands.client_side.core

abstract class Command {
    fun execute(argument: Any? = null): Result<Any?> {
        return try {
            executeImplementation(argument)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    protected abstract fun executeImplementation(argument: Any?): Result<Any?>
}
