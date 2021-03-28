package smos.application.addressManagement;

import smos.Ambiente;
import smos.bean.Indirizzo;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerIndirizzo;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per modificare un indirizzo.
 * 
 * @author Vecchione Giuseppe.
 * 
 */
public class ServletMostraDettagliIndirizzo extends HttpServlet {

	private static final long serialVersionUID = 2136348837349051766L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "./persistentDataManagement/addressManagement/showAddressDetails.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		int addressId = 0;
		Indirizzo address= null;
		ManagerIndirizzo managerAddress = ManagerIndirizzo.ottenereIstanza();
		
		
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
			addressId = Integer.valueOf(pRichiesta.getParameter("idAddress"));
			address = managerAddress.ottenereIndirizzoPerId(addressId);
			session.setAttribute("address", address);
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
