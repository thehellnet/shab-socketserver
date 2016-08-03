package org.thehellnet.shab.protocol.line;

import org.thehellnet.shab.protocol.Command;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;

/**
 * Created by sardylan on 03/08/16.
 */
public abstract class Line {

    private String rawLine;
    private Command command;

    protected Line(Command command) {
        this.command = command;
    }

    protected Line(Command command, String rawLine) throws AbstractProtocolException {
        this.command = command;
        this.rawLine = rawLine;
        parse(rawLine.split("|"));
    }

    public String getRawLine() {
        return rawLine;
    }

    public void setRawLine(String rawLine) {
        this.rawLine = rawLine;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public abstract String serialize();

    protected abstract void parse(String[] items) throws AbstractProtocolException;
}
