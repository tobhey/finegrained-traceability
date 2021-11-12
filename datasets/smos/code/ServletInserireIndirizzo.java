package smos.application.addressManagement;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import smos.Ambiente;
import smos.bean.Utente;
import smos.bean.Indirizzo;
import smos.exception.EntitaDuplicataEccezione;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerIndirizzo;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

/**
 * Servlet utilizzata per inserire un indirizzo nel database
 * 
 * @author Vecchione Giuseppe
 */
public class ServletInserireIndirizzo extends HttpServlet {

	private static final long serialVersionUID = 8318905833953187814L;
	
	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * 
	 * @param pRisposta
	 * 
	 */
	
	public void doGet(HttpServletRequest pRichiesta, HttpServletResponse pRisposta){
		String gotoPage="./showAddressList";
		String messaggioDiErrore="";
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser= ManagerUtente.ottenereIstanza();
		ManagerIndirizzo managerAddress= ManagerIndirizzo.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		try {
				if(loggedUser==null){
					pRisposta.sendRedirect("./index.htm");
					return;
				}
				if(!managerUser.eAmministratore(loggedUser)){
					messaggioDiErrore= "L' utente collegato non ha accesso alla funzionalita'!";
					gotoPage="./error.jsp";
				}
				
				Indirizzo address= new Indirizzo();
				address.settareNome(pRichiesta.getParameter("name"));
				
				/**
				 * Verifichiamo che l' indirizzo non sia presente nel database
				 * e lo inseriamo
				 */
				if(!managerAddress.esiste(address)){
					managerAddress.inserire(address);
				}else{
					throw new EntitaDuplicataEccezione("Indirizzo gia' esistente");
				}
				
			} catch (IOException ioException) {
				messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
				gotoPage = "./error.jsp";
				ioException.printStackTrace();
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
