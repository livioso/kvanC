package ch.fhnw.kvan.chat.servlet;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class Client {

	private static Logger logger = Logger.getLogger(Client.class);
	
	public static void main(String[] args) throws Exception
	{
		String username = args[0];
		String hostname = args[1];
		String portnumber = args[2];

		logger.setLevel(Level.ALL);
		logger.info("Username is: " + username);
		logger.info("Hostname is: " + hostname);
		logger.info("Port number is: " + portnumber);

		if (!portnumber.matches("[1-9]{4}")) {
			throw new Exception("Port number is expected to be 4 digit number.");
		}


		// make sure sockets & streams are set up at this point we depend on working streams.
		//this.messageSender = new ClientMessageSender(outputStream, username);
		//this.ui = new ClientGUI(messageSender, username);
		//this.messagesReceiver = new ClientMessageReceiver(inputStream, ui);
	}
}
