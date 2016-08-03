package org.thehellnet.shab.protocol.line;

import org.thehellnet.shab.protocol.Command;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;
import org.thehellnet.shab.protocol.exception.ParseLineException;

/**
 * Created by sardylan on 03/08/16.
 */
public class ClientUpdateLine extends Line {

    private static final Command COMMAND = Command.CLIENT_UPDATE;
    public static final String COMMAND_TAG = "CU";

    private String id;
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
    public String serializeLine() {
        return String.format("%s|%s|%.06f|%.06f|%.06f", COMMAND_TAG, id, latitude, longitude, altitude);
    }

    @Override
    protected void parse(String[] items) throws AbstractProtocolException {
        if (!items[1].equals(COMMAND_TAG) || items.length != 6) {
            throw new ParseLineException();
        }

        id = items[2];
        latitude = Float.parseFloat(items[3]);
        longitude = Float.parseFloat(items[4]);
        altitude = Float.parseFloat(items[5]);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
