package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.general.ChatRoom;
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

    // FIXME: This really shouldn't be public.
    private Socket clientSocket;
    private List<ConnectionHandler> chatPeers;
    private In clientInputStream;
    private Out clientOutputStream;

    private ChatRoom theChatRoom = ChatRoom.getInstance();
    private Logger logger = Logger.getLogger(Server.class);

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

            case "new_user":
                addNewUser(jsonRecievedMessage);
                break;

            case "remove_user":
                removeUser(jsonRecievedMessage);
                break;

            case "add_message":
                addMessageToTopic(jsonRecievedMessage);
                break;

            case "get_latest_messages":
                getLatestMessages(jsonRecievedMessage);
                break;

            case "get_topics":
                getTopics(jsonRecievedMessage);
                break;

            case "get_participants":
                getParticipants(jsonRecievedMessage);
                break;
        }

        // notify all the chat peers.
        // for messages they don't care there
        // is simply not message handler implemented
        notifyAllChatPeers(jsonRecievedMessage);
    }

    private void notifyAllChatPeers(JsonObject withJsonMessage) {
        logger.info("Sending message to clients: " + withJsonMessage);

        for (ConnectionHandler each : chatPeers) {

            if (each == this) {
                continue;
            }

            each.clientOutputStream.println(withJsonMessage);
        }
    }

    private void addTopic(JsonObject addTopicJsonMessage) throws IOException {
        String newTopic = addTopicJsonMessage.getString("topic");

        theChatRoom.addTopic(newTopic);
    }

    private void removeTopic(JsonObject removeTopicJsonMessage) throws IOException {
        String removeTopic = removeTopicJsonMessage.getString("topic");

        theChatRoom.removeTopic(removeTopic);
    }

    private void addNewUser(JsonObject newUserJsonMessage) throws IOException {
        String newUserName = newUserJsonMessage.getString("name");

        theChatRoom.addParticipant(newUserName);
    }

    private void removeUser(JsonObject removeUserJsonMessage) throws IOException {
        String removeUserName = removeUserJsonMessage.getString("name");

        theChatRoom.removeParticipant(removeUserName);
    }

    private void addMessageToTopic(JsonObject jsonAddMessageToTopic) throws IOException {
        String topic = jsonAddMessageToTopic.getString("topic");
        String message = jsonAddMessageToTopic.getString("message");

        theChatRoom.addMessage(topic, message);
    }

    private void getLatestMessages(JsonObject jsonAddMessageToTopic) throws IOException {
        String topic = jsonAddMessageToTopic.getString("topic");
        String responseMessages = theChatRoom.getMessages(topic);
        responseMessages = responseMessages.replaceFirst("messages=", "");

        JsonObject replyLatestMessagesFromTopicJson = Json.createObjectBuilder()
                .add("action", "response_latest_messages")
                .add("messages", responseMessages).build();
        clientOutputStream.println(replyLatestMessagesFromTopicJson);
    }

    private void getTopics(JsonObject jsonGetTopics) throws IOException {
        String responseAllTopics = theChatRoom.getTopics();
        responseAllTopics = responseAllTopics.replaceFirst("topics=", "");

        // there is a trailing ; at the very end of the string => not needed
        responseAllTopics = responseAllTopics.substring(0, responseAllTopics.length()-1);

        JsonObject replyTopicsJson = Json.createObjectBuilder()
                .add("action", "response_all_topics")
                .add("topics", responseAllTopics).build();
        clientOutputStream.println(replyTopicsJson);
    }

    private void getParticipants(JsonObject jsonGetParticipants) throws IOException {
        String responseParticipants = theChatRoom.getParticipants();
        responseParticipants = responseParticipants.replaceFirst("participants=", "");

        // there is a trailing ; at the very end of the string => not needed
        responseParticipants = responseParticipants.substring(0, responseParticipants.length()-1);

        JsonObject replyTopicsJson = Json.createObjectBuilder()
                .add("action", "response_all_participants")
                .add("participants", responseParticipants).build();
        clientOutputStream.println(replyTopicsJson);
    }
}
