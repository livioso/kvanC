package ch.fhnw.kvan.chat.servlet;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.io.IOException;

public class Client {

	private static Logger logger = Logger.getLogger(Client.class);

	private String username;
	private String urlServer;

	private HTTPMessageSender messageSender;

	public Client (String username, String urlServer) throws IOException {

		this.username = username;
		this.urlServer = urlServer;
		this.messageSender = new HTTPMessageSender(urlServer);

		logger.setLevel(Level.ALL);
		logger.info("Username is: " + username);
		logger.info("Server is at: " + urlServer);

		ClientGUI ui = new ClientGUI(messageSender, username);

		doInitialUserInterfaceUpdate();
	}

	public static void main(String[] args) throws Exception
	{
		if (args.length != 2) {
			throw new Exception("Expecting <host> <username> parameters.");
		} else {
			Client newClient = new Client(args[0], args[1]);

		}
	}

	/**
	 * Announce client and update ui with existing topics and participants
	 */
	private void doInitialUserInterfaceUpdate() throws IOException {
		messageSender.addParticipant(username); // announce himself
		messageSender.getExistingTopics();
		messageSender.getExistingParticipants();
	}
}
