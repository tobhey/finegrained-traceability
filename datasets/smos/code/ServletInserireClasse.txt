package smos.application.classroomManagement;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import smos.Ambiente;
import smos.bean.Classe;
import smos.bean.Utente;
import smos.exception.EntitaDuplicataEccezione;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.ManagerClasse;
import smos.storage.ManagerUtente;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

public class ServletInserireClasse extends HttpServlet {

	
	
	
	
	/**
	 * Servlet per inserire una classe 
	 * @author Nicola Pisanti
	 * @version 0.9
	 */
	private static final long serialVersionUID = 1355159545343902216L;

	/**
	 * Definizione del metodo doGet
	 * 
	 * @param pRichiesta
	 * @param pRisposta
	 * 
	 */
	public void doGet(HttpServletRequest pRichiesta, 
			HttpServletResponse pRisposta) {
		int aC=Integer.valueOf(pRichiesta.getParameter("academicYear"));
		String gotoPage = "./showClassroomList?academicYear="+aC;
		
		String messaggioDiErrore = "";
		HttpSession session = pRichiesta.getSession();
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		ManagerClasse managerClassroom= ManagerClasse.ottenereIstanza();
		Utente loggedUser = (Utente) session.getAttribute("loggedUser");
		
		String isWizard = "yes";
		
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
		
			int idAdd= (int) Integer.valueOf(pRichiesta.getParameter("address"));
			
			Classe classroom= new Classe();
			classroom.settareNome(pRichiesta.getParameter("name"));
			classroom.settareAnnoAccademico(aC);
			classroom.settareIdIndirizzo(idAdd);
			
			if(classroom.ottenereAnnoAccademico()<1970){
				throw new ValoreNonValidoEccezione("l'anno inserito e troppo vecchio");
			
			}
			
			if(!(managerClassroom.esiste(classroom))){
				managerClassroom.inserire(classroom);
				session.setAttribute("isWizard", isWizard);
			}else{
				throw new EntitaDuplicataEccezione("la classe gia esiste nel database");
			}
			
		} catch (SQLException SQLException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + SQLException.getMessage();
			gotoPage = "./error.jsp";
			SQLException.printStackTrace();
		} catch (ConnessioneEccezione connectionException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + connectionException.getMessage();
			gotoPage = "./error.jsp";
			connectionException.printStackTrace();
		} catch (CampoObbligatorioEccezione mandatoryFieldException) {
			messaggioDiErrore =  Ambiente.DEFAULT_MESSAGIO_ERRORE + mandatoryFieldException.getMessage();
			gotoPage = "./error.jsp";
			mandatoryFieldException.printStackTrace();
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
