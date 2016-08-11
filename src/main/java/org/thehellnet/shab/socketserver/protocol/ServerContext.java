package org.thehellnet.shab.socketserver.protocol;

import org.thehellnet.shab.protocol.entity.Client;
import org.thehellnet.shab.protocol.entity.Hab;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sardylan on 06/08/16.
 */
public class ServerContext {

    private Hab hab = new Hab();
    private Set<Client> clients = new HashSet<>();
    private int sliceTot;
    private int sliceNum;
    private ByteBuffer imageBuffer;

    public Hab getHab() {
        return hab;
    }

    public void setHab(Hab hab) {
        this.hab = hab;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public ByteBuffer getImageBuffer() {
        return imageBuffer;
    }

    public int getSliceTot() {
        return sliceTot;
    }

    public void setSliceTot(int sliceTot) {
        this.sliceTot = sliceTot;
    }

    public int getSliceNum() {
        return sliceNum;
    }

    public void setSliceNum(int sliceNum) {
        this.sliceNum = sliceNum;
    }

    public void setImageBuffer(ByteBuffer imageBuffer) {
        this.imageBuffer = imageBuffer;
    }
}
