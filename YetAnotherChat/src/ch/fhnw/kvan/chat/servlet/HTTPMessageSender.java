package ch.fhnw.kvan.chat.servlet;

import ch.fhnw.kvan.chat.interfaces.IChatRoom;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;

public class HTTPMessageSender implements IChatRoom {

    String host;

    HttpClient httpClient = new DefaultHttpClient();

    public HTTPMessageSender(String host) {
        this.host = host;
    }

    @Override
    public boolean addParticipant(String name) throws IOException{

       try {
           URI uri = new URIBuilder()
                   .setScheme("http")
                   .setHost(host)
                   .setPath("/api/user")
                   .setParameter("action", "new_user")
                   .setParameter("name", name)
                   .build();
           HttpGet request = new HttpGet(uri);
           HttpResponse res = httpClient.execute(request);
       } catch (URISyntaxException ex) {

       }

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
