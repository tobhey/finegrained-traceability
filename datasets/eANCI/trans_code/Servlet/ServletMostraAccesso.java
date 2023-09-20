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
import Manager.AccessManager;

/**
 * The ServletShowAccess class shows the access data of an employee or administrator The
 * ServletShowAccess class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletShowAccess extends HttpServlet {
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

        login = (String) session.getAttribute("login");
        ac = AM.getAccess(login);

        //instreetre i dati
        rd = sc.getRequestDispatcher("/workers/index.jsp?func=mostra&page=access");

        request.setAttribute("access", ac);
        rd.forward(request, response);
      } catch (DbException e) {
        ris = e.getMessage();
        request.setAttribute("ris", ris);
        rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
        rd.forward(request, response);
      }
    } else {
      String url;
      url = "/myDoc/workers/Access.jsp";
      response.sendRedirect(url);
    }
  }
}
