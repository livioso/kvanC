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
        String users = theChatRoom.getParticipants();
        users = users.replaceFirst("participants=", "");

        // there is a trailing ; at the very end of the string => not needed
        if (!users.isEmpty()) {
            users.substring(0, users.length() - 1);
        }

        return users;
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
        String topics = theChatRoom.getTopics().toString();

        topics = topics.replaceFirst("topics=", "");
        // there is a trailing ; at the very end of the string => not needed
        if (!topics.isEmpty()) {
            topics.substring(0, topics.length() - 2);
        }

        return topics;
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
    @Consumes("application/x-www-form-urlencoded")
    public void addMessage(@PathParam("topicName") String topicName, @FormParam("message") String message) throws IOException {
        theChatRoom.addMessage(topicName, message);
    }

    @GET
    @Path("/messages/{topicName}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessages(@PathParam("topicName") String topicName) throws IOException {

        String messages = theChatRoom.getMessages(topicName);
        messages = messages.replaceFirst("messages=", "");

        // there is a trailing ; at the very end of the string => not needed
        if (!messages.isEmpty()) {
            messages.substring(0, messages.length() - 2);
        }

        return messages;
    }
}
