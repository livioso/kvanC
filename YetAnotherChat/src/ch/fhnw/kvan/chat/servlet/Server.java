package ch.fhnw.kvan.chat.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import ch.fhnw.kvan.chat.general.ChatRoom;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

public abstract class Server {

    private static ChatRoom theChatRoom = ChatRoom.getInstance();

    // Controller responsible for handling participants:
    // POST: /chatserver/api/v1/participant?username=X => add participant X
    // DELETE: /chatserver/api/v1/participant?username=X => remove participant X
    // GET: /chatserver/api/v1/participant => get list of all participants
    @WebServlet("/chatserver/api/v1/participant")
    public static class ChatParticipantController extends HttpServlet {

        public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
            final String username = req.getParameter("username");
            if (username != null) {
                theChatRoom.addParticipant(username);
                res.setStatus(200);
            } else {
                res.sendError(400);
            }
        }

        public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
            final String username = req.getParameter("username");
            if (username != null) {
                theChatRoom.removeParticipant(username);
                res.setStatus(200);
            } else {
                res.sendError(400);
            }
        }

        public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            PrintWriter out = res.getWriter();
            out.println(theChatRoom.getParticipants());
            out.close();
        }
    }

    // Controller responsible for handling topics:
    // POST: /chatserver/api/v1/topic?name=X => add topic X
    // DELETE: /chatserver/api/v1/topic?name=X => remove topic X
    // GET: /chatserver/api/v1/topic => get list of all topics
    @WebServlet("/chatserver/api/v1/topic")
    public static class ChatTopicController extends HttpServlet {

        public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
            final String topic = req.getParameter("name");
            if (topic != null) {
                theChatRoom.addTopic(topic);
                res.setStatus(200);
            } else {
                res.sendError(400);
            }
        }

        public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
            final String topic = req.getParameter("name");
            if (topic != null) {
                theChatRoom.removeTopic(topic);
                res.setStatus(200);
            } else {
                res.sendError(400);
            }
        }

        public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            PrintWriter out = res.getWriter();
            out.println(theChatRoom.getTopics());
            out.close();
        }
    }

    // Controller responsible for handling messages:
    // POST: /chatserver/api/v1/message?topic=X&message=Y => add message Y to topic X
    // GET: /chatserver/api/v1/message?topic=X => get last message of topic X
    @WebServlet("/chatserver/api/v1/message")
    public static class ChatMessageController extends HttpServlet {

        public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
            final String topic = req.getParameter("topic");
            final String message = req.getParameter("message");
            if (topic != null && message != null) {
                theChatRoom.addMessage(topic, message);
                res.setStatus(200);
            } else {
                res.sendError(400);
            }
        }

        public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
            final String topic = req.getParameter("topic");
            if(topic != null) {
                PrintWriter out = res.getWriter();
                out.println(theChatRoom.getMessages(topic));
                out.close();
            } else {
                res.sendError(400);
            }
        }
    }
}