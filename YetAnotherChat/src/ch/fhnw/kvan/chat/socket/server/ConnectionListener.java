package ch.fhnw.kvan.chat.socket.server;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private List<ConnectionHandler> chatClients =
            Collections.synchronizedList(new ArrayList<>());

    public ConnectionListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        logger.setLevel(Level.ALL);
    }

    @Override
    public void run() {
        logger.info("Listing on port " + serverSocket.getLocalPort());

        while (true) {
            try {
                Socket incomingSocket = serverSocket.accept();
                addNewClientSocket(incomingSocket);
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        }
    }

    /**
     * Adds a new client connection to the chatClients collection.
     *
     * @param incomingSocket Socket to the client
     */
    private void addNewClientSocket(Socket incomingSocket) {
        ConnectionHandler newClientConnectionHandler =
                new ConnectionHandler(incomingSocket, chatClients);
        chatClients.add(newClientConnectionHandler);
    }
}
