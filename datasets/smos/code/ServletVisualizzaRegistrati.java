package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.RegistratiLinea;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerClasse;
import smos.storage.ManagerRegistrati;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;

public class ServletVisualizzaRegistrati extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4054623648928396283L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "./registerManagement/showRegister.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		ManagerClasse managerClassroom = ManagerClasse.ottenereIstanza();
		ManagerRegistrati  managerRegister= ManagerRegistrati.ottenereIstanza();
		Collection<RegistratiLinea> register = null;
		int year;
		int month;
		int day;
		
		String date=pRichiesta.getParameter("date");
		int idClass = Integer.valueOf(pRichiesta.getParameter("idClasse"));
		
		String [] datevalues;
		datevalues = date.split("/");
		year = Integer.valueOf(datevalues[2]);
		month = Integer.valueOf(datevalues[1]);
		day = Integer.valueOf(datevalues[0]);
		
		try {
			register= managerRegister.ottenereRegistratiPerClasseIDEData(idClass, Utility.String2Date(date));
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			} 
			if ((!managerUser.eAmministratore(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error.jsp";
			} 
			//settare le cose da passare alla session, usare session.setAttribute(String, attribute) 
			
			
			
			Classe classroom= managerClassroom.ottenereClassePerID(idClass);
			
			session.setAttribute("register", register);
			session.setAttribute("year", year);
			session.setAttribute("month", month);
			session.setAttribute("day", day);
			
			
			session.setAttribute("classroom", classroom);
			
			//prendere l'academic year dalla session
			pRisposta.sendRedirect(gotoPage);
			return; 
			
		} catch (SQLException sqlException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + sqlException.getMessage();
			gotoPage = "./error.jsp";
			sqlException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		}catch (IOException ioException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		}catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		}
		
		session.setAttribute("messaggioDiErrore", messaggioDiErrore);
		try {
			pRisposta.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		}
	}

	/**
	 * Definizione del metodo doPost
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doPost(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		this.doGet(pRichiesta, pRisposta);
	}
	
	
}
