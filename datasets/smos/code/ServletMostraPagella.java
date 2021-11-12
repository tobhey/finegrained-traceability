package smos.application.reportManagement;


import smos.Ambiente;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.bean.Voto;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
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
 * Servlet utilizzata per visualizzare tutti gli insegnamenti.
 * 
 * @author Giulio D'Amora.
 * @version 1.0
 * 
 *          
 */
public class ServletMostraPagella extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1361713427864776624L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./reportsManagement/showReports.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Collection<Voto> votesList = null;
		ManagerVoto managerVotes = ManagerVoto.ottenereIstanza();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAmministratore(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
				messaggioDiErrore = "L'Utente collegato non ha accesso alla funzionalita'!";
				gotoPage = "./error.jsp";
			}
			int studentId = Integer.valueOf(pRichiesta.getParameter("student"));
			Integer year=(Integer) session.getAttribute("selectedYear");
			Integer turn=Integer.valueOf(pRichiesta.getParameter("q"));
			session.setAttribute("q", turn);
			votesList = managerVotes.ottenereVotoPerUtenteIdAnnoTurno(studentId,year,turn);
			Utente u = (Utente) managerUser.ottenereUtentePerId(studentId);
			VoceElencoUtenti st=new VoceElencoUtenti();
			st.settareNome(u.ottenereNome());
			st.settareEMail(u.ottenereEMail());
			st.settareId(u.ottenereId());
			session.setAttribute("std", st);
			session.setAttribute("votesList", votesList);
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


