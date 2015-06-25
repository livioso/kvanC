package ch.fhnw.kvan.chat.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class Server extends Application {
    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> empty = new HashSet<>();

    public Server() {
        singletons.add(new ChatRoomResource());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    public static void main(String[] args) throws IOException {

        final String baseUri = "http://localhost:9998";

        final ResourceConfig rc = ResourceConfig
                .forApplication(new Server());

        System.out.println("Starting Grizzly...");
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);

        System.in.read();
        httpServer.shutdown();
    }
}
