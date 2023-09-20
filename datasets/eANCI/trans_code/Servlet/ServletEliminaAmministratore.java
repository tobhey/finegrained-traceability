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
 * The ServletDeleteAdministrator class search and delete an administrator The
 * ServletDeleteAdministrator class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletDeleteAdministrator extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (session
        != null) { //Se la sessione � nulla effettua il redirect alla pagina di autenticazione
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

            rd = sc.getRequestDispatcher("/workers/index.jsp?func=delete&page=administrator");
            rd.forward(request, response);
          } else {
            ris = "La number non corrisponde ad un administrator";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
            rd.forward(request, response);
          }
        } else {  //Se gli attributi sono presenti procedo con la deletezione

          AccessManager AM = new AccessManager();
          AdminManager AdM = new AdminManager();

          Access ac = (Access) session.getAttribute("acc");
          Administrator am = (Administrator) session.getAttribute("amm");

          String number = am.getNumber();
          String login = ac.getLogin();

          String risCanc = AdM.deleteAdministrator(number);  //provo ad effettuare la deletezione

          if (risCanc.equals(
              "ok")) { // controllo che l'administrator non � unique ed � state deleteto
            if (AM.deleteAccess(login)) { //delete l'access corrspondente
              ris = "ok";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
            } else {
              ris = "fallita";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
            }
          } else {
            if (risCanc.equals("unique")) //se l'administrator � unique non � state deleteto
              ris = "Non si pu� deletere l'ultimo administrator";
            else
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
