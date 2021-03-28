package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per visualizzare tutti gli utenti.
 * 
 * @author Napolitano Vincenzo.
 *
 */
public class ServletMostraElencoUtenti extends HttpServlet {

	private static final long serialVersionUID = -3988115259356084996L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "./persistentDataManagement/userManagement/managerUser.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Collection<VoceElencoUtenti> userList = null;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			} 
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error0.jsp";
			} 
				
			userList = managerUser.ottenereUtenteElenco();
			
					
			session.setAttribute("userList", userList);
			pRisposta.sendRedirect(gotoPage);
			return; 
			
		} catch (SQLException sqlException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + sqlException.getMessage();
			gotoPage = "./error1.jsp";
			sqlException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error2.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error3.jsp";
			connectionException.printStackTrace();
		}catch (IOException ioException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error4.jsp";
			ioException.printStackTrace();
		}catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error5.jsp";
			invalidValueException.printStackTrace();
		}
		
		pRichiesta.getSession().setAttribute("messaggioDiErrore", messaggioDiErrore);
		try {
			pRisposta.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error6.jsp";
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
