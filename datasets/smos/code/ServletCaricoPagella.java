package smos.application.reportManagement;

import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Insegnamento;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.bean.Voto;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerInsegnamento;
import smos.storage.ManagerUtente;
import smos.storage.ManagerVoto;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per caricare la Pagella di uno studente.
 * 
 * @author Giulio D'Amora.
 * @version 1.0
 * 
 *          
 */
public class ServletCaricoPagella extends HttpServlet {

	private static final long serialVersionUID = -1045906657573424217L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./reportsManagement/updateReport.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Collection<Voto> votesList = null;
		Collection<Insegnamento> teachingList = null;
		ManagerVoto managerVotes = ManagerVoto.ottenereIstanza();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerInsegnamento managerTeaching = ManagerInsegnamento.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore = "L'Utente collegato non ha accesso alla funzionalita'!";
				gotoPage = "./error.jsp";
			}
			Classe classroom = (Classe) session.getAttribute("classroom");
			// Lista teaching
			teachingList = managerTeaching.ottenereInsegnamentiPerClasseId(classroom
					.ottenereIdIndirizzo());
			Integer year = (Integer) session.getAttribute("selectedYear");
			// Quadrimestre
			int turn = (Integer) session.getAttribute("q");

			VoceElencoUtenti student = (VoceElencoUtenti) session
					.getAttribute("student");
			// Lista Voti
			votesList = managerVotes.ottenereVotoPerUtenteIdAnnoTurno(student.ottenereId(),
					year, turn);
			session.setAttribute("teachingList", teachingList);
			session.setAttribute("votesList", votesList);
			// provare i dati
			pRisposta.sendRedirect(gotoPage);
			return;

		} catch (SQLException sqlException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ sqlException.getMessage();
			gotoPage = "./error.jsp";
			sqlException.printStackTrace();
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
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		}

		pRichiesta.getSession().setAttribute("messaggioDiErrore", messaggioDiErrore);
		try {
			pRisposta.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ ioException.getMessage();
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
