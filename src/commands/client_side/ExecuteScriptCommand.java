package commands.client_side;

import application.Application;
import commands.client_side.core.ClientSideCommand;
import lib.ExecutionStatus;
import commands.client_side.core.ClientCallbackFunction;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ExecuteScriptCommand extends ClientSideCommand {
    public ExecuteScriptCommand(ClientCallbackFunction clientCallbackFunction, Application application) {
        super(clientCallbackFunction, application);
    }

    @Override
    protected void executeImplementation(String[] args, ClientCallbackFunction callback) throws FileNotFoundException {
        String filename = args[0];

        try (FileReader fileReader = new FileReader(filename)) {
            application.getBufferedReaderWithQueueOfStreams().pushStream(fileReader);
            callback.invoke(ExecutionStatus.SUCCESS, null, filename);
        } catch (Exception e) {
            callback.invoke(ExecutionStatus.FAILURE, e, filename);
        }
    }
}
