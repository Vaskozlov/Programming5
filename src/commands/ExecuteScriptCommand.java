package commands;

import client.Application;
import commands.core.ClientSideCommand;
import lib.ExecutionStatus;
import lib.Localization;
import lib.functions.CallbackFunction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class ExecuteScriptCommand extends ClientSideCommand {
    public ExecuteScriptCommand(CallbackFunction callbackFunction, Application application) {
        super(callbackFunction, application);
    }

    @Override
    protected void executeThrowableCommand(String[] args, CallbackFunction callback) throws FileNotFoundException {
        String filename = args[0];
        FileReader fileReader = new FileReader(filename);
        application.getBufferedReaderWithQueueOfStreams().pushStream(fileReader);
        callback.invoke(ExecutionStatus.SUCCESS, null, filename);
    }
}
