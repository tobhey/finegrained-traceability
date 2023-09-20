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
 * The ServletEnterAdministrator class inserts an administrator into the database The
 * ServletEnterAdministrator class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletEnterAdministrator extends HttpServlet {
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
        != null) {  //Se la sessione + nulla effettua il redirect alla pagina di autenticazione
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
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

        rd = null;
        sc = getServletContext();

        if (AM.enterAccess(ac) && AdM.enterAdmin(
            am)) { //inserisco idati relativi all'access e all'administrator
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
