package ch.fhnw.kvan.chat.socket.client;

public class Client {
	
	static private String host;
	static private String port;
	static private String username;
	
	public static void main(String[] args) {
		
		if(args.length != 3) {
			System.err.println("Expecting <host> <port> <username>");
		}
		
		host = args[0];
		port = args[1];
		username = args[2]; 
		
		System.out.println(username);
		System.out.println(host);
		System.out.println(port);
	}
}