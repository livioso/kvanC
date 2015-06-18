package ch.fhnw.kvan.chat.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Server {
    public static void main(String[] args) throws IOException {

        final String baseUri = "http://localhost:9998";

        final ResourceConfig rc = ResourceConfig
                .forApplication(new CustomerApplication());

        System.out.println("Starting Grizzly...");
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);

        System.out.println(String.format(
                "Jersey app started with WADL available at " +
                        "%sapplication.wadl\nTry out %s\nHit enter to stop it...",
                baseUri, baseUri));

        System.in.read();
        httpServer.shutdown();
}
