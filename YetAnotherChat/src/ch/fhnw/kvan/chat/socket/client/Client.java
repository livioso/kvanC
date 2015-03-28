package ch.fhnw.kvan.chat.socket.client;

import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import ch.fhnw.kvan.chat.gui.ClientGUI;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private static Logger logger = Logger.getLogger(Client.class);

    // connection information
    private String username;
    private String hostname;
    private String portnumber;

    // socket and input & output streams
    private Socket clientSocket;
    private In clientInputStream;
    private Out clientOutputStream;

    ClientMessageSender clientMessageSender;
    ClientMessageReceiver clientMessagesReceiver;

    // corresponding gui client instance
    private ClientGUI clientGui;

    /**
     * The main method; called via refection from RunClient.
     * @param args In this order: [Hostname] [Port[4-digits]] [Username]
     */
    public static void main(String[] args) {

        try {

            if (args.length != 3) {
                throw new Exception("Expecting <host> <port> <username> parameters.");
            }

            // parameter preconditions are checked as well.
            new Client(args[0], args[1], args[2]);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public Client(String username, String hostname, String portnumber) throws Exception {
        this.username = username;
        this.hostname = hostname;
        this.portnumber = portnumber;

        logger.setLevel(Level.ALL);
        logger.info("Username is: " + this.username);
        logger.info("Hostname is: " + this.hostname);
        logger.info("Port number is: " + this.portnumber);

        if (!portnumber.matches("[1-9]{4}")) {
            throw new Exception("Port number is expected to be 4 digit number.");
        }

        setupSocketAndIOStreams();

        // make sure sockets are set up at this point! We depend on working streams.
        this.clientMessageSender = new ClientMessageSender(clientOutputStream, username);
        this.clientGui = new ClientGUI(clientMessageSender, username);
        this.clientMessagesReceiver = new ClientMessageReceiver(clientInputStream, clientGui);

        doInitialUserInterfaceUpdate();
    }

    private void setupSocketAndIOStreams() throws IOException {
        logger.info(username + " trying to connect to " + hostname + " on port " + portnumber);
        clientSocket = new Socket(hostname, Integer.parseInt(portnumber));
        clientInputStream = new In(clientSocket);
        clientOutputStream = new Out(clientSocket);
        logger.info("Client connection established.");
    }

    /* Announce client and update ui with existing topics and participants */
    private void doInitialUserInterfaceUpdate() throws IOException {
        clientMessageSender.addParticipant(username); // himself
        clientMessageSender.getExistingTopics();
        clientMessageSender.getExistingParticipants();
    }
}