package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Insegnamento;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerClasse;
import smos.storage.ManagerInsegnamento;
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
 * Servlet utilizzata per visualizzare tutti gli anni accademici presenti nel
 * db.
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 *          
 */
public class ServletMostraInsegnanteModuloPerClasse extends HttpServlet {

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
		String gotoPage = "./persistentDataManagement/userManagement/teacherTeachings.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Collection<Insegnamento> teachingList = null;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		//User teacher = (User) session.getAttribute("user");
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore = "L'Utente collegato non ha accesso alla "
						+ "funzionalita'!";
				gotoPage = "./error.jsp";
			}
			// recuperiamo l'id della classe selezionata
			int selectedClassId = Integer.valueOf(pRichiesta.getParameter("classId"));
			ManagerInsegnamento managerTeaching = ManagerInsegnamento.ottenereIstanza();
			//Calcoliamo l'elenco degli insegnamenti associati alla class eselezionata
			teachingList = managerTeaching.ottenereInsegnamentiPerClasseId(selectedClassId);
			session.setAttribute("teachingList", teachingList);
			Classe selectedClass = ManagerClasse.ottenereIstanza().ottenereClassePerID(selectedClassId);
			session.setAttribute("selectedClass", selectedClass);
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
