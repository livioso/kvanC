package ch.fhnw.kvan.chat.rest;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by livio on 16/06/15.
 */
public class Client implements IChatRoom {

    private final String username;
    private final String host;
    private final ClientGUI ui;

    // base uri which can use for further
    // specification of the REST api call
    // http://localhost:8080/
    private final String baseUri;

    public Client(String username, String host) throws IOException, URISyntaxException {
        this.username = username;
        this.host = host;
        this.ui = new ClientGUI(this, username);
        this.baseUri = "";

        doInitialUserInterfaceUpdate();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Expecting <host> <username> parameters.");
        } else {
            new Client(args[0], args[1]);
        }
    }

    /**
     * Announce client and update ui with existing topics and participants
     */
    private void doInitialUserInterfaceUpdate() throws IOException {
        addParticipant(username); // announce himself
        getExistingTopics();
        getExistingParticipants();
    }

    @Override
    public boolean addParticipant(String name) throws IOException {
        return false;
    }

    @Override
    public boolean removeParticipant(String name) throws IOException {
        return false;
    }

    @Override
    public boolean addTopic(String topic) throws IOException {
        return false;
    }

    @Override
    public boolean removeTopic(String topic) throws IOException {
        return false;
    }

    @Override
    public boolean addMessage(String topic, String message) throws IOException {
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

    public void getExistingTopics() {

    }

    public void getExistingParticipants() {

    }
}
