package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Insegnamento;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
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
 * Servlet utilizzata per visualizzare un report degli insegnamenti di un docente
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 *          
 */
public class ServletRapportoInsegnamenti extends HttpServlet {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 9020697390947529914L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./persistentDataManagement/userManagement/showTeacherDetails.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		Collection<Insegnamento> teachingList = null;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		Utente teacher = (Utente) session.getAttribute("user");
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
			//recuperiamo l'anno accademico selezionato
			int academicYear = Integer.valueOf(pRichiesta.getParameter("accademicYear"));
			ManagerInsegnamento managerTeaching = ManagerInsegnamento.ottenereIstanza();
			ManagerClasse managerClassroom = ManagerClasse.ottenereIstanza();
			//Calcoliamo l'elenco delle classi in cui insegna il docente in base all'anno selezionato
			Collection<Classe> classroomList = null;
			classroomList = managerClassroom.ottenereClassePerInsegnanteAnnoAccademico(teacher, academicYear);
			session.setAttribute("classroomList", classroomList);
			session.setAttribute("selectedYear", academicYear);
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
		} catch (CampoObbligatorioEccezione e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
