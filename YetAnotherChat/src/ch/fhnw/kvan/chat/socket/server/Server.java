package ch.fhnw.kvan.chat.socket.server;

import java.net.ServerSocket;

public class Server {

    /**
     * Server listens on this port for clients.
     */
    private static final int LISTING_PORT = 1235;

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket = new ServerSocket(LISTING_PORT);
            ConnectionListener connectionListener = new ConnectionListener(serverSocket);
            Thread connectionListenerThread = new Thread(connectionListener);

            // server now listing
            connectionListenerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
