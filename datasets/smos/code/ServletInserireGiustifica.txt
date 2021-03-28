package smos.application.registerManagement;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Assenza;
import smos.bean.Giustificare;
import smos.bean.Utente;
import smos.exception.EntitaDuplicataEccezione;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerRegistrati;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;

public class ServletInserireGiustifica extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1252760418542867296L;
	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	public void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		String gotoPage = "./registerManagement/showClassroomList.jsp";
		String messaggioDiErrore = "";
		
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
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
		ManagerRegistrati mR= ManagerRegistrati.ottenereIstanza();
			Giustificare justify=new Giustificare();
			justify.settareAnnoAccademico(Integer.parseInt(pRichiesta.getParameter("academicYear")));
			
			justify.settareDataGiustificare(Utility.String2Date(pRichiesta.getParameter("date")));
			
			justify.settareIdUtente(Integer.parseInt(pRichiesta.getParameter("idUser")));
			String idA= pRichiesta.getParameter("idAbsence");
			int idAbsence=Integer.parseInt(idA);
			
			//String idC = pRichiesta.getParameter("idClasse");
			//int idClasse= Integer.parseInt(idC);
			
			//gotoPage+=idClasse;
			Assenza absence = mR.ottenereAssenzaPerIdAssenza(idAbsence);
			
			if(!mR.esiste(absence)){
				messaggioDiErrore =  "assenza non prensente nel db!";	
					gotoPage = "./error.jsp";
			}
			
			//inserimento giustifica
			
			if(!mR.esiste(justify)){
				mR.inserireGiustificare(justify, absence);
				session.setAttribute("justify", justify);
				
			}else 
				throw new EntitaDuplicataEccezione("Giustifica gia' esistente");
			
		} catch (SQLException SQLException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (EntitaNonTrovataEccezione entityNotFoundException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + entityNotFoundException.getMessage();
			gotoPage = "./error.jsp";
			entityNotFoundException.printStackTrace();
		} catch (ValoreNonValidoEccezione invalidValueException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + invalidValueException.getMessage();
			gotoPage = "./error.jsp";
			invalidValueException.printStackTrace();
		} catch (EntitaDuplicataEccezione duplicatedEntityException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + duplicatedEntityException.getMessage();
			gotoPage = "./error.jsp";
			duplicatedEntityException.printStackTrace();
		} catch (IOException ioException) {
			messaggioDiErrore = Ambiente.DEFAULT_MESSAGIO_ERRORE
				+ ioException.getMessage();
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
