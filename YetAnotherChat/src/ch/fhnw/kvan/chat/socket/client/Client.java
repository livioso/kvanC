package ch.fhnw.kvan.chat.socket.client;

import java.io.IOException;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.general.ChatRoomDriver;
import ch.fhnw.kvan.chat.socket.server.*;

import org.apache.log4j.Logger;

public class Client {
	
	static private Server chatRoomServer = new Server();
	static private Logger logger = Logger.getLogger(Client.class);

	public static void main(String[] args) throws IOException {

		try{
            if(args.length != 3) {
                logger.error("Expecting <host> <port> <username> parameters. Can not proceed.");
                return;
            }

            String username = args[0];
            String host = args[1];
            Integer port = Integer.parseInt(args[2]);

            logger.info(username);
            logger.info(host);
            logger.info(port);

            ClientGUI client = new ClientGUI((new ChatRoomDriver()).getChatRoom(), username);

            // wow, this is really necessary?
            client.addParticipant(username);

		} catch(Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
	}
}