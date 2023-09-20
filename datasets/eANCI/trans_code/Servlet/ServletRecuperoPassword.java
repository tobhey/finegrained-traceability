package Servlet;

import java.io.IOException;
import java.util.Random;
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
 * The ServletRecoverPassword class manages the password recovery operation for a citizen The
 * ServletRecoverPassword class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletRecoverPassword extends HttpServlet {

  private String email;
  private String ci;
  private String login;
  private String type;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    RequestDispatcher rd = null;
    ServletContext sc = getServletContext();
    String ris;

    try {
      ci = request.getParameter("ci").toUpperCase();
      login = request.getParameter("login");
      type = "Citizen";

      CitizenManager CM = new CitizenManager();
      CIManager CIM = new CIManager();
      AccessManager AM = new AccessManager();
      IdentityCard CI = CIM.getCardByNumber(ci);

      if (CI != null) {
        if (AM.checkLogin(login)) {
          Access ac = AM.getAccess(login);
          Citizen c = CM.getCitizenById(CI.id());
          if (c.getLogin().equals(login)) {
            String p = generatePassword();  //nuova password auto-generateta
            ac.setPassword(p);
            AM.editAccess(login, ac);

            //instreetre l'email a c.getEmail();

            ris = "E' stata instreetta un email al suo streetAddress di posta elettronica";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=riuscita");
          } else {
            ris = "La login non corrisponde alla codice della carta";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/user/home.jsp?func=operazione&page=fallita");
          }
        } else {
          ris = "Siamo spiacenti, la login non� presente";
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
      rd = sc.getRequestDispatcher("/user/index.jsp?func=operazione&page=fallita");
      rd.forward(request, response);
    }
  }

  private static String generatePassword() {
    String pass = "";
    Random r = new Random();
    for (int i = 0; i < 8; i++) {
      int x = r.nextInt(10);   // generate un intero tra 0 e 9
      char c = (char) ((int) 'A' + r.nextInt(26));   // generate un char tra 'A' e 'Z
      boolean s = r.nextBoolean();
      if (s)
        pass = pass + c;
      else
        pass = pass + x;
    }
    return pass;
  }

}
