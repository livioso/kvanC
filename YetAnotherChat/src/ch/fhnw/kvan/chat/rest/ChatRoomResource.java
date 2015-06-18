package ch.fhnw.kvan.chat.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/hello")
public class ChatRoomResource {
    // private String lastTimestamp = "";
    private String lastTimestamp = String.valueOf(System.currentTimeMillis());

    @Path("/hi")
    @GET
    public Response getOrder(@Context Request request) {
        EntityTag tag = new EntityTag(lastTimestamp);

        ResponseBuilder builder = request.evaluatePreconditions(tag);
        CacheControl cc = new CacheControl();
        cc.setMaxAge(3000);

        if (builder != null)
            return builder.cacheControl(cc).build();
        String order = null;
        return Response.ok(order).cacheControl(cc).build();
    }
}
