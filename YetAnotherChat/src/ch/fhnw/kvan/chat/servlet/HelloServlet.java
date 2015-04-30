package ch.fhnw.kvan.chat.servlet;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;

@WebServlet("/report")
public class HelloServlet extends HttpServlet {
  public void doGet (HttpServletRequest req,
                     HttpServletResponse res)
    throws ServletException, IOException
  {
    PrintWriter out = res.getWriter();

    out.println("Hello, world!");
    out.close();
  }
}