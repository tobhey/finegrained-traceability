package Servlet;

import Bean.*;
import DB.*;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The ServletStateOfFamily class returns a list of the members of a family The class depends on the
 * DbFamily class
 *
 * @author Christian Ronca
 */

public class ServletStateOfFamily extends HttpServlet {
  private static final long serialVersionUID = -6835425792119775069L;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int id = Integer.parseInt(request.getParameter("id"));
    //int id = 200101;

    HttpSession session = request.getSession(true);
    ArrayList<Citizen> arrayList = null;
    //Request ric = null;

    try {
      DbFamily dbnc = new DbFamily();
      arrayList = (ArrayList<Citizen>) dbnc.getFamilyStatus(id);
      for (int i = 0; i < arrayList.size(); i++) {
        System.out.println(arrayList.get(i).getName());
      }
      session.setAttribute("array", arrayList);
    } catch (Exception e) {
      //e.getMessage().toString();
      e.printStackTrace();
    }

    ServletContext sc = getServletContext();
    RequestDispatcher rd = sc.getRequestDispatcher("/user/home.jsp?func=serv&page=state_famiglia");
    rd.forward(request, response);
  }
}
