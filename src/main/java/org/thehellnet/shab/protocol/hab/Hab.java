package org.thehellnet.shab.protocol.hab;

import org.thehellnet.shab.protocol.Position;

/**
 * Created by sardylan on 03/08/16.
 */
public class Hab {

    private Position position = new Position();

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
