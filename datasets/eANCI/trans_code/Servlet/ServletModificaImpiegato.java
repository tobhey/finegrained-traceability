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
 * The ServletEditEmployee class that performs the modify operation of an employee The
 * ServletEditEmployee class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletEditEmployee extends HttpServlet {

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
    if (session
        != null) {  //Se la sessione� nulla effettua il redirect alla pagina di autenticazione
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
        //Se gli attributi di sessione amm e acc sono nulli devo effettuare la search
        if (session.getAttribute("amm") == null && session.getAttribute("acc") == null) {
          number = request.getParameter("number");
          EmployeeManager IM = new EmployeeManager();
          Employee imp = IM.searchEmployeeByNumber(number);
          if (imp != null) {
            session.setAttribute("amm", imp);

            AccessManager AM = new AccessManager();
            Access ac = AM.getAccess(imp.getLogin());
            session.setAttribute("acc", ac);

            rd = sc.getRequestDispatcher("/workers/index.jsp?func=modify&page=employee");
            rd.forward(request, response);
          } else {
            ris = "La number non corrisponde ad un employee";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
            rd.forward(request, response);
          }
        } else {
          name = request.getParameter("name");
          surname = request.getParameter("surname");
          email = request.getParameter("email");
          number = request.getParameter("number");
          login = request.getParameter("login");
          password = request.getParameter("password");
          type = request.getParameter("type");

          AccessManager AM = new AccessManager();
          EmployeeManager IM = new EmployeeManager();

          Access ac = new Access(login, password, type);
          Employee imp = new Employee(name, surname, number, email, login);
          Employee impOld = (Employee) session.getAttribute("amm");

          if (AM.editAccess(impOld.getLogin(), ac) && IM.modifyEmployee(impOld.getNumber(),
              imp)) {  //procedo con la modify dei dati
            //controllando l'esito
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
