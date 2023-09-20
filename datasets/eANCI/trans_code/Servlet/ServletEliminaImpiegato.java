package Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Manager.AccessManager;
import Manager.EmployeeManager;
import Bean.Access;
import Bean.Employee;
import DB.DbException;

/**
 * The ServletDeleteEmployee class search and delete an employee The ServletDeleteEmployee class has
 * no dependencies
 *
 * @author Federico Cinque
 */
public class ServletDeleteEmployee extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (session
        != null) {  //Se la sessione � nulla effettua il redirect alla pagina di autenticazione
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
        //Se gli attributi di sessione amm e acc sono nulli devo effettuare la search
        if (session.getAttribute("amm") == null && session.getAttribute("acc") == null) {
          String number = (String) request.getParameter("number");
          EmployeeManager IM = new EmployeeManager();
          Employee imp = IM.searchEmployeeByNumber(number);
          if (imp != null) {
            session.setAttribute("amm", imp);

            AccessManager AM = new AccessManager();
            Access ac = AM.getAccess(imp.getLogin());
            session.setAttribute("acc", ac);

            rd = sc.getRequestDispatcher("/workers/index.jsp?func=delete&page=employee");
            rd.forward(request, response);
          } else {
            ris = "La number non corrisponde ad un employee";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
            rd.forward(request, response);
          }
        } else {  //Se gli attributi sono presenti procedo con la deletezione

          AccessManager AM = new AccessManager();
          EmployeeManager IM = new EmployeeManager();

          Employee imp = (Employee) session.getAttribute("amm");

          String number = imp.getNumber();
          String login = imp.getLogin();

          if (IM.deleteEmployee(number) && AM.deleteAccess(login)) { //delete l'employee e l'access
            //controllando che l'esito sia positivo
            ris = "ok";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
          } else {
            ris = "fallita";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
          }

          rd.forward(request, response);
          session.removeAttribute("amm");
          session.removeAttribute("acc");
        }
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
