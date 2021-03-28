package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Giustificare;
import smos.bean.Utente;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerRegistrati;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

public class ServletEliminaGiustifica extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7692034998093997864L;

	protected void doGet(HttpServletRequest pRichiesta,
			HttpServletResponse pRisposta) {
		String gotoPage = "./registerManagement/showRegister.jsp";
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		String idClasse=(String) session.getAttribute("idClasse");
		int id= Integer.parseInt(idClasse);
		gotoPage+="?idClasse="+id;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerRegistrati mR= ManagerRegistrati.ottenereIstanza();
		// Verifica che l'utente abbia effettuato il login
		try {
			if (loggedUser == null) {
				pRisposta.sendRedirect("./index.htm");
				return;
			}
			if ((!managerUser.eAmministratore(loggedUser)) &&
					(!managerUser.eAmministratore(loggedUser))) {
				messaggioDiErrore =  "L'Utente collegato non ha accesso alla " +
						"funzionalita'!";
				gotoPage = "./error.jsp";
			}
		
			Giustificare justify = (Giustificare) session.getAttribute("justify");
			
			if(mR.esiste(justify)){
				mR.eliminareGiustificare(justify.ottenereIdGiustificare());
			}else{
				messaggioDiErrore= "impossibile cancellare la giustifica, questa non esiste!";
				gotoPage = "./error.jsp";
			}
			
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
		} catch (SQLException SQLException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
					+ SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + ioException.getMessage();
			gotoPage = "./error.jsp";
			ioException.printStackTrace();
		} catch (CampoObbligatorioEccezione mandatoryFieldException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
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
