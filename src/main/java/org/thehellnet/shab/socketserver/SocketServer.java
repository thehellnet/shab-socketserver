package org.thehellnet.shab.socketserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.shab.socketserver.socket.ClientSocket;
import org.thehellnet.shab.socketserver.socket.ClientSocketCallback;
import org.thehellnet.shab.socketserver.socket.ListenSocket;
import org.thehellnet.shab.socketserver.socket.ListenSocketCallback;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sardylan on 13/07/16.
 */
public class SocketServer implements ListenSocketCallback, ClientSocketCallback {

    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private ListenSocket listenSocket;
    private List<ClientSocket> clientSockets = new ArrayList<>();

    private String lastLine = "";

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.run(args);
    }

    @Override
    public void newClient(Socket socket) {
        ClientSocket clientSocket = new ClientSocket(socket, this);
        clientSocket.start();
        clientSockets.add(clientSocket);
        logger.info(String.format("New client from %s", clientSocket));
    }

    @Override
    public void newLine(ClientSocket clientSocket, String line) {
        if (!lastLine.equals(line)) {
            lastLine = line;
            logger.debug(String.format("Line from %s: %s", clientSocket.toString(), line));
            clientSockets.forEach(cs -> cs.send(line));
        }
    }

    @Override
    public void disconnected(ClientSocket clientSocket) {
        clientSockets.remove(clientSocket);
        logger.info(String.format("Client from %s disconnected", clientSocket));
    }

    private void run(String[] args) {
        logger.info("START");
        listenSocket = new ListenSocket(this);
        listenSocket.start(12345);
        listenSocket.join();
        logger.info("STOP");
    }
}
