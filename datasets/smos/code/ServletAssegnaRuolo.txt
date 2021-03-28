package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Ruolo;
import smos.bean.Utente;
import smos.bean.VoceElencoUtenti;
import smos.exception.EliminaManagerEccezione;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per assegnare uno o piu ruoli ad un utente.
 * 
 * @author Napolitano Vincenzo.
 * 
 */
public class ServletAssegnaRuolo extends HttpServlet {

	private static final long serialVersionUID = 537330195407987283L;
	
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
		
		Collection<VoceElencoUtenti> administrators = new Vector<VoceElencoUtenti>();
		Iterator<VoceElencoUtenti> itAdmin = null;
		
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();

		Utente loggedUser = (Utente) session.getAttribute("loggedUser");

		// Verifica che l'utente abbia effettuato il login
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
			
			Utente user = (Utente) session.getAttribute("user");
			
			administrators = managerUser.ottenereAmministratori();
			itAdmin = administrators.iterator();
			itAdmin.next();
			
			String[] selectedRoles = pRichiesta.getParameterValues("selectedRoles");
			String[] unselectedRoles = pRichiesta.getParameterValues("unselectedRoles");
			
			if (selectedRoles != null) {
				int selectedlength = selectedRoles.length;
				for (int i = 0; i < selectedlength; i++) {
					int role = Integer.valueOf(selectedRoles[i]);
					/*
					 * controlliamo se il ruolo che stiamo assegnando e'
					 * quello di docente*/
					 
					/*if ((role == Role.TEACHER) && (!managerUser.isTeacher(user))){
						gotoPage="./loadYearForTeachings";
						
					}*/
					/*
					 * controlliamo se il ruolo che stiamo assegnando e'
					 * quello di studente*/
					/*if ((role == Role.STUDENT) && (!managerUser.isStudent(user))){
						gotoPage="./showUserList";
						
					} */
					/*
					 * controlliamo se il ruolo che stiamo assegnando e'
					 * quello di genitore*/
					/*if((role==Role.PARENT)&& (!managerUser.isParent(user))){
						gotoPage="./persistentDataManagement/userManagement/showStudentParentForm.jsp";
					}*/
					managerUser.assegnareRuolo(user, role);
				}
			} 
			
			if (unselectedRoles != null) {
				int unselectedlength = unselectedRoles.length;
				for (int i = 0; i < unselectedlength; i++) {
					int role = Integer.valueOf(unselectedRoles[i]);
					if ((managerUser.eAmministratore(user))&&(!itAdmin.hasNext())&&(role==Ruolo.AMMINISTRATORE)) {
						throw new EliminaManagerEccezione ("Impossibile modificare il ruolo dell'utente, e' l'unico Amministratore del sistema! Creare un nuovo Amministratore e riprovare!");
					}
					managerUser.eliminareRuolo(user, role);
				}
			}
			
			session.setAttribute("user", user);
		} catch (NumberFormatException numberFormatException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + numberFormatException.getMessage();
			gotoPage = "./error.jsp";
			numberFormatException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (SQLException SQLException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (EliminaManagerEccezione deleteManagerException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + deleteManagerException.getMessage();
			gotoPage = "./error.jsp";
			deleteManagerException.printStackTrace();
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
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
