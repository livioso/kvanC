package ch.fhnw.kvan.chat.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/hello")
public class ChatRoomResource {
    // private String lastTimestamp = "";
    private String lastTimestamp = String.valueOf(System.currentTimeMillis());

    @GET
    @Path("/hi")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
}
