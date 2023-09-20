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
import Manager.CIManager;
import Manager.CitizenManager;
import Manager.FamilyManager;
import Bean.Access;
import Bean.IdentityCard;
import Bean.Citizen;
import Bean.Family;
import DB.DbException;

/**
 * The ServletDeleteCitizen class search and delete a citizen The ServletDeleteCitizen class has no
 * dependencies
 *
 * @author Federico Cinque
 */
public class ServletDeleteCitizen extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (session
        != null) {  //Se la sessione � nulla effettua il redirect alla pagina di autenticazione
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
        //Se gli attributi di sessione c e acc sono nulli devo effettuare la search
        if (session.getAttribute("c") == null && session.getAttribute("acc") == null) {
          String cod = request.getParameter("ci").toUpperCase();
          CIManager CIM = new CIManager();
          CitizenManager CM = new CitizenManager();
          IdentityCard CI = CIM.getCardByNumber(cod);

          if (CI != null) {
            Citizen c = CM.getCitizenById(CI.id());
            session.setAttribute("c", c);

            AccessManager AM = new AccessManager();
            Access ac = AM.getAccess(c.getLogin());
            session.setAttribute("acc", ac);

            FamilyManager NFM = new FamilyManager();
            int componenti = NFM.getNCoreComponents(c.getFamily());
            Family nf = NFM.getNucleo(c.getFamily());
            if (componenti > 1 && nf.getCapoFamiglia() == c.getIdCitizen()) {
              String nc = "si";
              session.setAttribute("newCapo", nc);
            }

            sc = getServletContext();
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=delete&page=citizen");
            rd.forward(request, response);
          } else {
            ris = "Siamo spiacenti, il codice della carta d'identit� non � presente nel database";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
          }
        } else {//Se gli attributi sono presenti procedo con la deletezione
          AccessManager AM = new AccessManager();
          CitizenManager CM = new CitizenManager();

          Access ac = (Access) session.getAttribute("acc");
          Citizen c = (Citizen) session.getAttribute("c");

          String login = ac.getLogin();

          if (request.getParameter("ci")
              .equals("")) {  //Se non c'� il codice della carta d'identit�
            //il citizen da deletere � solo nel nucleo familiare
            if (AM.deleteAccess(login) && CM.deleteCitizen(
                c.getIdCitizen())) {  //delete il citizen e l'access
              //controllando che l'esito sia positivo
              FamilyManager NFM = new FamilyManager();
              NFM.getNCoreComponents(c.getFamily());
              ris = "ok";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
            } else {
              ris = "fallita";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
            }
          } else {  //Se � presente un codice devo sostituire il capo famiglia
            FamilyManager NFM = new FamilyManager();
            CIManager CIM = new CIManager();
            IdentityCard CI = CIM.getCardByNumber(request.getParameter("ci"));
            if (CI != null) {  //Controllo che il nuovo capo famiglia esiste nel db
              Citizen newCapo = CM.getCitizenById(CI.id());
              NFM.setHeadOfFamily(c.getFamily(),
                  newCapo.getIdCitizen()); //modifico il capo famiglia del nucleo
              if (CM.deleteCitizen(c.getIdCitizen())) {//delete il citizen e l'access
                //controllando che l'esito sia positivo
                NFM.decrementaComponenti(
                    c.getFamily());  // Decremento il number di componenti del nucleo
                AM.deleteAccess(login);
                ris = "ok";
                request.setAttribute("ris", ris);
                rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
              } else {
                ris = "fallita";
                request.setAttribute("ris", ris);
                rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
              }
            } else {
              ris = "Siamo spiacenti, il codice della carta d'identit� del nuovo capo famiglia non � presente nel database";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
            }
          }
          rd.forward(request, response);
          session.removeAttribute("c");
          session.removeAttribute("acc");
          session.removeAttribute("newCapo");
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
