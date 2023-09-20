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
 * The ServletInsertEmployee class inserts an employee into the database The ServletInsertEmployee
 * class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletInsertEmployee extends HttpServlet {
  private String name;
  private String surname;
  private String email;
  private String number;
  private String login;
  private String password;
  private String type;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    ServletContext sc = getServletContext();
    RequestDispatcher rd = null;
    String ris;
    if (session
        != null) {  //Se la sessione � nulla effettua il redirect alla pagina di autenticazione
      try {
        name = request.getParameter("name");
        surname = request.getParameter("surname");
        email = request.getParameter("email");
        number = request.getParameter("number");
        login = request.getParameter("login");
        password = request.getParameter("password");
        type = request.getParameter("type");

        AccessManager AM = new AccessManager();
        EmployeeManager IdM = new EmployeeManager();

        Access ac = new Access(login, password, type);
        Employee am = new Employee(name, surname, number, email, login);

        if (AM.enterAccess(ac) && IdM.insertEmployee(
            am)) {  //inserisco idati relativi all'access e all'employee
          //controllando l'esito positivo
          ris = "ok";
          request.setAttribute("ris", ris);
          rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");

        } else {
          ris = "fallita";
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