package org.thehellnet.shab.protocol;

import org.thehellnet.shab.protocol.line.ClientUpdateLine;
import org.thehellnet.shab.protocol.line.HabPositionLine;

/**
 * Created by sardylan on 03/08/16.
 */
public class Position {

    private float latitude;
    private float longitude;
    private float altitude;

    public Position() {
    }

    public Position(float latitude, float longitude, float altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public Position(ClientUpdateLine line) {
        if (line != null) {
            latitude = line.getLatitude();
            longitude = line.getLongitude();
            altitude = line.getAltitude();
        }
    }

    public Position(HabPositionLine line) {
        if (line != null) {
            latitude = line.getLatitude();
            longitude = line.getLongitude();
            altitude = line.getAltitude();
        }
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
