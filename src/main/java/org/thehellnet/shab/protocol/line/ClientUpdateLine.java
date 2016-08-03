package org.thehellnet.shab.protocol.line;

import org.thehellnet.shab.protocol.Command;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;
import org.thehellnet.shab.protocol.exception.ParseLineException;

/**
 * Created by sardylan on 03/08/16.
 */
public class ClientUpdateLine extends Line {

    private static final Command COMMAND = Command.CLIENT_UPDATE;

    private float latitude;
    private float longitude;
    private float altitude;

    public ClientUpdateLine() {
        super(COMMAND);
    }

    public ClientUpdateLine(String rawLine) throws AbstractProtocolException {
        super(COMMAND, rawLine);
    }

    @Override
    public String serialize() {
        return null;
    }

    @Override
    protected void parse(String[] items) throws AbstractProtocolException {
        if (!items[1].equals("CU") || items.length != 5) {
            throw new ParseLineException();
        }

        latitude = Float.parseFloat(items[2]);
        longitude = Float.parseFloat(items[3]);
        altitude = Float.parseFloat(items[4]);
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }
}
