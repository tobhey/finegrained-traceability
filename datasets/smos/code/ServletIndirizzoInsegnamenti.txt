package smos.application.addressManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smos.Ambiente;
import smos.bean.Insegnamento;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.*;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

public class ServletIndirizzoInsegnamenti extends HttpServlet {

	/**
	 * Servlet utilizzata per visualizzare gli insegnamenti associati ad un indirizzo.
	 * 
	 * @author Vecchione Giuseppe.
	 * 
	 */
	private static final long serialVersionUID = 239937097347087502L;

	
	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "./persistentDataManagement/addressManagement/addressTeachings.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerInsegnamento managerTeaching =ManagerInsegnamento.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error.jsp";
			}
			Collection<Insegnamento> teachingList = managerTeaching.ottenereInsegnamenti();
			session.setAttribute("teachingList", teachingList);

		} catch (NumberFormatException numberFormatException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + numberFormatException.getMessage();
			gotoPage = "./error.jsp";
			numberFormatException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (SQLException SQLException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
	} catch (ValoreNonValidoEccezione e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
