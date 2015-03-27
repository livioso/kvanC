package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.general.ChatRoomDriver;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;

import java.net.ServerSocket;

public class Server {

    /* Specifies the port that the server listens on. */
    private static final int LISTING_PORT = 1235;
    private static IChatRoom theChatRoom = ChatRoom.getInstance();

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket =
                    new ServerSocket(LISTING_PORT);

            ConnectionListener connectionListener =
                    new ConnectionListener(serverSocket);

            Thread connectionListenerThread =
                    new Thread(connectionListener);

            // server now listing
            connectionListenerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
