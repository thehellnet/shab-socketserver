package org.thehellnet.shab.socketserver.socket;

import org.thehellnet.shab.socketserver.thread.StoppableThread;

import java.net.Socket;

/**
 * Created by sardylan on 14/07/16.
 */
public class ClientSocket {

    private ClientSocketCallback callback;
    private Socket socket;
    private String lastLine;
    private StoppableThread thread;

    public ClientSocket(Socket socket, ClientSocketCallback callback) {
        this.socket = socket;
        this.callback = callback;
    }

    public void setCallback(ClientSocketCallback callback) {
        this.callback = callback;
    }

    public void start() {
        thread = new StoppableThread(() -> {

        });
        thread.start();
    }

    public void stop() {
        thread.stop();
    }
}
