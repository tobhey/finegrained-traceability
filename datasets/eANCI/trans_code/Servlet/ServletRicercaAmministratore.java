package Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.Administrator;
import DB.DbException;
import Manager.AdminManager;

/**
 * The ServletSearchAdministrator class searches and returns the data of an administrator The
 * ServletSearchAdministrator class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletSearchAdministrator extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (session != null) {
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
        String number = request.getParameter("number");
        AdminManager AdM = new AdminManager();
        Administrator A = AdM.searchAdminByNumber(number);

        if (A != null) {
          request.setAttribute("ris", A);
          rd = sc.getRequestDispatcher("/workers/index.jsp?func=mostra&page=datiA");
        } else {
          ris = "Administrator non trovato";
          request.setAttribute("ris", ris);
          rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
        }
        rd.forward(request, response);
      } catch (DbException e) {
        ris = e.getMessage();
        request.setAttribute("ris", ris);
        rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
        rd.forward(request, response);
      }
    } else {
      String url = "/myDoc/workers/Access.jsp";
      response.sendRedirect(url);
    }
  }
}