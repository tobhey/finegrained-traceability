package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.LoginEccezione;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per effettuare il login dell'utente.
 * 
 * @author Napolitano Vincenzo.
 */
public class ServletAccesso extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,HttpServletResponse pRisposta) {
		String gotoPage = "";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();		
		
		// Ottengo i dati dalla request
		String login = pRichiesta.getParameter("user");
		String password = pRichiesta.getParameter("password");
		
		// Login dell'utente
		try {
			
			ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
			
			if(managerUser.ottenereUtentePerLogin(login) != null){
			
				Utente loggedUser = managerUser.login(login, password);
				if (loggedUser != null)
					session.setAttribute("loggedUser", loggedUser);
				else throw new LoginEccezione("Nome Utente e/o Password errati!");
				
				if (managerUser.eAmministratore(loggedUser) ){
					gotoPage="./homePage/homeAdmin.jsp";
				}else if (managerUser.eInsegnante(loggedUser) ){ 
					gotoPage="./homePage/homeProfessor.jsp";
				}else if (managerUser.eStudente(loggedUser) ){ 
					gotoPage="./homePage/homeStudent.jsp";
				}else if (managerUser.eGenitore(loggedUser) ){ 
					gotoPage="./homePage/homeParent.jsp";
				}else if (managerUser.eAtaPersone(loggedUser) ){ 
					gotoPage="./homePage/homeAtaPeople.jsp";
				}else if (managerUser.eDirettore(loggedUser) ){ 
					gotoPage="./homePage/homeDirector.jsp";
				}
				
			}
			
		} catch (LoginEccezione loginException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + loginException.getMessage();
			gotoPage = "./error.jsp";
			loginException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (SQLException sqlException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + sqlException.getMessage();
			gotoPage = "./error.jsp";
			sqlException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
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
