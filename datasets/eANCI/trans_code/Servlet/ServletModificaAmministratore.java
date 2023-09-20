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
import Manager.AdminManager;
import Bean.Access;
import Bean.Administrator;
import DB.DbException;

/**
 * The ServletEditAdministrator class that performs the modify operation of an administrator The
 * ServletEditAdministrator class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletEditAdministrator extends HttpServlet {

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
        != null) {  //Se la sessione � nulla effettua il redirect alla pagina di autenticazione
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
        //Se gli attributi di sessione amm e acc sono nulli devo effettuare la search
        if (session.getAttribute("amm") == null && session.getAttribute("acc") == null) {
          String number = request.getParameter("number");
          AdminManager AdM = new AdminManager();
          Administrator am = AdM.searchAdminByNumber(number);
          if (am != null) {
            session.setAttribute("amm", am);

            AccessManager AM = new AccessManager();
            Access ac = AM.getAccess(am.getLogin());
            session.setAttribute("acc", ac);

            rd = sc.getRequestDispatcher("/workers/index.jsp?func=modify&page=administrator");
            rd.forward(request, response);
          } else {
            ris = "La number non corrisponde ad un administrator";
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
          AdminManager AdM = new AdminManager();

          Access ac = new Access(login, password, type);
          Administrator am = new Administrator(name, surname, number, email, login);
          Administrator amOld = (Administrator) session.getAttribute("amm");

          if (AM.editAccess(amOld.getLogin(), ac) && AdM.editAdmin(amOld.getNumber(),
              am)) { //procedo con la modify dei dati
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
