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
 * Responsible for receiving inbound messages from server.
 * Listens on the inputStream as long as the user is connected.
 * <p>
 * As soon as received as message it will be processed by
 * dispatchMessage and if interesting for us lead to a ui change.
 */
public class ClientMessageReceiver implements Runnable {

    private Logger logger = Logger.getLogger(Client.class);

    // listen on this input stream for messages
    private In inputStream;

    // ... and update the corresponding
    // ui on new messages.
    private ClientGUI ui;

    public ClientMessageReceiver(In inputStream, ClientGUI ui) {
        this.inputStream = inputStream;
        this.ui = ui;

        Thread listener = new Thread(this);
        listener.start();
    }

    @Override
    public void run() {
        while (true) {
            String message = inputStream.readLine();
            logger.info("Message received from server: " + message);
            dispatchMessage(message);
        }
    }

    /**
     * Processes a new received message from the server. Dispatch and update ui.
     *
     * @param message Format send by server {"action:" _, "key": "value", ... }
     */
    private void dispatchMessage(String message) {
        JsonReader jsonReader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = jsonReader.readObject();

        final String action = jsonMessage.getString("action");

        switch (action) {

            case "add_topic":
                ui.addTopic(jsonMessage.getString("topic"));
                break;

            case "remove_topic":
                ui.removeTopic(jsonMessage.getString("topic"));
                break;

            case "new_user":
                ui.addParticipant(jsonMessage.getString("name"));
                break;

            case "remove_user":
                ui.removeParticipant(jsonMessage.getString("name"));
                break;

            case "add_message":
                ui.addMessage(jsonMessage.getString("message"));
                break;

            case "response_latest_messages":
                String latestMessages = jsonMessage.getString("messages");
                // we need to reverse the messages (so we have oldest->newest)
                String[] messages = latestMessages.split(";;");
                Collections.reverse(Arrays.asList(messages));
                ui.updateMessages(messages);
                break;

            case "response_all_topics":
                String allTopics = jsonMessage.getString("topics");
                ui.updateTopics(allTopics.split(";"));
                break;

            case "response_all_participants":
                String allParticipants = jsonMessage.getString("participants");
                ui.updateParticipants(allParticipants.split(";"));
                break;
        }
    }
}
