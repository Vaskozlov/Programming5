package commands.client_side

import application.Application
import commands.client_side.core.ClientSideCommand
import exceptions.UnableToReadFromFileException
import java.io.FileReader

class ExecuteScriptCommand(application: Application) :
    ClientSideCommand(application) {
    override fun executeImplementation(argument: Any?): Result<String> {
        val filename = argument as String

        return try {
            FileReader(filename).use { fileReader ->
                application.bufferedReaderWithQueueOfStreams.pushStream(fileReader)
            }
            Result.success(filename)
        } catch (e: Exception) {
            Result.failure(UnableToReadFromFileException(filename));
        }
    }
}
