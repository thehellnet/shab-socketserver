package org.thehellnet.shab.socketserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.shab.socketserver.socket.ClientSocket;
import org.thehellnet.shab.socketserver.socket.ClientSocketCallback;
import org.thehellnet.shab.socketserver.socket.ListenSocketCallback;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sardylan on 13/07/16.
 */
public class SocketServer implements ListenSocketCallback, ClientSocketCallback {

    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private List<ClientSocket> clientSockets = new ArrayList<>();

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.run(args);
    }

    @Override
    public void newClient(Socket socket) {
        ClientSocket clientSocket = new ClientSocket(socket, this);
        clientSocket.start();
        clientSockets.add(clientSocket);
    }

    @Override
    public void newLine(String line) {

    }

    private void run(String[] args) {
        logger.info("START");

        logger.info("STOP");
    }

}
