package Servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.IdentityCard;
import Bean.Citizen;
import Bean.Family;
import DB.DbException;
import Manager.CIManager;
import Manager.CitizenManager;
import Manager.FamilyManager;

/**
 * The ServletEditCitizen class that performs the modify operation of a citizen The
 * ServletEditCitizen class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletEditCitizen extends HttpServlet {

  private String name;
  private String surname;
  private String email;
  private int idNF;
  private Citizen citizen;
  private FamilyManager NFM;
  private CitizenManager CM;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (session
        != null) {  //Se la sessione � nulla effettua il redirect alla pagina di autenticazione
      ServletContext sc = getServletContext();
      RequestDispatcher rd = null;
      String ris;
      try {
        //Se l'attributo di sessione city� nullo devo effettuare la search
        if (session.getAttribute("city") == null) {
          CitizenManager CM = new CitizenManager();
          Citizen c = null;
          String cod = request.getParameter("ci").toUpperCase();
          CIManager CIM = new CIManager();
          CM = new CitizenManager();
          IdentityCard CI = CIM.getCardByNumber(cod);

          if (CI != null) {
            c = CM.getCitizenById(CI.id());

            FamilyManager NFM = new FamilyManager();
            int componenti = NFM.getNCoreComponents(c.getFamily());
            Family nf = NFM.getNucleo(c.getFamily());
            if (componenti > 1 && nf.getCapoFamiglia() == c.getIdCitizen()) {
              String nc = "si";
              session.setAttribute("newCapo", nc);
            }

            session.setAttribute("city", c);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=modify&page=citizen");
          } else {
            ris = "Siamo spiacenti, il codice della carta d'identit� non� presente nel database";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
          }
          rd.forward(request, response);
        } else {
          name = request.getParameter("name");
          surname = request.getParameter("surname");
          email = request.getParameter("email");
          if (request.getParameter("email") != null)
            email = request.getParameter("email");
          else
            email = "";
          idNF = Integer.parseInt(request.getParameter("nucleof"));

          CM = new CitizenManager();
          NFM = new FamilyManager();

          citizen = (Citizen) session.getAttribute("city");
          if (idNF
              == 0) {  //Se l'id del nucleo� zero devo creare un nuovo nucleo familiare per il citizen
            if (NFM.getNucleo(citizen.getFamily()).getCapoFamiglia() != citizen.getIdCitizen()) {
              NFM.decrementaComponenti(citizen.getFamily());
              idNF = creaNucleoF();  //Salvo l'id del nuovo nucleo
              citizen.setFamily(idNF);  //setto l'id del nucleo del citizen
              //effettuo le modifiche dei dati controllando l'esito positivo
              if (CM.modifyFamily(citizen.getIdCitizen(), idNF) && CM.modifyEmail(
                  citizen.getIdCitizen(), email) && CM.modifyName(citizen.getIdCitizen(), name)
                  && CM.modifySurname(citizen.getIdCitizen(), surname) && idNF != 0) {
                ris = "ok";
                request.setAttribute("ris", ris);
                rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
              } else {
                ris = "fallita";
                request.setAttribute("ris", ris);
                rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
              }
            } else {
              CIManager CIM = new CIManager();
              IdentityCard CI = CIM.getCardByNumber(request.getParameter("ci"));
              if (CI != null) {  //Controllo che il nuovo capo famiglia esiste nel db
                Citizen newCapo = CM.getCitizenById(CI.id());
                NFM.setHeadOfFamily(citizen.getFamily(),
                    newCapo.getIdCitizen()); //modifico il capo famiglia del nucleo
                NFM.decrementaComponenti(citizen.getFamily());
                idNF = creaNucleoF();  //Salvo l'id del nuovo nucleo
                citizen.setFamily(idNF);  //setto l'id del nucleo del citizen
                //effettuo le modifiche dei dati controllando l'esito positivo
                if (idNF != 0) {
                  if (CM.modifyFamily(citizen.getIdCitizen(), idNF) && CM.modifyEmail(
                      citizen.getIdCitizen(), email) && CM.modifyName(citizen.getIdCitizen(), name)
                      && CM.modifySurname(citizen.getIdCitizen(), surname)) {
                    ris = "ok";
                    request.setAttribute("ris", ris);
                    rd = sc.getRequestDispatcher(
                        "/workers/index.jsp?func=operazione&page=riuscita");
                  } else {
                    ris = "fallita";
                    request.setAttribute("ris", ris);
                    rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
                  }
                } else {
                  ris = "Errore creazione nuovo nucleo";
                  request.setAttribute("ris", ris);
                  rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
                }
              } else {
                ris = "Siamo spiacenti, il codice della carta d'identit� non � presente nel db";
                request.setAttribute("ris", ris);
                rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
              }
            }
          } else {  // Se l'id del nucleo familiare non� zero, devo aggiungere il citizen ad un nucleo esistente
            if (NFM.checkFamilyId(idNF)) {  //controllo l'esistenza del nucleo nel db
              if (NFM.getNucleo(citizen.getFamily()).getCapoFamiglia() != citizen.getIdCitizen()) {
                NFM.incrementaComponenti(idNF);  //incremento i componenti del nucleo
                citizen.setFamily(idNF);  //setto l'id del nucleo del citizen
                //effettuo le modifiche dei dati controllando l'esito positivo
                if (CM.modifyFamily(citizen.getIdCitizen(), idNF) && CM.modifyEmail(
                    citizen.getIdCitizen(), email) && CM.modifyName(citizen.getIdCitizen(), name)
                    && CM.modifySurname(citizen.getIdCitizen(), surname) && idNF != 0) {
                  ris = "ok";
                  request.setAttribute("ris", ris);
                  rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
                } else {
                  ris = "fallita";
                  request.setAttribute("ris", ris);
                  rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
                }
              }
              CIManager CIM = new CIManager();
              IdentityCard CI = CIM.getCardByNumber(request.getParameter("ci"));
              if (CI != null) {  //Controllo che il nuovo capo famiglia esiste nel db
                Citizen newCapo = CM.getCitizenById(CI.id());
                NFM.setHeadOfFamily(citizen.getFamily(),
                    newCapo.getIdCitizen()); //modifico il capo famiglia del nucleo
                NFM.decrementaComponenti(citizen.getFamily());
                citizen.setFamily(idNF);  //setto l'id del nucleo del citizen
                NFM.incrementaComponenti(idNF);
                //effettuo le modifiche dei dati controllando l'esito positivo
                if (CM.modifyFamily(citizen.getIdCitizen(), idNF) && CM.modifyEmail(
                    citizen.getIdCitizen(), email) && CM.modifyName(citizen.getIdCitizen(), name)
                    && CM.modifySurname(citizen.getIdCitizen(), surname)) {
                  ris = "ok";
                  request.setAttribute("ris", ris);
                  rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
                } else {
                  ris = "fallita";
                  request.setAttribute("ris", ris);
                  rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
                }
              } else {
                ris = "Siamo spiacenti, il codice della carta d'identit� del nuovo capofamiglia non � presente nel db";
                request.setAttribute("ris", ris);
                rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
              }
            } else {
              ris = "Id non presente";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
            }
          }
          rd.forward(request, response);
          session.removeAttribute("city");
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

  private int creaNucleoF() {
    Family nf = new Family();
    nf.setHeadOfFamily(citizen.getIdCitizen());
    nf.setIdFamily(0);
    nf.setNComponenti(1);
    return NFM.insertNucleo(nf);
  }
}
