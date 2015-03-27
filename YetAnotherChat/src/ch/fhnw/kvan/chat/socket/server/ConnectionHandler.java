package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import java.util.List;

import java.net.Socket;

/**
 * This thread is responsible for listing to one
 * particular client for changes.
 * <p>
 * Created by livio on 27/03/15.
 */
public class ConnectionHandler implements Runnable {

    Socket clientSocket;
    List<ConnectionHandler> chatPeers;
    In clientInputStream;
    Out clientOutputStream;

    public ConnectionHandler(Socket clientSocket, List<ConnectionHandler> chatPeers) {
        this.clientSocket = clientSocket;
        this.chatPeers = chatPeers;
        this.clientOutputStream = new Out(clientSocket);
        this.clientInputStream = new In(clientSocket);

        Thread me = new Thread(this);
        me.start();
    }

    @Override
    public void run() {

        System.out.println("Server is listing for user input.");

        while(clientSocket.isConnected()) {
            String inputString = clientInputStream.readLine();
            System.out.println(inputString);
        }
    }
}
