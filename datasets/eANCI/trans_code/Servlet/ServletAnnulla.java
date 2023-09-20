package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The ServletCancel class cancels the operation the user was performing The ServletCancel class has
 * no dependencies
 *
 * @author Federico Cinque
 */
public class ServletCancel extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    session.removeAttribute("amm");
    session.removeAttribute("acc");
    session.removeAttribute("c");
    session.removeAttribute("newCapo");
    session.removeAttribute("city");

    String url = "/myDoc/workers/index.jsp";
    response.sendRedirect(url);
  }
}
