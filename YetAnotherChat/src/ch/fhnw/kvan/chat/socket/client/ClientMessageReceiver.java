package ch.fhnw.kvan.chat.socket.client;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.utils.In;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import org.apache.log4j.Logger;

/**
 * Responsible for receiving inbound messages from server
 */
public class ClientMessageReceiver implements Runnable {

    private Logger logger = Logger.getLogger(Client.class);
    private In clientInputStream;
    private ClientGUI clientGui;

    public ClientMessageReceiver(In clientInputStream, ClientGUI clientGui) {
        this.clientInputStream = clientInputStream;
        this.clientGui = clientGui;

        Thread listener = new Thread(this);
        listener.start();
    }

    @Override
    public void run() {
        while (true) {
            String message = clientInputStream.readLine();
            logger.info("Message received from server: " + message);
            processMessage(message);
        }
    }

    private void processMessage(String message) {

        JsonReader jsonReader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = jsonReader.readObject();

        final String action = jsonMessage.getString("action");

        switch (action) {

            case "add_topic":
                clientGui.addTopic(jsonMessage.getString("topic"));
                break;

            case "remove_topic":
                clientGui.removeTopic(jsonMessage.getString("topic"));
                break;

            case "new_user":
                clientGui.addParticipant(jsonMessage.getString("name"));
                break;

            case "remove_user":
                clientGui.removeParticipant(jsonMessage.getString("name"));
                break;

            case "add_message":
                clientGui.addMessage(jsonMessage.getString("message"));
                break;

            case "response_latest_messages":
                String latestMessages = jsonMessage.getString("messages");
                // we need to reverse the messages (so we have oldest->newest)
                String[] messages = latestMessages.split(";;");
                Collections.reverse(Arrays.asList(messages));
                clientGui.updateMessages(messages);
                break;

            case "response_all_topics":
                String allTopics = jsonMessage.getString("topics");
                clientGui.updateTopics(allTopics.split(";"));
                break;

            case "response_all_participants":
                String allParticipants = jsonMessage.getString("participants");
                clientGui.updateParticipants(allParticipants.split(";"));
                break;
        }
    }
}
