package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.*;
import DB.*;

/**
 * The ServletViewPractices class returns a list of requests The class depends on the DbRequests
 * class
 *
 * @author Christian Ronca
 */

public class ServletViewPractices extends HttpServlet {
  private static final long serialVersionUID = -6835425792119775069L;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    ArrayList<Request> arrayList = null;

    try {
      DbRequest dbnc = new DbRequest();
      arrayList = (ArrayList<Request>) dbnc.getRequests();
      session.setAttribute("array", arrayList);
    } catch (Exception e) {
      //e.getMessage().toString();
      e.printStackTrace();
    }

    session.setAttribute("array", arrayList);
    ServletContext sc = getServletContext();
    RequestDispatcher rd = sc.getRequestDispatcher("/workers/index.jsp?func=pra&page=visualizza");
    rd.forward(request, response);
  }
}
