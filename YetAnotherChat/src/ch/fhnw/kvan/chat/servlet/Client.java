package ch.fhnw.kvan.chat.servlet;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import javax.print.URIException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Client implements IChatRoom {

	private final String username;
	private final String host;
    private final ClientGUI ui;

    // base uri which can use for further
    // specification of the REST api call
    // http://localhost:8080/
    private final URI baseUri;

    // HTTP client used to make the calls
    private final HttpClient httpClient;

	public Client (String username, String host) throws IOException, URISyntaxException {
		this.username = username;
		this.host = host;
		this.ui = new ClientGUI(this, username);
        this.baseUri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .build();
        this.httpClient = HttpClientBuilder.create().build();

        doInitialUserInterfaceUpdate();
	}

	public static void main(String[] args) throws Exception
	{
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
    public boolean addParticipant(String name) throws IOException{
        try {
            URI uri = new URIBuilder(baseUri)
                    .setPath("/chatserver/api/v1/participant")
                    .setParameter("username", name)
                    .build();
            HttpPost request = new HttpPost(uri);
            HttpResponse response = httpClient.execute(request);
            EntityUtils.consume(response.getEntity());
        } catch (URISyntaxException ex) {}
        return false;
    }

    @Override
    public boolean removeParticipant(String name) throws IOException {
        try {
            URI uri = new URIBuilder(baseUri)
                    .setPath("/chatserver/api/v1/participant")
                    .setParameter("username", name)
                    .build();
            HttpDelete request = new HttpDelete(uri);
            HttpResponse response = httpClient.execute(request);
            EntityUtils.consume(response.getEntity());
        } catch (URISyntaxException ex) {}
        return false;
    }

    @Override
    public boolean addTopic(String topic) throws IOException {
        try {
            URI uri = new URIBuilder(baseUri)
                    .setPath("/chatserver/api/v1/topic")
                    .setParameter("name", topic)
                    .build();
            HttpPost request = new HttpPost(uri);
            HttpResponse response = httpClient.execute(request);
            EntityUtils.consume(response.getEntity());
        } catch (URISyntaxException ex) {}
        return false;
    }

    @Override
    public boolean removeTopic(String topic) throws IOException {
        try {
            URI uri = new URIBuilder(baseUri)
                    .setPath("/chatserver/api/v1/topic")
                    .setParameter("name", topic)
                    .build();
            HttpDelete request = new HttpDelete(uri);
            HttpResponse response = httpClient.execute(request);
            EntityUtils.consume(response.getEntity());
        } catch (URISyntaxException ex) {}
        return false;
    }

    @Override
    public boolean addMessage(String topic, String message) throws IOException {
        try {
            URI uri = new URIBuilder(baseUri)
                    .setPath("/chatserver/api/v1/message")
                    .setParameter("topic", topic)
                    .setParameter("message", message)
                    .build();
            HttpPost request = new HttpPost(uri);
            HttpResponse response = httpClient.execute(request);
            EntityUtils.consume(response.getEntity());
        } catch (URISyntaxException ex) {}
        return false;
    }

    @Override
    public String getMessages(String topic) throws IOException {
        String messages = "";
        try {
            URI uri = new URIBuilder(baseUri)
                    .setPath("/chatserver/api/v1/message")
                    .setParameter("topic", topic)
                    .build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = httpClient.execute(request);
            messages = EntityUtils.toString(response.getEntity(), "US-ASCII");
            EntityUtils.consume(response.getEntity());
        } catch (URISyntaxException ex) {
           ex.printStackTrace();
        } finally {
            ui.updateMessages(messages.split(";;"));
        }

        return "";
    }

    @Override
    public String refresh(String topic) throws IOException {
        getExistingParticipants();
        getExistingTopics();
        getMessages(topic);
        return "";
    }

    public void getExistingTopics() throws IOException {
        String topics = "";
        try {
            URI uri = new URIBuilder(baseUri)
                    .setPath("/chatserver/api/v1/topic")
                    .build();

            HttpGet request = new HttpGet(uri);
            HttpResponse response = httpClient.execute(request);
            topics = EntityUtils.toString(response.getEntity(), "US-ASCII");
            EntityUtils.consume(response.getEntity());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        finally {
            ui.updateTopics(topics.split(";"));
        }
    }

    public void getExistingParticipants() throws IOException {
        String participants = "";
        try {
            URI uri = new URIBuilder(baseUri)
                    .setPath("/chatserver/api/v1/participant")
                    .build();

            HttpGet request = new HttpGet(uri);
            HttpResponse response = httpClient.execute(request);
            participants = EntityUtils.toString(response.getEntity(), "US-ASCII");
            EntityUtils.consume(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ui.updateParticipants(participants.split(";"));
        }
    }
}
