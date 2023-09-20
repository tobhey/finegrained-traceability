package Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.Employee;
import DB.DbException;
import Manager.EmployeeManager;

/**
 * The ServletSearchEmployee class searches and returns the data of an employee The
 * ServletSearchEmployee class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletSearchEmployee extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (session != null) {
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
        String number = request.getParameter("number");
        EmployeeManager IdM = new EmployeeManager();
        Employee I = IdM.searchEmployeeByNumber(number);

        sc = getServletContext();
        rd = null;

        if (I != null) {
          request.setAttribute("ris", I);
          rd = sc.getRequestDispatcher("/workers/index.jsp?func=mostra&page=datiI");
        } else {
          ris = "Employee non trovato";
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
