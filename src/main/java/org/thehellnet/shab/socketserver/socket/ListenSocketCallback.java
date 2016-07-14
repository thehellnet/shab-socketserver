package org.thehellnet.shab.socketserver.socket;

import java.net.Socket;

/**
 * Created by sardylan on 14/07/16.
 */
public interface ListenSocketCallback {

    void newClient(Socket clientSocket);
}
