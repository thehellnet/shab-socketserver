package org.thehellnet.shab.socketserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sardylan on 13/07/16.
 */
public class SocketServer {

    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.run(args);
    }

    private void run(String[] args) {
        logger.info("START");

        logger.info("STOP");
    }
}
