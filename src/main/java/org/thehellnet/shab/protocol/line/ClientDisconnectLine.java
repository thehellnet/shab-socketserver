package org.thehellnet.shab.protocol.line;

import org.thehellnet.shab.protocol.Command;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;
import org.thehellnet.shab.protocol.exception.ParseLineException;

/**
 * Created by sardylan on 03/08/16.
 */
public class ClientDisconnectLine extends Line {

    private static final Command COMMAND = Command.CLIENT_DISCONNECT;
    public static final String COMMAND_TAG = "CD";

    private String id;

    public ClientDisconnectLine() {
        super(COMMAND);
    }

    public ClientDisconnectLine(String rawLine) throws AbstractProtocolException {
        super(COMMAND, rawLine);
    }

    @Override
    public String serializeLine() {
        return String.format("%s|%s", COMMAND_TAG, id);
    }

    @Override
    protected void parse(String[] items) throws AbstractProtocolException {
        if (!items[1].equals(COMMAND_TAG) || items.length != 3) {
            throw new ParseLineException();
        }

        id = items[2];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
