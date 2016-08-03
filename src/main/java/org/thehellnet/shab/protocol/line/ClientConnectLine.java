package org.thehellnet.shab.protocol.line;

import org.thehellnet.shab.protocol.Command;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;
import org.thehellnet.shab.protocol.exception.ParseLineException;

/**
 * Created by sardylan on 03/08/16.
 */
public class ClientConnectLine extends Line {

    private static final Command COMMAND = Command.CLIENT_CONNECT;
    public static final String COMMAND_TAG = "CC";

    private String id;
    private String name;

    public ClientConnectLine() {
        super(COMMAND);
    }

    public ClientConnectLine(String rawLine) throws AbstractProtocolException {
        super(COMMAND, rawLine);
    }

    @Override
    public String serializeLine() {
        return String.format("%s|%s|%s", COMMAND_TAG, id, name);
    }

    @Override
    protected void parse(String[] items) throws AbstractProtocolException {
        if (!items[1].equals(COMMAND_TAG) || items.length != 4) {
            throw new ParseLineException();
        }

        id = items[2];
        name = items[3];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
