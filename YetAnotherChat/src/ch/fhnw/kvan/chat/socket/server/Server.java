package ch.fhnw.kvan.chat.socket.server;

import org.apache.log4j.Logger;
import ch.fhnw.kvan.chat.interfaces.IChatDriver;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.general.ChatRoomDriver;

public class Server {

    private static Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) {

        try {
            IChatDriver theChatRoomDriver = new ChatRoomDriver();

            // FIXME: Parameters don't matter at all (not used) ?!
            // Sole purpose of this call is to initiate the
            // chat room singleton (created on first getInstance).
            // Why this isn't done within the constructor is beyond me.
            theChatRoomDriver.connect("localhost", 8080);

            IChatRoom theChatRoom = theChatRoomDriver.getChatRoom();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
