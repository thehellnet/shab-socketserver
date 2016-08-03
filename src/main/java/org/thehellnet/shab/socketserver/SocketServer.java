package org.thehellnet.shab.socketserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.shab.protocol.LineFactory;
import org.thehellnet.shab.protocol.Position;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;
import org.thehellnet.shab.protocol.hab.Hab;
import org.thehellnet.shab.protocol.line.*;
import org.thehellnet.shab.socketserver.protocol.Client;
import org.thehellnet.shab.socketserver.socket.ClientSocket;
import org.thehellnet.shab.socketserver.socket.ClientSocketCallback;
import org.thehellnet.shab.socketserver.socket.ListenSocket;
import org.thehellnet.shab.socketserver.socket.ListenSocketCallback;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sardylan on 13/07/16.
 */
public class SocketServer implements ListenSocketCallback, ClientSocketCallback {

    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private ListenSocket listenSocket;
    private List<ClientSocket> clientSockets = new ArrayList<>();
    private String lastLine = "";

    private Hab hab = new Hab();
    private Set<Client> clients = new HashSet<>();

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
        if (lastLine.equals(line)) {
            return;
        }

        lastLine = line;
        logger.debug(String.format("Line from %s: %s", clientSocket.toString(), line));

        parseLine(clientSocket, line);

        sendAllClients(line);
    }

    @Override
    public void disconnected(ClientSocket clientSocket) {
        logger.info(String.format("Client from %s disconnected", clientSocket));
        clientSockets.remove(clientSocket);

        if (clientSocket.getClientId() != null) {
            ClientDisconnectLine line = new ClientDisconnectLine();
            line.setId(clientSocket.getClientId());
            sendAllClients(line);
        }
    }

    private void run(String[] args) {
        logger.info("START");
        listenSocket = new ListenSocket(this);
        listenSocket.start(12345);
        listenSocket.join();
        logger.info("STOP");
    }

    private void sendAllClients(Line line) {
        sendAllClients(line.serialize());
    }

    private void sendAllClients(String line) {
        clientSockets.forEach(cs -> cs.send(line));
    }

    private void parseLine(ClientSocket clientSocket, String rawLine) {
        Line abstractLine;

        try {
            abstractLine = LineFactory.parse(rawLine);
        } catch (AbstractProtocolException e) {
            logger.warn("Invalid line");
            return;
        }

        if (abstractLine instanceof ClientConnectLine) {
            ClientConnectLine line = (ClientConnectLine) abstractLine;
            Client client = new Client();
            client.setId(line.getId());
            client.setName(line.getName());
            clients.add(client);

            clientSocket.setClientId(line.getId());
        }

        if (abstractLine instanceof ClientUpdateLine) {
            ClientUpdateLine line = (ClientUpdateLine) abstractLine;
            Client client = clients.stream()
                    .filter(c -> c.getId().equals(line.getId()))
                    .findFirst()
                    .get();
            Position position = new Position(line);
            client.setPosition(position);
            clients.add(client);
        }

        if (abstractLine instanceof ClientDisconnectLine) {
            ClientDisconnectLine line = (ClientDisconnectLine) abstractLine;
            Client client = clients.stream()
                    .filter(c -> c.getId().equals(line.getId()))
                    .findFirst()
                    .get();
            clients.remove(client);
        }
        if (abstractLine instanceof HabPositionLine) {
            HabPositionLine line = (HabPositionLine) abstractLine;
            Position position = new Position(line);
            hab.setPosition(position);
        }

        if (abstractLine instanceof HabImageLine) {
            HabImageLine line = (HabImageLine) abstractLine;
            handleNewImage(line.getSliceTot(), line.getSliceNum(), line.getData());
        }

        if (abstractLine instanceof HabTelemetryLine) {
            HabTelemetryLine line = (HabTelemetryLine) abstractLine;
        }
    }

    private void handleNewImage(int sliceTot, int sliceNum, byte[] data) {

    }
}
