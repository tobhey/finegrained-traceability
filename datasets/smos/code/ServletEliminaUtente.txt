package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.exception.EliminaAmministratoreEccezione;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per eliminare un utente.
 * 
 * @author Napolitano Vincenzo.
 * 
 */
public class ServletEliminaUtente extends HttpServlet {

	private static final long serialVersionUID = -7693860059069872995L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "showUserList";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Collection<VoceElencoUtenti> manager = null;
		Iterator<VoceElencoUtenti> it = null;
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();

		// Verifica che l'utente abbia effettuato il login
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAmministratore(loggedUser)) &&
					(!managerUser.eAmministratore(loggedUser))) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error.jsp";
			}
		
			Utente user = (Utente) session.getAttribute("user");
			//cancella utente se non e amministratore
			if(!managerUser.eAmministratore(user)) {
				managerUser.eliminare(user);
			}
			//controllo se l'utente e amministratore e se ce ne sono degli altri
			else {
				manager = managerUser.ottenereAmministratori();
				it = manager.iterator();
				it.next();
				if (it.hasNext()) {
					managerUser.eliminare(user);
				}
				else
					throw new EliminaAmministratoreEccezione ();
			}
			
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (SQLException SQLException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (CampoObbligatorioEccezione mandatoryFieldException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		} catch (EliminaAmministratoreEccezione deleteAdministratorException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + deleteAdministratorException.getMessage();
			gotoPage = "./error.jsp";
			deleteAdministratorException.printStackTrace();
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
