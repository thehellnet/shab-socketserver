package org.thehellnet.shab.socketserver.protocol;

/**
 * Created by sardylan on 03/08/16.
 */
public class Client {

    private String id;
    private String name;
    private Position position;

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
}
