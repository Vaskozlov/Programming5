package commands;

import client.Application;
import commands.core.ClientSideCommand;
import lib.ExecutionStatus;
import lib.functions.CallbackFunction;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ExecuteScriptCommand extends ClientSideCommand {
    public ExecuteScriptCommand(CallbackFunction callbackFunction, Application application) {
        super(callbackFunction, application);
    }

    @Override
    protected void executeImplementation(String[] args, CallbackFunction callback) throws FileNotFoundException {
        String filename = args[0];
        FileReader fileReader = new FileReader(filename);

        application.getBufferedReaderWithQueueOfStreams().pushStream(fileReader);
        callback.invoke(ExecutionStatus.SUCCESS, null, filename);
    }
}
