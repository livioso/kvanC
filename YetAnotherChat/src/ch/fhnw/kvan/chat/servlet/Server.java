package ch.fhnw.kvan.chat.servlet;

import java.io.*;
import ch.fhnw.kvan.chat.general.ChatRoom;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;



public abstract class Server {

    private static ChatRoom theChatRoom = ChatRoom.getInstance();

    @WebServlet("/chatserver/api/v1/participant")
    public static class ChatServerParticipantController extends HttpServlet {

        public void doPost(HttpServletRequest req, HttpServletResponse res)
                throws ServletException, IOException {

            final String username = req.getParameter("username");
            if (username != null) {
                theChatRoom.addParticipant(username);
                res.setStatus(200);
            } else {
                res.sendError(400);
            }
        }

        public void doDelete(HttpServletRequest req, HttpServletResponse res)
                throws ServletException, IOException {

            final String username = req.getParameter("username");
            if (username != null) {
                theChatRoom.removeParticipant(username);
                res.setStatus(200);
            } else {
                res.sendError(400);
            }
        }

        public void doGet(HttpServletRequest req, HttpServletResponse res)
                throws ServletException, IOException {

            PrintWriter out = res.getWriter();
            out.println(theChatRoom.getParticipants());
            out.close();
        }
    }

    @WebServlet("/chatserver/api/v1/chatroom")
    public static class ChatServerChatroomController extends HttpServlet {
        public void doGet (HttpServletRequest req, HttpServletResponse res)
                throws ServletException, IOException {

            PrintWriter out = res.getWriter();
            out.println("Hello chatroom!");
            out.close();
        }
    }
}