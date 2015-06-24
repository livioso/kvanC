package ch.fhnw.kvan.chat.rest;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by livio on 16/06/15.
 */
public class Client implements IChatRoom {

    private final String username;
    private final ClientGUI ui;
    private final javax.ws.rs.client.Client client;
    private final String baseUri;
    private WebTarget target;

    public Client(String username, String baseUri) throws IOException, URISyntaxException {

        this.username = username;
        this.ui = new ClientGUI(this, username);
        this.client = ClientBuilder.newClient();
        this.baseUri = baseUri;

        doInitialUserInterfaceUpdate();
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            throw new Exception("Expecting <baseuri> <username> parameters.");
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
        target = client.target("http://" + baseUri + "/users/" + name);
        target.request().put(Entity.entity("", MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        return false;
    }

    @Override
    public boolean removeParticipant(String name) throws IOException {
        target = client.target("http://" + baseUri + "/users/" + name);
        target.request().delete();
        return false;
    }

    @Override
    public boolean addTopic(String topic) throws IOException {
        target = client.target("http://" + baseUri + "/topics/" + topic);
        target.request().put(Entity.entity("", MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        return false;
    }

    @Override
    public boolean removeTopic(String topic) throws IOException {
        target = client.target("http://" + baseUri + "/topics/" + topic);
        target.request().delete();
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
