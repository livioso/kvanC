package ch.fhnw.kvan.chat.socket.client;

import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.utils.Out;

import java.io.IOException;
import javax.json.JsonObject;
import javax.json.Json;

/**
 * Responsible for sending outbound messages to the server
 * using a corresponding client output stream.
 */
public class ClientMessageSender implements IChatRoom {

    private String username;
    private Out outputStream;

    public ClientMessageSender(Out outputStream, String username) {
        this.outputStream = outputStream;
        this.username = username;
    }

    @Override
    public boolean addParticipant(String name) throws IOException {
        JsonObject addParticipantJson = Json.createObjectBuilder()
                .add("action", "new_user")
                .add("name", name).build();
        outputStream.println(addParticipantJson);
        return true;
    }

    @Override
    public boolean removeParticipant(String name) throws IOException {
        JsonObject removeParticipantJson = Json.createObjectBuilder()
                .add("action", "remove_user")
                .add("name", name).build();
        outputStream.println(removeParticipantJson);
        return true;
    }

    @Override
    public boolean addTopic(String topic) throws IOException {
        JsonObject addTopicJson = Json.createObjectBuilder()
                .add("action", "add_topic")
                .add("topic", topic).build();
        outputStream.println(addTopicJson);
        return true;
    }

    @Override
    public boolean removeTopic(String topic) throws IOException {
        JsonObject removeTopicJson = Json.createObjectBuilder()
                .add("action", "remove_topic")
                .add("topic", topic).build();
        outputStream.println(removeTopicJson);
        return true;
    }

    @Override
    public boolean addMessage(String topic, String message) throws IOException {
        JsonObject addMessageToTopicJson = Json.createObjectBuilder()
                .add("action", "add_message")
                .add("message", username + ": " + message)
                .add("topic", topic).build();
        outputStream.println(addMessageToTopicJson);
        return true;
    }

    @Override
    public String getMessages(String topic) throws IOException {
        JsonObject getMessagesFromTopicJson = Json.createObjectBuilder()
                .add("action", "get_latest_messages")
                .add("topic", topic).build();
        outputStream.println(getMessagesFromTopicJson);
        return "";
    }

    @Override
    public String refresh(String topic) throws IOException {
        return getMessages(topic);
    }

    public void getExistingTopics() {
        JsonObject getTopicsJson = Json.createObjectBuilder()
                .add("action", "get_topics").build();
        outputStream.println(getTopicsJson);
    }

    public void getExistingParticipants() {
        JsonObject getParticipantsJson = Json.createObjectBuilder()
                .add("action", "get_participants").build();
        outputStream.println(getParticipantsJson);
    }
}
