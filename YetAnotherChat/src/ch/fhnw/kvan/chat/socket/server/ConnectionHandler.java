package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import java.util.List;
import javax.json.*;
import java.io.StringReader;

import java.net.Socket;

/**
 * This thread is responsible for listing to one
 * particular client for changes.
 * <p>
 * Created by livio on 27/03/15.
 */
public class ConnectionHandler implements Runnable {

    Socket clientSocket;
    private List<ConnectionHandler> chatPeers;
    private In clientInputStream;
    Out clientOutputStream;

    public ConnectionHandler(Socket clientSocket, List<ConnectionHandler> chatPeers) {
        this.clientSocket = clientSocket;
        this.chatPeers = chatPeers;
        this.clientOutputStream = new Out(clientSocket);
        this.clientInputStream = new In(clientSocket);

        // listens as long as the client is connected
        // or forcefully quites once the pipe seems broken.
        Thread clientListener = new Thread(this);
        clientListener.start();
    }

    @Override
    public void run() {

        // as soon as the pipe is
        // broken the read line just
        // gets mambo jumbo messages
        boolean pipeIsAlive = true;

        while(pipeIsAlive) {

            String message = clientInputStream.readLine();

            if(message == null) {
                pipeIsAlive = false;
            } else {
                processNewMessage(message);
            }


            // FIXME: Review me, it works but is it the way to go?
            //for(ConnectionHandler i: chatPeers) {
            //    System.out.println(i.clientSocket.getInetAddress());
            //    i.clientOutputStream.println(inputString);
            //}
        }
    }

    private void processNewMessage(String message) {
        JsonReader jsonReader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = jsonReader.readObject();
    }
}
