package org.thehellnet.shab.protocol.line;

import org.thehellnet.shab.protocol.Command;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;
import org.thehellnet.shab.protocol.exception.ParseLineException;

/**
 * Created by sardylan on 03/08/16.
 */
public class HabTelemetryLine extends Line {

    private static final Command COMMAND = Command.HAB_TELEMETRY;
    public static final String COMMAND_TAG = "HT";

    public HabTelemetryLine() {
        super(COMMAND);
    }

    public HabTelemetryLine(String rawLine) throws AbstractProtocolException {
        super(COMMAND, rawLine);
    }

    @Override
    public String serializeLine() {
        return null;
    }

    @Override
    protected void parse(String[] items) throws AbstractProtocolException {
        if (!items[1].equals(COMMAND_TAG) || items.length != 5) {
            throw new ParseLineException();
        }
    }
}
