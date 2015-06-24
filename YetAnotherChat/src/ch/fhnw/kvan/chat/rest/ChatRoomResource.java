package ch.fhnw.kvan.chat.rest;

import ch.fhnw.kvan.chat.general.ChatRoom;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;

@Path("/chat/chatRoom")
public class ChatRoomResource {

    ChatRoom theChatRoom = ChatRoom.getInstance();

    @GET
    @Path("/users")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUsers() throws IOException {
        return theChatRoom.getParticipants().toString();
    }

    @PUT
    @Path("/users/{username}")
    public void addUser(@PathParam("username") String username) throws IOException {
        theChatRoom.addParticipant(username);
    }

    @DELETE
    @Path("/users/{username}")
    public void removeUser(@PathParam("username") String username) throws IOException {
        theChatRoom.removeParticipant(username);
    }

    @GET
    @Path("/topics")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTopics() throws IOException {
        return theChatRoom.getTopics().toString();
    }

    @PUT
    @Path("/topics/{topicName}")
    public void addTopic(@PathParam("topicName") String topicName) throws IOException {
        theChatRoom.addTopic(topicName);
    }

    @DELETE
    @Path("/topics/{topicName}")
    public void removeTopic(@PathParam("topicName") String topicName) throws IOException {
        theChatRoom.removeTopic(topicName);
    }

    @POST
    @Path("/messages/{topicName}")
    public void addMessage(@PathParam("topicName") String topicName, @FormParam("message") String message) throws IOException {
        theChatRoom.addMessage(topicName, message);
    }

    @GET
    @Path("/messages/{topicName}")
    public String getMessages(@PathParam("topicName") String topicName) throws IOException {
        return theChatRoom.getMessages(topicName);
    }
}
