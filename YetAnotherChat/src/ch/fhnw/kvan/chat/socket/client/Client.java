package ch.fhnw.kvan.chat.socket.client;

import javax.json.*;

import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ch.fhnw.kvan.chat.gui.ClientGUI;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

public class Client {

    private static Logger logger = Logger.getLogger(Client.class);

    // connection information
    private String username;
    private String hostname;
    private String portnumber;

    // socket and input & output streams
    private Socket clientSocket;
    private In clientInputStream;
    private Out clientOutputStream;

    // the client' chat room instance which handles message sending
    // The setupClientUserInterface() requires this to be initiated
    // before its call!
    ClientChatRoomMessagesSender clientChatRoomMessagesSender
        = new ClientChatRoomMessagesSender();

    ClientChatRoomMessagesReciever clientChatRoomMessagesReciever;

    // corresponding gui client instance
    private ClientGUI clientGui;

    /**
     * The main method; called via refection from RunClient.
     *
     * @param args In this order: Hostname Port[4-digits] Username
     */
    public static void main(String[] args) {

        try {

            if (args.length != 3) {
                throw new Exception("Expecting <host> <port> <username> parameters.");
            }

            // parameter preconditions are checked as well.
            Client aNewClient = new Client(args[0], args[1], args[2]);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public Client(String username, String hostname, String portnumber) throws Exception {
        this.username = username;
        this.hostname = hostname;
        this.portnumber = portnumber;

        logger.setLevel(Level.ALL);
        logger.info("Username is: " + this.username);
        logger.info("Hostname is: " + this.hostname);
        logger.info("Port number is: " + this.portnumber);

        if (!portnumber.matches("[1-9]{4}")) {
            throw new Exception("Port number is expected to be 4 digit number.");
        }

        setupSocketConnection();
        setupClientUserInterface();

        // --> NEEDS TO BE INSTANTIATED AFTER THE SOCKET SETUP!
        this.clientChatRoomMessagesReciever = new ClientChatRoomMessagesReciever();
    }

    private void setupSocketConnection() throws IOException {
        logger.info(username + " trying to connect to " + hostname + " on port " + portnumber);
        clientSocket = new Socket(hostname, Integer.parseInt(portnumber));

        clientInputStream = new In(clientSocket);
        clientOutputStream = new Out(clientSocket);

        logger.info("Client connection established.");
    }

    private void setupClientUserInterface() throws IOException{
        clientGui = new ClientGUI(
                clientChatRoomMessagesSender, username);
        clientChatRoomMessagesSender.addParticipant(username);
    }



    /**
     * Responsible for the outbound message to the server
     */
    private class ClientChatRoomMessagesSender implements IChatRoom {

        @Override
        public boolean addParticipant(String name) throws IOException {
            JsonObject addParticipantJson = Json.createObjectBuilder()
                    .add("action", "new_user")
                    .add("name", name).build();
            clientOutputStream.println(addParticipantJson.toString());

            clientGui.addParticipant(name);
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

            clientGui.addTopic(topic);
            return true;
        }

        @Override
        public boolean removeTopic(String topic) throws IOException {
            JsonObject removeTopicJson = Json.createObjectBuilder()
                    .add("action", "remove_topic")
                    .add("topic", topic).build();
            clientOutputStream.println(removeTopicJson.toString());

            clientGui.removeTopic(topic);
            return true;
        }

        @Override
        public boolean addMessage(String topic, String message) throws IOException {
            JsonObject addMessageToTopicJson = Json.createObjectBuilder()
                    .add("action", "add_message")
                    .add("message", message)
                    .add("topic", topic).build();
            clientOutputStream.println(addMessageToTopicJson.toString());

            clientGui.addMessage(message);
            return false;
        }

        @Override
        public String getMessages(String topic) throws IOException {
            return null;
        }

        @Override
        public String refresh(String topic) throws IOException {
            return null;
        }
    }



    /**
     * Responsible for receiving inbound messages from server
     */
    private class ClientChatRoomMessagesReciever implements Runnable {

        public ClientChatRoomMessagesReciever() {
            Thread listener = new Thread(this);
            listener.start();
        }

        @Override
        public void run() {
            listen();
        }

        private void listen() {
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
            }
        }
    }
}