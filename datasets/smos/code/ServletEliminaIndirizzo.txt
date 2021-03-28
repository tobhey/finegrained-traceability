package smos.application.addressManagement;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Indirizzo;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerIndirizzo;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
/**
 * Servlet utilizzata per cancellare un indirizzo dal database
 * 
 * @author Vecchione Giuseppe
 */
public class ServletEliminaIndirizzo extends HttpServlet {

	private static final long serialVersionUID = -7383336226678925533L;
	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta, HttpServletResponse pRisposta){
		String messaggioDiErrore="";
		String gotoPage="./showAddressList";
		ManagerUtente managerUser= ManagerUtente.ottenereIstanza();
		ManagerIndirizzo managerAddress= ManagerIndirizzo.ottenereIstanza();
		HttpSession session= pRichiesta.getSession();
		Utente loggedUser= (Utente)session.getAttribute("loggedUser");
		Indirizzo address= null;
		try {
				if(loggedUser==null){		
					pRisposta.sendRedirect("./index.htm");
					return;
				}
				if(!managerUser.eAmministratore(loggedUser)){
					messaggioDiErrore= "L' utente collegato non ha accesso alla funzionalita'!";
					gotoPage="./error.jsp";
				}
				
				address= (Indirizzo)session.getAttribute("address");
				managerAddress.elimina(address);
				
		} 	  catch (IOException ioException) {
				messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
				gotoPage = "./error.jsp";
				ioException.printStackTrace();
			} catch (SQLException SQLException) {
				messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + SQLException.getMessage();
				gotoPage = "./error.jsp";
				SQLException.printStackTrace();
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
			} catch (CampoObbligatorioEccezione mandatoryFieldException) {
				messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
				+ mandatoryFieldException.getMessage();
				gotoPage = "./error.jsp";
				mandatoryFieldException.printStackTrace();
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
