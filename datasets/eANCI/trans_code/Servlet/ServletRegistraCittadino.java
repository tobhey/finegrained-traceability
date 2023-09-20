package Servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Manager.AccessManager;
import Manager.CIManager;
import Manager.CitizenManager;
import Bean.Access;
import Bean.IdentityCard;
import Bean.Citizen;
import DB.DbException;

/**
 * The ServletRegisterCitizen class handles the registration operation of a citizen in the system
 * The ServletRegisterCitizen class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletRegisterCitizen extends HttpServlet {
  private String name;
  private String surname;
  private String email;
  private String ci;
  private String cf;
  private String login;
  private String password;
  private String type;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    RequestDispatcher rd = null;
    ServletContext sc = getServletContext();
    String ris;
    try {
      name = request.getParameter("name");
      surname = request.getParameter("surname");
      email = request.getParameter("email");
      ci = request.getParameter("ci");
      cf = request.getParameter("cf").toUpperCase();
      login = request.getParameter("login");
      password = request.getParameter("password");
      type = "Citizen";

      CitizenManager CM = new CitizenManager();
      CIManager CIM = new CIManager();
      AccessManager AM = new AccessManager();
      IdentityCard CI = CIM.getCardByNumber(ci);

      if (CI != null) {
        if (!AM.checkLogin(login)) {
          Access ac = new Access(login, password, type);
          Citizen c = CM.getCitizenById(CI.id());
          if (c.getFiscalCode().equals(cf) && c.getSurname().equals(surname) && c.getName()
              .equals(name)) {
            if (AM.enterAccess(ac) && CM.modifyLogin(c.getIdCitizen(), login) && CM.modifyEmail(
                c.getIdCitizen(), email)) {
              ris = "ok";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=riuscita");
            } else {
              ris = "fallita";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=fallita");
            }
          } else {
            ris = "I dati inseriti non corrispondono";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=fallita");
          }
        } else {
          ris = "Siamo spiacenti, la login � gi� presente";
          request.setAttribute("ris", ris);
          rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=fallita");
        }
      } else {
        ris = "Siamo spiacenti, il codice della carta d'identit� non� presente nel database";
        request.setAttribute("ris", ris);
        rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=fallita");
      }
      rd.forward(request, response);
    } catch (DbException e) {
      ris = e.getMessage();
      request.setAttribute("ris", ris);
      rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=fallita");
      rd.forward(request, response);
    }
  }
}
