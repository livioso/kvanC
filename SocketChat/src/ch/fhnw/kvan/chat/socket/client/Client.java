package ch.fhnw.kvan.chat.socket.client;

import java.io.IOException;

import ch.fhnw.kvan.chat.gui.*;
import ch.fhnw.kvan.chat.socket.server.*;

public class Client {
	
	static private String host;
	static private String port;
	static private String username;
	static private Server chatRoomServer = new Server();
	
	public static void main(String[] args) throws IOException {
		
		if(args.length != 3) {
			System.err.println("Expecting <host> <port> <username>");
		}
		
		host = args[0];
		port = args[2];
		username = args[1]; 
		
		System.out.println(username);
		System.out.println(host);
		System.out.println(port);

		chatRoomServer.connect(host, 80);
		
		ClientGUI client = new ClientGUI(chatRoomServer, username);
		
		client.addParticipant(username);
		
	}
}