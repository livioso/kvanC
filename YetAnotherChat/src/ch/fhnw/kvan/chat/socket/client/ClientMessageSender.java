package ch.fhnw.kvan.chat.socket.client;

import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.utils.Out;
import java.io.IOException;
import javax.json.JsonObject;
import javax.json.Json;

public class ClientMessageSender implements IChatRoom {

    private String clientUserIdentifier;
    private Out clientOutputStream;

    public ClientMessageSender(Out clientOutputStream, String clientUserIdentifier) {
        this.clientOutputStream = clientOutputStream;
        this.clientUserIdentifier = clientUserIdentifier;
    }

    @Override
    public boolean addParticipant(String name) throws IOException {
        JsonObject addParticipantJson = Json.createObjectBuilder()
                .add("action", "new_user")
                .add("name", name).build();
        clientOutputStream.println(addParticipantJson.toString());
        return true;
    }

    @Override
    public boolean removeParticipant(String name) throws IOException {
        JsonObject removeParticipantJson = Json.createObjectBuilder()
                .add("action", "remove_user")
                .add("name", name).build();
        clientOutputStream.println(removeParticipantJson.toString());
        return true;
    }

    @Override
    public boolean addTopic(String topic) throws IOException {
        JsonObject addTopicJson = Json.createObjectBuilder()
                .add("action", "add_topic")
                .add("topic", topic).build();
        clientOutputStream.println(addTopicJson.toString());
        return true;
    }

    @Override
    public boolean removeTopic(String topic) throws IOException {
        JsonObject removeTopicJson = Json.createObjectBuilder()
                .add("action", "remove_topic")
                .add("topic", topic).build();
        clientOutputStream.println(removeTopicJson.toString());
        return true;
    }

    @Override
    public boolean addMessage(String topic, String message) throws IOException {
        JsonObject addMessageToTopicJson = Json.createObjectBuilder()
                .add("action", "add_message")
                .add("message", clientUserIdentifier + ": " + message)
                .add("topic", topic).build();
        clientOutputStream.println(addMessageToTopicJson.toString());
        return true;
    }

    @Override
    public String getMessages(String topic) throws IOException {
        JsonObject getMessagesFromTopicJson = Json.createObjectBuilder()
                .add("action", "get_latest_messages")
                .add("topic", topic).build();
        clientOutputStream.println(getMessagesFromTopicJson.toString());
        return "";
    }

    @Override
    public String refresh(String topic) throws IOException {
        return getMessages(topic);
    }

    public void getExistingTopics () {
        JsonObject getTopicsJson = Json.createObjectBuilder()
                .add("action", "get_topics").build();
        clientOutputStream.println(getTopicsJson.toString());
    }

    public void getExistingParticipants() {
        JsonObject getParticipantsJson = Json.createObjectBuilder()
                .add("action", "get_participants").build();
        clientOutputStream.println(getParticipantsJson.toString());
    }
}
