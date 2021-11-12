package smos.application.addressManagement;

import smos.Ambiente;
import smos.bean.Indirizzo;
import smos.bean.Utente;
import smos.exception.EntitaDuplicataEccezione;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerIndirizzo;
import smos.storage.ManagerInsegnamento;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet utilizzata per assegnare Insegnamenti ad un docente
 * 
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 *          
 */
public class ServletAggiungiRimuoviInsegnamentiComeIndirizzo extends HttpServlet {



	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6692711286746163446L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./showAddressList";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerInsegnamento managerTeaching = ManagerInsegnamento.ottenereIstanza();
		ManagerIndirizzo managerAddress = ManagerIndirizzo.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
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
			// //Mi servono l'id dell'address e le due liste di insegnamenti!
			Indirizzo address = (Indirizzo) session.getAttribute("address"); 
			String[] idSelectedList = pRichiesta.getParameterValues("selectedTeachings");
			String[] idUnselectedList = pRichiesta.getParameterValues("unselectedTeachings");
			int nSelected =idSelectedList.length;
			int nUnselected =idUnselectedList.length;
			int temp;
			//Aggiungiamo gli insegnamenti selezionati!!
				for(int i=0;i<nSelected;i++){
					temp = Integer.valueOf(idSelectedList[i]);
					if(!managerAddress.avereInsegnamento(managerTeaching.ottenereInsegnamentoPerId(temp), address)){
						managerAddress.assegnareInsegnamentoComeIndirizzo(address, managerTeaching.ottenereInsegnamentoPerId(temp));
					}
				}
			//Rimuoviamo gli insegnamenti non selezionati
				for(int i=0;i<nUnselected;i++){
					temp = Integer.valueOf(idUnselectedList[i]);
					if(managerAddress.avereInsegnamento(managerTeaching.ottenereInsegnamentoPerId(temp), address)){
						managerAddress.rimuovereInsegnamentoComeIndirizzo(address, managerTeaching.ottenereInsegnamentoPerId(temp));
					}
				}
			//session.setAttribute("teachingListTeacher", listSelcected);
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
		} catch (ValoreNonValidoEccezione e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CampoObbligatorioEccezione e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntitaDuplicataEccezione e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
