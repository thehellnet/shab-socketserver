package org.thehellnet.shab.socketserver.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.shab.protocol.line.Line;

import java.io.*;
import java.net.Socket;

/**
 * Created by sardylan on 14/07/16.
 */
public class ClientSocket {

    private static final Logger logger = LoggerFactory.getLogger(ClientSocket.class);

    private ClientSocketCallback callback;

    private Thread thread;
    private Socket socket;
    private boolean keepRunning;
    private BufferedReader reader;
    private PrintWriter writer;

    private String address = "";
    private String lastLine = "";

    private String clientId;

    public ClientSocket(Socket socket, ClientSocketCallback callback) {
        this.socket = socket;
        this.callback = callback;
    }

    public void start() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            address = socket.getRemoteSocketAddress().toString();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return;
        }

        keepRunning = true;
        thread = new Thread(() -> {
            try {
                while (keepRunning && socket.isConnected()) {
                    lastLine = reader.readLine();
                    if (lastLine == null) {
                        socket.close();
                        break;
                    }
                    if (lastLine.length() > 0) {
                        callback.newLine(this, lastLine);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            callback.disconnected(this);
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        keepRunning = false;
        writer.close();
        try {
            reader.close();
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
    }

    public void send(Line line) {
        send(line.serialize());
    }

    public void send(String line) {
        if (lastLine.equals(line) || lastLine.length() == 0) {
            return;
        }
        lastLine = line;
        writer.println(line);
        writer.flush();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return String.format("%s", address);
    }
}
