package network.client.udp;

import network.client.DatabaseCommand;

public class Frame {
    DatabaseCommand command;
    Object object;

    public Frame(DatabaseCommand command, Object object) {
        this.command = command;
        this.object = object;
    }

    public DatabaseCommand getCommand() {
        return command;
    }

    public void setCommand(DatabaseCommand command) {
        this.command = command;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
