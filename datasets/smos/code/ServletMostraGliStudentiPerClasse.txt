package smos.application.reportManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerClasse;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

/**
 * Servlet utilizzata per visualizzare gli alunni associati ad una Classe.
 * 
 * @author Giulio D'Amora
 * 
 */
public class ServletMostraGliStudentiPerClasse extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2020233250419553067L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./reportsManagement/showStudentsByClass.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");

		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAmministratore(loggedUser)) && (!managerUser.eDirettore(loggedUser))) {
				messaggioDiErrore = "L'Utente collegato non ha accesso alla "
						+ "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			int idClasse = Integer.valueOf(pRichiesta
					.getParameter("idClasse"));
			Classe classroom = ManagerClasse.ottenereIstanza().ottenereClassePerID(idClasse);
			Collection<VoceElencoUtenti> students = (Collection<VoceElencoUtenti>) managerUser
					.ottenereStudentePerClasseId(idClasse);
			session.setAttribute("StudentList", students);
			session.setAttribute("classroom", classroom);

		} catch (NumberFormatException numberFormatException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ numberFormatException.getMessage();
			gotoPage = "./error.jsp";
			numberFormatException.printStackTrace();
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
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ ioException.getMessage();
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
