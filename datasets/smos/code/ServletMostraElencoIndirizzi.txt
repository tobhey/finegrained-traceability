package smos.application.addressManagement;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import smos.Ambiente;
import smos.bean.Indirizzo;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerIndirizzo;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per visualizzare tutti gli indirizzi.
 * 
 * @author Vecchione Giuseppe
 * 
 */
public class ServletMostraElencoIndirizzi extends HttpServlet {

	
	private static final long serialVersionUID = 8797912020763935353L;
	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	
	protected void doGet(HttpServletRequest pRichiesta, HttpServletResponse pRisposta){
		String messaggioDiErrore="";
		String gotoPage="./persistentDataManagement/addressManagement/showAddressList.jsp";
		HttpSession session=pRichiesta.getSession();
		Collection<Indirizzo> addressList=null;
		ManagerUtente managerUser= ManagerUtente.ottenereIstanza();
		ManagerIndirizzo managerAddress= ManagerIndirizzo.ottenereIstanza();
		Utente loggedUser = (Utente)session.getAttribute("loggedUser");
		
		
		try {
			if(loggedUser==null){
				pRisposta.sendRedirect("./index.htm");
				return;
				}
			if(!managerUser.eAmministratore(loggedUser)){
				messaggioDiErrore="L' utente collegato non ha accesso alla funzionalita'!";
				gotoPage="./error.jsp";
				}
			addressList=managerAddress.ottenereIndirizzoElenco();
			
			
			session.setAttribute("addressList", addressList);
			pRisposta.sendRedirect(gotoPage);
			return;
				
			} catch (IOException ioException) {
				messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
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
			} catch (ValoreNonValidoEccezione invalidValueException) {
				messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
				gotoPage = "./error.jsp";
				invalidValueException.printStackTrace();
			}
		pRichiesta.getSession().setAttribute("messaggioDiErrore",messaggioDiErrore);
		
		try {
			pRisposta.sendRedirect(gotoPage);
		} catch (IOException ioException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
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
