package ch.fhnw.kvan.chat.socket.client;

import ch.fhnw.kvan.chat.gui.ClientGUI;
import ch.fhnw.kvan.chat.utils.In;
import ch.fhnw.kvan.chat.utils.Out;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * The Client is responsible for implementing the main
 * method which is called by RunClient (using reflection).
 * <p>
 * The key point to check out here are the two members
 * messageSender and messagesReceiver which implement
 * the message handling for sending and respectively
 * receiving messages from / to the server .
 */
public class Client {

    private static Logger logger = Logger.getLogger(Client.class);

    // server connection information
    private String username;
    private String hostname;
    private String portnumber;

    // socket and input & output streams
    private Socket socket;
    private In inputStream;
    private Out outputStream;

    // client message handling
    ClientMessageSender messageSender;
    ClientMessageReceiver messagesReceiver;

    // corresponding gui client instance
    private ClientGUI ui;

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

        // make sure sockets & streams are set up at this point we depend on working streams.
        this.messageSender = new ClientMessageSender(outputStream, username);
        this.ui = new ClientGUI(messageSender, username);
        this.messagesReceiver = new ClientMessageReceiver(inputStream, ui);

        doInitialUserInterfaceUpdate();
    }

    private void setupSocketAndIOStreams() throws IOException {
        logger.info(username + " trying to connect to " + hostname + " on port " + portnumber);
        socket = new Socket(hostname, Integer.parseInt(portnumber));
        inputStream = new In(socket);
        outputStream = new Out(socket);
        logger.info("Client connection established.");
    }

    /**
     * Announce client and update ui with existing topics and participants
     */
    private void doInitialUserInterfaceUpdate() throws IOException {
        messageSender.addParticipant(username); // announce himself
        messageSender.getExistingTopics();
        messageSender.getExistingParticipants();
    }

    /*
     * See Client class comment for more information.
     *
     * @param args In this order: [Username] [Hostname] [Port[4-digits]]
     */
    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                throw new Exception("Expecting <host> <port> <username> parameters.");
            }

            new Client(args[0], args[1], args[2]); // parameters are checked.

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
