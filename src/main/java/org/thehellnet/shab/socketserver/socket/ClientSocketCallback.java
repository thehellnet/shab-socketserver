package org.thehellnet.shab.socketserver.socket;

/**
 * Created by sardylan on 14/07/16.
 */
public interface ClientSocketCallback {

    void newLine(ClientSocket clientSocket, String line);

    void disconnected(ClientSocket clientSocket);
}
