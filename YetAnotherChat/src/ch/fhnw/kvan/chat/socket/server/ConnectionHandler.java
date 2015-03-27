package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;

import java.io.IOException;
import java.util.List;
import javax.json.*;
import java.io.StringReader;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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

    IChatRoom theChatRoom = ChatRoom.getInstance();

    Logger logger = Logger.getLogger(Server.class);

    public ConnectionHandler(Socket clientSocket, List<ConnectionHandler> chatPeers) {

        logger.setLevel(Level.ALL);

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

        while (pipeIsAlive) {

            String messageFromClient = clientInputStream.readLine();

            if (messageFromClient == null) {
                pipeIsAlive = false;
            }

            try {
                processMessage(messageFromClient);
            } catch (Exception e) {
                logger.warn("Message processing failed. "
                        + "Reason for error: " + e.getMessage());
            }
        }
    }

    private void processMessage(String recievedMessage) throws IOException {

        JsonReader jsonReader = Json.createReader(
                new StringReader(recievedMessage));
        JsonObject jsonRecievedMessage = jsonReader.readObject();

        final String action = jsonRecievedMessage.getString("action");

        switch (action) {

            case "add_topic":
                addTopic(jsonRecievedMessage);
                break;

            case "remove_topic":
                removeTopic(jsonRecievedMessage);
                break;

            case "new_client":
                break;

        }
    }

    private void addTopic(JsonObject addTopicJsonMessage) throws IOException {
        String newTopic = addTopicJsonMessage.getString("topic");

        theChatRoom.addTopic(newTopic);

        notifyAllChatPeers(addTopicJsonMessage);
    }

    private void removeTopic(JsonObject removeTopicJsonMessage) throws IOException {
        String removeTopic = removeTopicJsonMessage.getString("topic");

        theChatRoom.removeTopic(removeTopic);

        notifyAllChatPeers(removeTopicJsonMessage);
    }

    private void notifyAllChatPeers(JsonObject withJsonMessage) {
        // FIXME: Review me, it works but is it the way to go?
        for (ConnectionHandler each : chatPeers) {

            if (each == this) {
                continue;
            }

            logger.info("Sending message " + withJsonMessage
                    + " to " + each.clientSocket.getLocalPort());

            each.clientOutputStream.println(withJsonMessage);
        }
    }
}
