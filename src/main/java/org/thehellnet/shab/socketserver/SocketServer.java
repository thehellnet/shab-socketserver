package org.thehellnet.shab.socketserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.shab.protocol.entity.Client;
import org.thehellnet.shab.protocol.exception.AbstractProtocolException;
import org.thehellnet.shab.protocol.helper.Position;
import org.thehellnet.shab.protocol.line.*;
import org.thehellnet.shab.socketserver.protocol.ServerContext;
import org.thehellnet.shab.socketserver.socket.ClientSocket;
import org.thehellnet.shab.socketserver.socket.ClientSocketCallback;
import org.thehellnet.shab.socketserver.socket.ListenSocket;
import org.thehellnet.shab.socketserver.socket.ListenSocketCallback;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by sardylan on 13/07/16.
 */
public class SocketServer implements ListenSocketCallback, ClientSocketCallback {

    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private ListenSocket listenSocket;
    private List<ClientSocket> clientSockets = new ArrayList<>();

    private ServerContext context;

    public static void main(String[] args) {
        final SocketServer socketServer = new SocketServer();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                socketServer.stop();
            }
        });

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
        logger.debug(String.format("Line from %s: %s", clientSocket.toString(), line));
        parseLine(clientSocket, line);
        clientSockets.stream()
                .filter(cs -> cs != clientSocket)
                .forEach(cs -> cs.send(line));
    }

    @Override
    public void disconnected(ClientSocket clientSocket) {
        logger.debug(String.format("Client from %s disconnected", clientSocket));
        clientSockets.remove(clientSocket);

        if (clientSocket.getClientId() != null) {
            ClientDisconnectLine line = new ClientDisconnectLine();
            line.setId(clientSocket.getClientId());
            sendAllClients(line);
            doClientDisconnected(line);
        }
    }

    private void run(String[] args) {
        logger.info("START");

        context = new ServerContext();

        listenSocket = new ListenSocket(this);
        listenSocket.start(12345);
        listenSocket.join();

        logger.info("STOP");
    }

    private void stop() {
        logger.info("STOP REQUEST");
        listenSocket.stop();
    }

    private void sendAllClients(Line line) {
        clientSockets.forEach(cs -> cs.send(line.serialize()));
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
            doClientConnected((ClientConnectLine) abstractLine, clientSocket);
        } else if (abstractLine instanceof ClientUpdateLine) {
            doClientUpdate((ClientUpdateLine) abstractLine);
        } else if (abstractLine instanceof ClientDisconnectLine) {
            doClientDisconnected((ClientDisconnectLine) abstractLine);
        } else if (abstractLine instanceof HabPositionLine) {
            doHabPosition((HabPositionLine) abstractLine);
        } else if (abstractLine instanceof HabImageLine) {
            doHabImage((HabImageLine) abstractLine);
        } else if (abstractLine instanceof HabTelemetryLine) {
            doHabTelemetry((HabTelemetryLine) abstractLine);
        }
    }

    private void doClientConnected(ClientConnectLine line, ClientSocket clientSocket) {
        logger.info("doClientConnected");

        removeClientIfExists(line.getId());

        context.getClients()
                .stream()
                .filter(c -> !line.getId().equals(c.getId()))
                .forEach(client -> {
                    ClientConnectLine clientConnectLine = new ClientConnectLine();
                    clientConnectLine.setId(client.getId());
                    clientConnectLine.setName(client.getName());
                    clientSocket.send(clientConnectLine.serialize());

                    if (client.getPosition() != null) {
                        ClientUpdateLine clientUpdateLine = new ClientUpdateLine();
                        clientUpdateLine.setId(client.getId());
                        clientUpdateLine.setLatitude(client.getPosition().getLatitude());
                        clientUpdateLine.setLongitude(client.getPosition().getLongitude());
                        clientUpdateLine.setAltitude(client.getPosition().getAltitude());
                        clientSocket.send(clientUpdateLine.serialize());
                    }
                });

        Client client = new Client();
        client.setId(line.getId());
        client.setName(line.getName());
        context.getClients().add(client);

        clientSocket.setClientId(line.getId());

        logger.info(String.format("New client connected: %s", client));
    }

    private void doClientUpdate(ClientUpdateLine line) {
        logger.info("doClientUpdate");

        Optional<Client> clientOptional = context.getClients().stream()
                .filter(c -> c.getId().equals(line.getId()))
                .findFirst();
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            Position position = new Position(line);
            client.setPosition(position);
            context.getClients().add(client);
            logger.info(String.format("Client %s updated", client));
        }
    }

    private void doClientDisconnected(ClientDisconnectLine line) {
        logger.info("doClientDisconnected");

        Optional<Client> clientOptional = context.getClients().stream()
                .filter(c -> c.getId().equals(line.getId()))
                .findFirst();
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            context.getClients().remove(client);
            logger.info(String.format("Client %s disconnected", client));
        }
    }

    private void doHabPosition(HabPositionLine line) {
        logger.info("doHabPosition");

        Position position = new Position(line);
        context.getHab().setPosition(position);
        logger.info("New HAB position");
    }

    private void doHabImage(HabImageLine line) {
        logger.info("doHabImage");

        if (line.getSliceNum() == 1) {
            context.getHab().clearImageData();
        }

        context.getHab().setSliceTot(line.getSliceTot());
        context.getHab().setSliceNum(line.getSliceNum());
        context.getHab().appendImageData(line.getData());
        logger.info("New image slice from HAB");

        if (line.getSliceNum() == line.getSliceTot()) {
            handleNewImage(context.getHab().getImageData());
            logger.info("New image completed");
        }
    }

    private void doHabTelemetry(HabTelemetryLine line) {
        logger.info("doHabTelemetry");
    }

    private void handleNewImage(byte[] imageData) {
    }

    private void removeClientIfExists(String clientId) {
        List<Client> clientsToRemove = context.getClients()
                .stream()
                .filter(c -> c.getId().equals(clientId))
                .collect(Collectors.toList());

        clientsToRemove.forEach(client -> {
            ClientDisconnectLine line = new ClientDisconnectLine();
            line.setId(client.getId());
            sendAllClients(line);
            doClientDisconnected(line);
        });

        context.getClients().removeAll(clientsToRemove);

        clientSockets.stream()
                .filter(clientSocket -> clientId.equals(clientSocket.getClientId()))
                .forEach(clientSocket -> clientSocket.setClientId(null));
    }
}
