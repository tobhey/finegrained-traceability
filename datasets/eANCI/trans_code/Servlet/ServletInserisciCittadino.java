package Servlet;

import java.io.IOException;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.Citizen;
import Bean.Family;
import DB.DbException;
import Manager.CitizenManager;
import Manager.FamilyManager;

/**
 * The ServletInsertCitizen class inserts a citizen into the database The ServletInsertCitizen class
 * has no dependencye
 *
 * @author Federico Cinque
 */
public class ServletInsertCitizen extends HttpServlet {

  private String name;
  private String surname;
  private String cf;
  private int day;
  private int month;
  private int year;
  private Date dataN = new Date();
  private String placeN;
  private String email;
  private boolean advertise;
  private int idNF;
  private String login;
  private String type;
  private Citizen citizen;
  FamilyManager NFM;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (session
        != null) {  //Se la sessione � nulla effettua il redirect alla pagina di autenticazione
      RequestDispatcher rd = null;
      ServletContext sc = getServletContext();
      String ris;
      try {
        name = request.getParameter("name");
        surname = request.getParameter("surname");
        cf = request.getParameter("cf").toUpperCase();
        day = Integer.parseInt(request.getParameter("gg"));
        month = Integer.parseInt(request.getParameter("mm"));
        year = Integer.parseInt(request.getParameter("aa"));
        dataN.setDate(day);
        dataN.setMonth(month);
        dataN.setYear(year);
        placeN = request.getParameter("ln");
        if (request.getParameter("email") != null)
          email = request.getParameter("email");
        else
          email = "";
        advertise = false;
        idNF = Integer.parseInt(request.getParameter("nucleof"));
        login = null;
        type = "Citizen";

        CitizenManager CM = new CitizenManager();
        NFM = new FamilyManager();

        citizen = new Citizen(0, cf, surname, name, dataN, placeN, email, advertise, idNF, login);

        if (idNF == 0) {  //Se l'id del nucleo familiare � zero, devo creare un nuovo nucleo

          int idC = CM.insertCitizen(citizen); //inserisco il citizen nel db
          citizen.setIdCitizen(idC);
          idNF = creaNucleoF(); //Salvo l'id del nuovo nucleo
          citizen.setFamily(idNF); //setto l'id del nucleo del citizen
          CM.modifyFamily(citizen.getIdCitizen(), idNF);
          if (idNF != 0 && idC
              != 0) { //Se gli id restituiti sono diversi da zero l'operazione � andata a buon fine
            ris = "ok";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
          } else {
            ris = "fallita";
            request.setAttribute("ris", ris);
            rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=fallita");
          }
        } else {  // Se l'id del nucleo familiare non � zero, devo aggiungere il citizen ad un nucleo esistente
          if (NFM.checkFamilyId(idNF)) {  //controllo l'esistenza del nucleo nel db
            NFM.incrementaComponenti(idNF);  //incremento i componenti del nucleo
            int idC = 0;
            if ((idC = CM.insertCitizen(citizen))
                != 0) { //inserisco il citizen nel db e controllo se l'esito � positivo
              citizen.setIdCitizen(idC);
              ris = "ok";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher("/workers/index.jsp?func=operazione&page=riuscita");
            } else {
              ris = "Errore inserimento citizen";
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
