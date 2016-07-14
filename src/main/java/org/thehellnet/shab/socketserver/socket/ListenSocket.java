package org.thehellnet.shab.socketserver.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sardylan on 13/07/16.
 */
public class ListenSocket {

    private ListenSocketCallback callback;
    private ServerSocket socket;
    private Thread thread;
    private boolean keepRunning;

    public ListenSocket(ListenSocketCallback callback) {
        this.callback = callback;
    }

    public void start(int port) {
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        keepRunning = true;
        thread = new Thread(() -> {
            while (keepRunning) {
                Socket newSocket;
                try {
                    newSocket = socket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                callback.newClient(newSocket);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        keepRunning = false;

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        socket = null;
        thread = null;
    }
}
