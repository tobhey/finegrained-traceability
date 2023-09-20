package Servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.Access;
import DB.DbException;
import Bean.Citizen;
import Manager.AccessManager;

/**
 * The ServletShowAccessA class shows access data for a citizen The ServletShowAccessA class has no
 * dependencies
 *
 * @author Federico Cinque
 */
public class ServletShowAccessA extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    if (session != null) {
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
        Access ac = null;
        AccessManager AM = new AccessManager();
        String login;
        if (session.getAttribute("c") != null) {
          Citizen c = (Citizen) session.getAttribute("c");
          login = c.getLogin();
          ac = AM.getAccess(login);
          rd = sc.getRequestDispatcher("/user/home.jsp?func=mostra&page=access");
        }
        request.setAttribute("access", ac);
        rd.forward(request, response);
      } catch (DbException e) {
        ris = e.getMessage();
        request.setAttribute("ris", ris);
        rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=fallita");
        rd.forward(request, response);
      }
    } else {
      String url;
      url = "/myDoc/user/home.jsp";
      response.sendRedirect(url);
    }
  }
}
