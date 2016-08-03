package org.thehellnet.shab.socketserver.protocol;

/**
 * Created by sardylan on 03/08/16.
 */
public class Position {

    private float latitude;
    private float longitude;
    private float altitude;

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
