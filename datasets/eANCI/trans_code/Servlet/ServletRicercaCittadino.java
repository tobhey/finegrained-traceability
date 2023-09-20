package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.IdentityCard;
import Bean.Citizen;
import DB.DbException;
import Manager.CIManager;
import Manager.CitizenManager;

/**
 * The ServletSearchCitizen class searches and returns citizen data The ServletSearchCitizen class
 * has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletSearchCitizen extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    Citizen c = null;
    ServletContext sc = getServletContext();
    RequestDispatcher rd = null;
    String ris;
    if (session != null) {
      try {
        CitizenManager CM = new CitizenManager();
        if (request.getParameter("ci") != null) {
          String cod = request.getParameter("ci").toUpperCase();
          CIManager CIM = new CIManager();
          IdentityCard CI = CIM.getCardByNumber(cod);
          if (CI != null) {
            c = CM.getCitizenById(CI.id());
            session.setAttribute("ci", c);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=mostra&page=risultati");
          } else {
            ris = "Siamo spiacenti, il codice della carta d'identit� non� presente nel database";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=mostra&page=risultati");
          }
        } else {
          String name = request.getParameter("name");
          String surname = request.getParameter("surname");
          ArrayList<Citizen> cityadini = (ArrayList<Citizen>) CM.searchCitizen(name, surname);
          if (cityadini.size() > 0) {
            request.setAttribute("ris", cityadini);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=mostra&page=risultati");
          } else {
            ris = "Siamo spiacenti, nessun risultato";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
          }
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
