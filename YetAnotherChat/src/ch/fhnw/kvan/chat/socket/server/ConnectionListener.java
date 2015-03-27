package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.interfaces.IChatRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Thread responsible for listing for new clients and
 * managing existing connections to clients.
 * <p>
 * Created by livio on 27/03/15.
 */
public class ConnectionListener implements Runnable {

    private Logger logger = Logger.getLogger(Server.class);
    private ServerSocket serverSocket;

    /**
     * Thread-safe list with all the connections to clients
     */
    private List<ConnectionHandler> connections =
            Collections.synchronizedList(new ArrayList<>());

    public ConnectionListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        logger.setLevel(Level.ALL);
    }

    @Override
    public void run() {
        listen();
    }

    /**
     * Listen for new incoming connections.
     */
    private void listen() {

        logger.info("Listing on port " + serverSocket.getLocalPort());

        while (true) {
            try {
                Socket newClientConnection = serverSocket.accept();
                addNewClientConnection(newClientConnection);
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        }
    }

    /**
     * Adds a new client connection to the connections collection.
     *
     * @param newClientConnection Socket to the client
     */
    private void addNewClientConnection(Socket newClientConnection) {
        ConnectionHandler newClientConnectionHandler =
                new ConnectionHandler(newClientConnection, connections);
        connections.add(newClientConnectionHandler);
    }
}
