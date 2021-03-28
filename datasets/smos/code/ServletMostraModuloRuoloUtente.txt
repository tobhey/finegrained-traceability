package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per visualizzare il form di gestione
 * dei ruoli degli utenti.
 * 
 * @author Napolitano Vincenzo.
 *
 */
public class ServletMostraModuloRuoloUtente extends HttpServlet {

	private static final long serialVersionUID = -2210761175435137331L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "./persistentDataManagement/userManagement/userRolez.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		
		
		
		boolean isTeacherBoolean = false;
		boolean isAdministratorBoolean = false;
		boolean isParentBoolean = false;
		boolean isStudentBoolean = false;
		boolean isAtaBoolean = false;
		boolean isDirectorBoolean = false;
		
		int isTeacher = 0;
		int isAdministrator = 0;
		int isDirector = 0;
		int isParent = 0;
		int isStudent = 0;
		int isAta = 0;
		
		Utente user = null;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
		
			if ((!managerUser.eAmministratore(loggedUser))) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error.jsp";
			}
			user = (Utente) session.getAttribute("user");
			//prepariamo i valori da passare alla jsp
			isTeacherBoolean = managerUser.eInsegnante(user);
			isAdministratorBoolean = managerUser.eAmministratore(user);
			isAtaBoolean = managerUser.eAtaPersone(user);
			isDirectorBoolean= managerUser.eDirettore(user);
			isStudentBoolean= managerUser.eStudente(user);
			isParentBoolean= managerUser.eGenitore(user);
		
			isTeacher = Utility.BooleanToInt(isTeacherBoolean);
			isDirector = Utility.BooleanToInt(isDirectorBoolean);
			isAdministrator = Utility.BooleanToInt(isAdministratorBoolean);
			isAta = Utility.BooleanToInt(isAtaBoolean);
			isStudent = Utility.BooleanToInt(isStudentBoolean);
			isParent = Utility.BooleanToInt(isParentBoolean);
			
			gotoPage = "./persistentDataManagement/userManagement/userRolez.jsp?isTeacher="+isTeacher+"&isAdmin="+isAdministrator+"&isAta="+isAta
			+"&isStudent="+isStudent+"&isParent="+isParent+"&isDirector="+isDirector;
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
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		}
		
		pRichiesta.getSession().setAttribute("messaggioDiErrore", messaggioDiErrore);
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
