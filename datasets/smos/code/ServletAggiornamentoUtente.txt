package smos.application.userManagement;

import smos.Ambiente;
import smos.bean.Utente;
import smos.exception.EntitaDuplicataEccezione;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per modificare un utente.
 * 
 * @author Napolitano Vincenzo.
 *
 */
public class ServletAggiornamentoUtente extends HttpServlet {

	private static final long serialVersionUID = 1316473033146481065L;

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
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		Utente user = (Utente) session.getAttribute("user");
		
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if (!managerUser.eAmministratore(loggedUser)) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error.jsp";
			}
			
			user.settareNomeDiBattesimo((pRichiesta.getParameter("firstName")));
			user.settareCognome((pRichiesta.getParameter("lastName")));
			user.settareCell((pRichiesta.getParameter("cell")));
			/*
			 * verifichiamo che la login sia unica.
			 */
			String login = pRichiesta.getParameter("login");
			user.settareLogin(login);
			if (managerUser.esisteLogin(user))
				throw new ValoreNonValidoEccezione("La login inserita esiste gia'. Inserire una nuova login.");
			
			user.settarePassword(pRichiesta.getParameter("password"));
			user.settareEMail(pRichiesta.getParameter("eMail"));
			//aggiorniamo
			if (!managerUser.esiste(user)){
				managerUser.aggiornare(user);
			}else {
				int userId = managerUser.ottenereUtenteId(user);
				if (user.ottenereId()==userId)
					managerUser.aggiornare(user);
				else 
					throw new EntitaDuplicataEccezione("Utente gia esistente");
			}
			
		} catch (SQLException SQLException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		}  catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (CampoObbligatorioEccezione mandatoryFieldException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (EntitaDuplicataEccezione duplicatedEntityException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + duplicatedEntityException.getMessage();
			gotoPage = "./error.jsp";
			duplicatedEntityException.printStackTrace();
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
