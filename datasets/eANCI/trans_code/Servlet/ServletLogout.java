package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.Citizen;

/**
 * The ServletLogin class performs the authentication operation of a user on the system The
 * ServletLogin class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletLogout extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String url;
    Citizen c = (Citizen) session.getAttribute("c");

    if (c != null)
      url = "/myDoc/user/home.jsp";
    else
      url = "/myDoc/workers/Access.jsp";

    session.invalidate();
    response.sendRedirect(url);
  }
}
