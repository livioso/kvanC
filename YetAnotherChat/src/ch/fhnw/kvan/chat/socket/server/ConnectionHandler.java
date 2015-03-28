package ch.fhnw.kvan.chat.socket.server;

import ch.fhnw.kvan.chat.general.ChatRoom;
import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.List;

/**
 * ConnectionHandler is responsible for listing for
 * requests by one particular client. Each client gets
 * its own connection handler.
 */
public class ConnectionHandler implements Runnable {

    private Logger logger = Logger.getLogger(Server.class);
    private List<ConnectionHandler> chatClients;
    private In inputStream;
    private Out outputStream;
    private ChatRoom theChatRoom = ChatRoom.getInstance();

    public ConnectionHandler(Socket incomingSocket, List<ConnectionHandler> chatClients) {
        logger.setLevel(Level.ALL);

        this.chatClients = chatClients;
        this.outputStream = new Out(incomingSocket);
        this.inputStream = new In(incomingSocket);

        // listens as long as the client is connected
        // or forcefully quites once the pipe seems broken.
        Thread clientListener = new Thread(this);
        clientListener.start();
    }

    @Override
    public void run() {

        while (true) {
            String messageFromClient = inputStream.readLine();

            if (messageFromClient == null) {
                logger.error("Pipe is broken: Disconnecting.");
                break;
            }

            try {
                dispatchMessage(messageFromClient);
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        }
    }

    /**
     * Processes a new received message from the client.
     *
     * @param message Format send by client {"action:" _, "key": "value", ... }
     */
    private void dispatchMessage(String message) throws IOException {
        JsonReader jsonReader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = jsonReader.readObject();

        final String action = jsonMessage.getString("action");

        switch (action) {

            case "add_topic":
                addTopic(jsonMessage);
                break;

            case "remove_topic":
                removeTopic(jsonMessage);
                break;

            case "new_user":
                addNewUser(jsonMessage);
                break;

            case "remove_user":
                removeUser(jsonMessage);
                break;

            case "add_message":
                addMessageToTopic(jsonMessage);
                break;

            case "get_latest_messages":
                getLatestMessages(jsonMessage);
                break;

            case "get_topics":
                getTopics(jsonMessage);
                break;

            case "get_participants":
                getParticipants(jsonMessage);
                break;
        }

        // --> send notifcation to all clients.
        sendMessageToAllClients(jsonMessage);
    }

    private void sendMessageToAllClients(JsonObject withMessage) {
        logger.info("Notify all clients -> Message : " + withMessage);

        for (ConnectionHandler each : chatClients) {
            each.outputStream.println(withMessage);
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
        outputStream.println(replyLatestMessagesFromTopicJson);
    }

    private void getTopics(JsonObject jsonGetTopics) throws IOException {
        String responseAllTopics = theChatRoom.getTopics();
        responseAllTopics = responseAllTopics.replaceFirst("topics=", "");

        if (responseAllTopics.isEmpty()) {
            return; // it's fine to not send anything / client struggles with ""
        }

        // there is a trailing ; at the very end of the string => not needed
        responseAllTopics = responseAllTopics.substring(0, responseAllTopics.length() - 1);

        JsonObject replyTopicsJson = Json.createObjectBuilder()
                .add("action", "response_all_topics")
                .add("topics", responseAllTopics).build();
        outputStream.println(replyTopicsJson);
    }

    private void getParticipants(JsonObject jsonGetParticipants) throws IOException {
        String responseParticipants = theChatRoom.getParticipants();
        responseParticipants = responseParticipants.replaceFirst("participants=", "");

        // there is a trailing ; at the very end of the string => not needed
        if (!responseParticipants.isEmpty()) {
            responseParticipants = responseParticipants.substring(0, responseParticipants.length() - 1);
        }

        JsonObject replyParticipantsJson = Json.createObjectBuilder()
                .add("action", "response_all_participants")
                .add("participants", responseParticipants).build();
        outputStream.println(replyParticipantsJson);
    }
}
