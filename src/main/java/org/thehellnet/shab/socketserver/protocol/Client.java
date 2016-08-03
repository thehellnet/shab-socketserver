package org.thehellnet.shab.socketserver.protocol;

import org.thehellnet.shab.protocol.Position;

/**
 * Created by sardylan on 03/08/16.
 */
public class Client {

    private String id;
    private String name;
    private Position position = new Position();

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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        return getId().equals(client.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
