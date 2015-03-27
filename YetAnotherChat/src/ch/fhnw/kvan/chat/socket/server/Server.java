package ch.fhnw.kvan.chat.socket.server;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ch.fhnw.kvan.chat.interfaces.IChatDriver;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.general.ChatRoomDriver;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static Logger logger = Logger.getLogger(Server.class);

    private static final int LISTING_PORT = 1235;

    public static void main(String[] args) {

        logger.setLevel(Level.ALL);

        try {

            IChatDriver theChatRoomDriver = new ChatRoomDriver();

            // FIXME: Parameters don't matter at all (not used) ?!
            // Sole purpose of this call is to initiate the
            // chat room singleton (created on first getInstance).
            // Why this isn't done within the constructor is beyond me.
            theChatRoomDriver.connect("", 0);

            IChatRoom theChatRoom = theChatRoomDriver.getChatRoom();

            ServerSocket serverSocket = new ServerSocket(LISTING_PORT);
            logger.info("Listing for new incoming connections");

            while(true) {
                Socket incomingConnectionSocket = serverSocket.accept();

                logger.info("New incoming connection detected from "
                        + incomingConnectionSocket.getInetAddress());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
